package org.jnd.user.repositories;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.dizitart.no2.Cursor;
import org.dizitart.no2.Nitrite;
import org.dizitart.no2.exceptions.UniqueConstraintException;
import org.dizitart.no2.objects.ObjectRepository;
import org.jnd.user.controller.UserController;
import org.jnd.user.model.exceptions.IncorrectPasswordException;
import org.jnd.user.model.exceptions.UserNotFoundException;
import org.jnd.user.model.exceptions.UsernameExistsException;
import org.jnd.user.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import java.util.List;
import java.util.UUID;

import static org.dizitart.no2.objects.filters.ObjectFilters.eq;


@Component("UserRepository")
public class UserRepository{

    private static final Logger log = LoggerFactory.getLogger(UserRepository.class);

    ObjectRepository<User> repository = null;

    ObjectMapper mapper = new ObjectMapper();

    @Value("${user.db.path:db/users.db}")
    String dbPath;

    @PostConstruct
    public void init(){
        Nitrite db = Nitrite.builder()
                .compressed()
                .filePath(dbPath)
                .openOrCreate("admin", "admin");

        repository = db.getRepository(User.class);

        User justin = new User("justin", "123456", "Justin", "Davis", "justin@email.com", "127.0.0.1");

        User superman = new User("superman", "lois", "Clark", "Kent", "bird@plane.com", "127.0.0.1");

        try {
            repository.insert(justin, superman);

        }
        catch (UniqueConstraintException uce)   {
            //nevermind we already have a base users
        }

    }

    public User register(User user) throws UsernameExistsException{

        try {
            user.setId(UUID.randomUUID().toString());
            repository.insert(user);
        }
        catch (UniqueConstraintException uce)   {
            UsernameExistsException e = new UsernameExistsException("This username is already in use, please choose another.");
            throw e;
        }

        return user;
    }

    public User login(User user) throws UserNotFoundException, IncorrectPasswordException{

        User candidateUser = repository.find(eq("username", user.getUsername())).firstOrDefault();

        if (candidateUser == null)  {
            throw new UserNotFoundException("User not found.");
        }

        if (!candidateUser.getPassword().equals(user.getPassword())){
            throw new IncorrectPasswordException("Incorrect Password.");
        }

        return candidateUser;
    }

    public User get(User user) throws UserNotFoundException{

        User candidateUser = repository.find(eq("username", user.getUsername())).firstOrDefault();

        if (candidateUser == null)  {
            candidateUser = repository.find(eq("id", user.getId())).firstOrDefault();
        }

        if (candidateUser == null)  {
            throw new UserNotFoundException("User not found.");
        }
        return candidateUser;
    }

    public User get(String username) throws UserNotFoundException{

        User candidateUser = repository.find(eq("username", username)).firstOrDefault();

        if (candidateUser == null)  {
            throw new UserNotFoundException("User not found.");
        }
        return candidateUser;
    }

    public void update(User user)  {
        repository.update(user);
    }

    public Long count()  {
        return repository.size();
    }

    public User[] all()  {
        List<User> userList = repository.find().toList();
        log.info("User list : " + userList);
        User[] users = userList.toArray(new User[userList.size()]);
        return users;
    }
}
