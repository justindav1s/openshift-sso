/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jnd.microservices.sso.userstorage;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.keycloak.component.ComponentModel;
import org.keycloak.credential.CredentialInput;
import org.keycloak.credential.CredentialInputUpdater;
import org.keycloak.credential.CredentialInputValidator;
import org.keycloak.credential.CredentialModel;
import org.keycloak.models.*;
import org.keycloak.models.cache.CachedUserModel;
import org.keycloak.models.cache.OnUserCache;
import org.keycloak.storage.StorageId;
import org.keycloak.storage.UserStorageProvider;
import org.keycloak.storage.adapter.AbstractUserAdapter;
import org.keycloak.storage.user.UserLookupProvider;
import org.keycloak.storage.user.UserQueryProvider;
import org.keycloak.storage.user.UserRegistrationProvider;
import java.util.*;


public class RestServiceUserStorageProvider implements
        UserStorageProvider,
        UserLookupProvider,
        CredentialInputValidator,
        CredentialInputUpdater,
        UserRegistrationProvider,
        UserQueryProvider,
        OnUserCache {

    public static final String UNSET_PASSWORD="#$!-UNSET-PASSWORD";
    protected KeycloakSession session;
    protected String base_url;
    protected ComponentModel model;

    private UserServiceProxy userServiceProxy;

    public RestServiceUserStorageProvider(KeycloakSession session, ComponentModel model, String base_url) {
        System.out.println("RestServiceUserStorageProvider Constructor.");
        this.session = session;
        this.model = model;
        this.base_url = base_url;
        userServiceProxy = new UserServiceProxy(base_url);
    }

    // OnUserCache method
    @Override
    public void onCache(RealmModel realmModel, CachedUserModel cachedUserModel, UserModel userModel) {
        System.out.println("RestServiceUserStorageProvider onCache.");
    }

    // UserLookupProvider methods
    @Override
    public UserModel getUserByUsername(String username, RealmModel realm) {
        System.out.println("UserLookupProvider : getUserByUsername : " + username);
        User queryUser = new User(username);
        UserModel usermodel = null;
        User user = userServiceProxy.getUser(queryUser);
        if (user != null && user.isValid())   {
            usermodel = createUserModel(realm, user);
        }
        return usermodel;
    }


    protected UserModel createUserModel(RealmModel realm, final User user) {
        System.out.println("UserLookupProvider : createUserModel : " + user);
        List<GroupModel> groups = realm.getGroups();


        System.out.println("Groups : "+ ReflectionToStringBuilder.toString(groups));
        System.out.println("Groups size : "+ groups.size());

        final GroupModel customergroup;


        AbstractUserAdapter abstractUser = new AbstractUserAdapter(session, realm, model) {
            @Override
            public String getUsername() {
                return user.getUsername();
            }

            @Override
            public void setUsername(String username) {
                user.setUsername(username);
            }

            @Override
            public String getFirstName() {
                return user.getFirstname();
            }

            @Override
            public void setFirstName(String firstName) {
                user.setFirstname(firstName);
            }

            @Override
            public String getLastName() {
                return user.getLastname();
            }

            @Override
            public void setLastName(String lastName) {
                user.setLastname(lastName);
            }

            @Override
            public String getEmail() {
                return user.getEmail();
            }

            @Override
            public void setEmail(String email) {
                user.setEmail(email);
            }

            @Override
            public Set<GroupModel> getGroups()  {
                Set<GroupModel> groupset = new HashSet<>();
                for (GroupModel group : realm.getGroups()) {
                    if (group.getName().equals("customer")) {
                        groupset.add(group);
                    }
                }
                return groupset;
            }

            @Override
            public Map<String,List<String>> getAttributes() {
                Map<String,List<String>> attributes = new HashMap<String,List<String>>();
                List<String> values = new ArrayList<>();
                Date now = new Date();
                values.add(now.toString());
                attributes.put("LAST_LOGIN", values);
                return attributes;
            }

        };

        return abstractUser;
    }

    @Override
    public UserModel getUserById(String id, RealmModel realm) {
        System.out.println("UserLookupProvider : getUserById");
        StorageId storageId = new StorageId(id);
        String username = storageId.getExternalId();
        return getUserByUsername(username, realm);
    }

    @Override
    public UserModel getUserByEmail(String email, RealmModel realm)
    {
        System.out.println("UserLookupProvider : getUserByEmail");
        return null;
    }

    // UserQueryProvider methods

    @Override
    public int getUsersCount(RealmModel realm) {

        System.out.println("UserQueryProvider : getUsersCount");

        int count = userServiceProxy.getUsersCount();

        System.out.println("UserQueryProvider : getUsersCount : " + count);

        return count;
    }



    @Override
    public List<UserModel> getUsers(RealmModel realm) {
        System.out.println("UserQueryProvider : getUsers(RealmModel realm)");
        return getUsers(realm, 0, Integer.MAX_VALUE);
    }

    @Override
    public List<UserModel> getUsers(RealmModel realm, int firstResult, int maxResults) {
        System.out.println("UserQueryProvider : getUsers(RealmModel realm, int firstResult : "+firstResult+" int maxResults : "+maxResults+")");
        List<UserModel> usermodels = new LinkedList<>();
        User[] users = userServiceProxy.getAllUsers();

        if (users.length < maxResults)
            maxResults =users.length;

        users = Arrays.copyOfRange(users, firstResult, (firstResult+maxResults));
        for (User user : users) {
            UserModel usermodel = createUserModel(realm, user);
            usermodels.add(usermodel);
        }
        return usermodels;
    }

    // UserQueryProvider method implementations

    @Override
    public List<UserModel> searchForUser(String search, RealmModel realm) {
        System.out.println("UserQueryProvider : searchForUser(String search, RealmModel realm)");
        return searchForUser(search, realm, 0, Integer.MAX_VALUE);
    }

    @Override
    public List<UserModel> searchForUser(String search, RealmModel realm, int firstResult, int maxResults) {
        System.out.println("UserQueryProvider : searchForUser(String search, RealmModel realm, int firstResult, int maxResults)");
        List<UserModel> usermodels = new LinkedList<>();
        User[] users = userServiceProxy.getAllUsers();

        for (User user : users) {
            if (user.getUsername().contains(search)) {
                UserModel usermodel = createUserModel(realm, user);
                usermodels.add(usermodel);
            }
        }
        return usermodels;
    }

    @Override
    public List<UserModel> searchForUser(Map<String, String> params, RealmModel realm) {
        System.out.println("UserQueryProvider : searchForUser(Map<String, String> params, RealmModel realm)");
        System.out.println("UserQueryProvider : params : "+ params);
        return searchForUser(params, realm, 0, Integer.MAX_VALUE);
    }

    @Override
    public List<UserModel> searchForUser(Map<String, String> params, RealmModel realm, int firstResult, int maxResults) {
        System.out.println("UserQueryProvider : searchForUser(Map<String, String> params, RealmModel realm, int firstResult, int maxResults)");
        System.out.println("UserQueryProvider : params : "+ params);

        // only support searching by username
        String usernameSearchString = params.get("username");
        if (usernameSearchString == null) return Collections.EMPTY_LIST;
        return searchForUser(usernameSearchString, realm, firstResult, maxResults);
    }

    @Override
    public List<UserModel> getGroupMembers(RealmModel realm, GroupModel group, int firstResult, int maxResults) {
        // runtime automatically handles querying UserFederatedStorage
        return Collections.EMPTY_LIST;
    }

    @Override
    public List<UserModel> getGroupMembers(RealmModel realm, GroupModel group) {
        // runtime automatically handles querying UserFederatedStorage
        return Collections.EMPTY_LIST;
    }
    
    @Override
    public List<UserModel> getRoleMembers(RealmModel realm, RoleModel role, int firstResult, int maxResults) {
        // Not supported in federated storage
        return Collections.EMPTY_LIST;
    }

    @Override
    public List<UserModel> getRoleMembers(RealmModel realm, RoleModel role) {
        // Not supported in federated storage
        return Collections.EMPTY_LIST;
    }


    @Override
    public List<UserModel> searchForUserByUserAttribute(String attrName, String attrValue, RealmModel realm) {
        // runtime automatically handles querying UserFederatedStorage
        return Collections.EMPTY_LIST;
    }


    // UserRegistrationProvider method implementations

    @Override
    public UserModel addUser(RealmModel realm, String username) {

        User user = new User();
        user.setUsername(username);

        return createUserModel(realm, user);
    }

    @Override
    public boolean removeUser(RealmModel realm, UserModel user) {
        // call service to remove user
        return true;
    }





    // CredentialInputValidator methods

    @Override
    public boolean isConfiguredFor(RealmModel realm, UserModel user, String credentialType) {
        System.out.println("CredentialInputValidator : isConfiguredFor : " + credentialType);
        boolean configuredFor = false;

        if (credentialType.equals(CredentialModel.PASSWORD))
            configuredFor = true;

        return configuredFor;
    }

    @Override
    public boolean supportsCredentialType(String credentialType) {
        System.out.println("CredentialInputValidator : supportsCredentialType : " + credentialType);
        return credentialType.equals(CredentialModel.PASSWORD);
    }

    @Override
    public boolean isValid(RealmModel realm, UserModel usermodel, CredentialInput input) {
        System.out.println("CredentialInputValidator : isValid");
        if (!supportsCredentialType(input.getType()) || !(input instanceof UserCredentialModel)) return false;

        boolean isValid = false;
        UserCredentialModel cred = (UserCredentialModel)input;

        String password = cred.getValue();
        String username = usermodel.getUsername();
        User user = new User(username, password);
        User loggedInUser = userServiceProxy.loginUser(user);

        if ((loggedInUser != null) && loggedInUser.isValid())
            isValid = true;

        return isValid;
    }

    // CredentialInputUpdater methods

    @Override
    public boolean updateCredential(RealmModel realm, UserModel user, CredentialInput input) {
        if (!(input instanceof UserCredentialModel)) return false;
        if (!input.getType().equals(CredentialModel.PASSWORD)) return false;
        UserCredentialModel cred = (UserCredentialModel)input;
        // call update service with
        //user.getUsername(), cred.getValue());
        return true;
    }

    @Override
    public void disableCredentialType(RealmModel realm, UserModel user, String credentialType) {
        if (!credentialType.equals(CredentialModel.PASSWORD)) return;

    }

    private static final Set<String> disableableTypes = new HashSet<>();

    static {
        disableableTypes.add(CredentialModel.PASSWORD);
    }

    @Override
    public Set<String> getDisableableCredentialTypes(RealmModel realm, UserModel user) {

        return disableableTypes;
    }
    @Override
    public void close() {

    }

}
