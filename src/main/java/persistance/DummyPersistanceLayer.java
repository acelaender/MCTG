package persistance;

import models.User;

import java.util.ArrayList;
import java.util.List;

public class DummyPersistanceLayer {
    private static List<User> usersRegistry;

    public DummyPersistanceLayer(){
        usersRegistry = new ArrayList<>();
        //TODO Dummy Instancing
        usersRegistry.add(new User("mustermann", "password"));
        usersRegistry.add(new User("testuser", "test"));
        usersRegistry.add(new User("dummy", "dummyPassword"));
    }

    public boolean registerUser(User user){
        for (int i = 0; i < usersRegistry.size(); i++) {
            if(usersRegistry.get(i).getUsername().equals(user.getUsername())){
                return false;
            }
        }
        usersRegistry.add(user);
        return true;
    }

    public boolean loginUser(User user){
        for (int i = 0; i < usersRegistry.size(); i++) {
            if(usersRegistry.get(i).getUsername().equals(user.getUsername())){
                if(usersRegistry.get(i).getPassword().equals(user.getPassword())){
                    return true;
                }
            }
        }
        return false;
    }

}
