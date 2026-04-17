package ui;

import service.AuthService;

import java.util.Scanner;

public class ConsoleMenu {

    private final Scanner scanner = new Scanner(System.in);
    private final AuthService service = new AuthService();

    public void start() {
        boolean running = true;

        while (running) {
            System.out.println("\n--- Secure note----");
            System.out.println("1. Register new user");
            System.out.println("2. Exit");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> register();
                case "2" -> running = false;
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
}
