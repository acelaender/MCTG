package persistance;

import models.User;

import java.util.ArrayList;
import java.util.List;

public class DummyPersistanceLayer {
    private List<User> usersRegistry;

    public DummyPersistanceLayer(){
        usersRegistry = new ArrayList<>();
        usersRegistry.add(new User("mustermann", "password", "max@mustermann.at"));
        usersRegistry.add(new User("testuser", "test", "test@user.at"));
        usersRegistry.add(new User("dummy", "dummyPassword", "dummy@user.at"));
    }

    public boolean registerUser(User user){
        usersRegistry.add(user);
        return true;
    }
}
