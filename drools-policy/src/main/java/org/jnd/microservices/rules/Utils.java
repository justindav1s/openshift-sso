package org.jnd.microservices.rules;

import org.drools.core.spi.KnowledgeHelper;
import org.keycloak.authorization.attribute.Attributes;
import org.keycloak.authorization.identity.Identity;
import org.keycloak.authorization.permission.ResourcePermission;
import org.keycloak.authorization.policy.evaluation.EvaluationContext;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Utils {

    private static final org.jboss.logging.Logger log = org.jboss.logging.Logger.getLogger(Utils.class);

    public static void help(final KnowledgeHelper drools, final String message){
        System.out.println(message);
        System.out.println("\nrule triggered: " + drools.getRule().getName());
    }

    public static void helper(final KnowledgeHelper drools){
        System.out.println("\nrule triggered: " + drools.getRule().getName());
    }

    public static void logmessage(final String message){
        log.info(message);
    }

    public static void logContext(EvaluationContext ec){
        Attributes attributes = ec.getAttributes();
        Map<String, Collection<String>> attrMap = attributes.toMap();
        Set<String> keys = attrMap.keySet();
        for (String key : keys) {
            log.info("Context Attribute key : "+key+" val : " + attrMap.get(key));
        }
    }

    public static void logIdentity(Identity id){
        log.info("Identity ID : "+id.getId());
        Attributes attributes = id.getAttributes();
        Map<String, Collection<String>> attrMap = attributes.toMap();
        Set<String> keys = attrMap.keySet();
        for (String key : keys) {
            log.info("Identity Attribute key : "+key+" val : " + attrMap.get(key));
        }
    }

    public static void logPermission(ResourcePermission perm){
        log.info("Permission Resource ID : "+perm.getResource().getId());
        log.info("Permission Resource Name : "+perm.getResource().getName());
        log.info("Permission Resource Owner : "+perm.getResource().getOwner());
        log.info("Permission Resource Type : "+perm.getResource().getType());
        log.info("Permission Resource Uri : "+perm.getResource().getUri());
        log.info("Permission Resource Server : "+perm.getResource().getResourceServer().getId());
        Map<String, Set<String>> claimsMap = perm.getClaims();
        Set<String> keys = claimsMap.keySet();
        for (String key : keys) {
            log.info("Permission Claim key : "+key+" val : " + claimsMap.get(key));
        }
    }

    public static boolean validateRequest(Identity id, EvaluationContext ec){
        String ip = ec.getAttributes().getValue("kc.client.network.ip_address").asString(0);
        String owner = id.getAttributes().getValue("preferred_username").asString(0);
        log.info("Validating IP : "+ip+" for user : "+owner);
        IPAddress ipaddress = new IPAddress(owner, ip);

        IPServiceProxy proxy = new IPServiceProxy("http://127.0.0.1:8090");
        ipaddress = proxy.validate(ipaddress);
        return ipaddress.isGranted();
    }

}

