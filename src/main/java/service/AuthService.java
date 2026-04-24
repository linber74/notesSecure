package service;

import model.User;
import org.mindrot.jbcrypt.BCrypt;
import repository.UserRepository;

public class AuthService {

    private final UserRepository userRepository = new UserRepository();

    private int loginAttempts = 0;
    private static final int MAX_LOGIN_ATTEMPTS = 3;

    public boolean register(String username, String password) {
        if(username == null || username.isBlank()){
            System.out.println("Username is null or blank");
            return false;
        }

        if(password == null || password.isBlank()){
            System.out.println("Password is null or blank");
            return false;
        }
        if (userRepository.findUsername(username)){
            System.out.println("Username already exists");
            return false;
        }

        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        return userRepository.saveUser(username, hashedPassword, "USER");
    }

    public User login (String username, String password) {
        if(username == null || username.isBlank()){
            System.out.println("Username is null or blank");
            return null;
        }
        if(password == null || password.isBlank()){
            System.out.println("Password is null or blank");
            return null;
        }
        if(loginAttempts >= MAX_LOGIN_ATTEMPTS){
            System.out.println("Too many login attempts");
            return null;
        }

        User user = userRepository.loginUser(username, password);

        if(user == null){
            System.out.println("Invalid username or password");
            loginAttempts++;
            return null;
        }else {
            loginAttempts = 0;
            return user;
        }
    }

    public boolean updatePassword (String username, String password) {
        if(password == null || password.isBlank()){
            System.out.println("Password is null or blank");
            return false;
        }
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        return userRepository.updatePassword(username, hashedPassword);
    }
}
