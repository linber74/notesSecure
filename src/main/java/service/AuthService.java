package service;

import config.LoggerConfig;
import model.User;
import org.mindrot.jbcrypt.BCrypt;
import repository.UserRepository;

import java.util.logging.Logger;

public class AuthService {

    private final UserRepository userRepository = new UserRepository();
    private static final Logger logger = LoggerConfig.getLogger();

    private int loginAttempts = 0;
    private static final int MAX_LOGIN_ATTEMPTS = 3;

    public boolean register(String username, String password) {
        if(username == null || username.isBlank()){
            System.out.println("Username is null or blank");
            logger.warning("Register failed - username is null or blank");
            return false;
        }
        if(username.length() < 3 || username.length() > 25){
            System.out.println("Username length is less than 3 character or more than 25 characters");
            logger.warning("Register failed - username length is less than 3 character or more than 25 characters");
            return false;
        }

        if(password == null || password.isBlank()){
            System.out.println("Password is null or blank");
            logger.warning("Register failed - password is null or blank");
            return false;
        }

        if(!validatePassword(password, username)){
            return false;
        }

        if (userRepository.findUsername(username)){
            System.out.println("Username already exists");
            logger.warning("Register failed - username already exists");
            return false;
        }

        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        return userRepository.saveUser(username, hashedPassword, "User");
    }

    public User login (String username, String password) {
        if(username == null || username.isBlank()){
            System.out.println("Username is null or blank");
            logger.warning("Login failed - username is null or blank");
            return null;
        }
        if(password == null || password.isBlank()){
            System.out.println("Password is null or blank");
            logger.warning("Login failed for " +username+" - password is null or blank");
            return null;
        }
        if(loginAttempts >= MAX_LOGIN_ATTEMPTS){
            System.out.println("Too many login attempts");
            logger.warning("Login attempts exceeded for " +username+" - password");
            return null;
        }

        User user = userRepository.loginUser(username, password);

        if(user == null){
            loginAttempts++;
            logger.warning("Invalid username or password");
            return null;
        }else {
            loginAttempts = 0;
            logger.info(username+" Logged in successfully");
            return user;
        }
    }

    public boolean updatePassword (String username, String password) {
        if(password == null || password.isBlank()){
            System.out.println("Password is null or blank");
            logger.warning("Update failed - password is null or blank");
            return false;
        }

        if(!validatePassword(password, username)){
            return false;
        }

        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        return userRepository.updatePassword(username, hashedPassword);
    }

    private boolean validatePassword(String password, String username) {

        // password length
        if (password.length() < 9) {
            System.out.println("Password too short");
            logger.warning("Password too short");
            return false;
        }
        int counter = 0;

        // password at least one number
        for (int i = 0; i < password.length(); i++) {
            if(Character.isDigit(password.charAt(i))){
                break;
            }
            counter++;
        }
        if (counter == password.length()) {
            System.out.println("Need at least 1 digit");
            logger.warning("Need at least 1 digit:" + username);
            return false;
        }

        counter = 0;

        // password at least  one uppercase letter
        for(int i = 0; i < password.length(); i++){
            if(Character.isUpperCase(password.charAt(i))){
                break;
            }
            counter++;
        }
        if (counter == password.length()) {
            System.out.println("Need at least 1 uppercase letter");
            logger.warning("Need at least 1 uppercase letter:" + username);
            return false;
        }
        counter = 0;

        // password at least one lowercase letter
        for(int i = 0; i < password.length(); i++){
            if(Character.isLowerCase(password.charAt(i))){
                break;
            }
            counter++;
        }
        if (counter == password.length()) {
            System.out.println("Need at least 1 lowercase letter");
            logger.warning("Need at least 1 lowercase letter:" + username);
            return false;
        }

        // password at least one special char
        boolean hasSpecial = false;
        char [] passwordArray = {'!', '@', '#', '$', '&', '*', ':', ';'};

        for(int i = 0; i < password.length(); i++){
            for (char c : passwordArray) {
                if (c == password.charAt(i)) {
                    hasSpecial = true;
                    break;
                }
            }
        }

        if (!hasSpecial) {
            System.out.println("Need at least 1 special character");
            logger.warning("Need at least 1 special character:" + username);
            return false;
        }

        return true;
    }
}
