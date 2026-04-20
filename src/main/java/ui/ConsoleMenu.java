package ui;

import model.User;
import service.AuthService;

import java.util.Scanner;

public class ConsoleMenu {

    private final Scanner scanner = new Scanner(System.in);
    private final AuthService service = new AuthService();
    private User currentUser = null;
    private boolean running = true;
    private boolean userRunning = true;

    public void start() {



        while (running) {
            System.out.println("\n--- Secure note----");
            System.out.println("1. Register new user");
            System.out.println("2. Log in");
            System.out.println("3. Exit");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> register();
                case "2"  -> login();
                case "3" -> running = false;
                default -> System.out.println("Invalid choice");
            }
        }
    }

    private void register() {
        System.out.println("Enter username: ");
        String username = scanner.nextLine();

        System.out.println("Enter password: ");
        String password = scanner.nextLine();

        boolean success = service.register(username, password);
        if (success) {
            System.out.println("User successfully registered");
        } else  {
            System.out.println("Could not save user");
        }
    }

    private void login() {
        System.out.println("Enter username: ");
        String username = scanner.nextLine();
        System.out.println("Enter password: ");
        String password = scanner.nextLine();

        currentUser = service.login(username,password);
        if (currentUser != null) {
            System.out.println("Welcome " + currentUser.getUsername());
            userMenu();
        }
        else {
            System.out.println("Invalid username or password");
        }
    }

    private void logout() {
        if (currentUser == null) {
            System.out.println("You are not logged in");
            return;
        }
        currentUser = null;
        userRunning = false;
        System.out.println("You are now logged out");
    }

    private void userMenu() {

        userRunning = true;

        while (userRunning) {
            System.out.println("\n--- User menu----");
            System.out.println("1. create note");
            System.out.println("2. View my notes");
            System.out.println("3. change notes");
            System.out.println("4. Delete notes");
            System.out.println("5. logout");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> createNotes();
                case "2" -> viewNotes();
                case "3" -> changeNotes();
                case "4" -> deleteNotes();
                case "5" -> logout();
                default -> System.out.println("Invalid choice");
            }
        }
    }
}
