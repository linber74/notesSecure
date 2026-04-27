package ui;

import config.LoggerConfig;
import model.Notes;
import model.User;
import service.AuthService;
import service.NotesService;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;


public class ConsoleMenu {

    private final Scanner scanner = new Scanner(System.in);
    private final AuthService service = new AuthService();
    private final NotesService notesService = new NotesService();
    private User currentUser = null;
    private boolean running = true;
    private boolean userRunning = true;
    private boolean success = false;
    private List<Notes> notes= new ArrayList<>();
    private static final Logger logger = LoggerConfig.getLogger();

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
        System.out.println("Enter username (min 3 character or max 25 character): ");
        String username = scanner.nextLine();

        System.out.println("Enter password: ");
        String password = scanner.nextLine();

        try {
            success = service.register(username, password);
        }catch (RuntimeException e){
            logger.severe(e.getMessage());
            System.out.println("Something went wrong, please try again");
        }
        password = null;
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

        try {
            currentUser = service.login(username, password);
        }catch (RuntimeException e){
            logger.severe(e.getMessage());
            System.out.println("Something went wrong, please try again");
        }
        password = null;
        if (currentUser != null) {
            System.out.println("Welcome " + currentUser.getUsername());
            if(currentUser.getRole().equals("Admin")){
                adminMenu();
            } else {
                userMenu();
            }
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
            System.out.println("5. Change password");
            System.out.println("6. logout");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> createNotes();
                case "2" -> viewNotes();
                case "3" -> changeNotes();
                case "4" -> deleteNotes();
                case "5" -> updatePassword();
                case "6" -> logout();
                default -> System.out.println("Invalid choice");
            }
        }
    }

    private void adminMenu() {

        userRunning = true;

        while (userRunning) {
            System.out.println("\n--- Admin menu----");
            System.out.println("1. create note");
            System.out.println("2. View my notes");
            System.out.println("3. change notes");
            System.out.println("4. Delete notes");
            System.out.println("5. View all notes");
            System.out.println("6. Delete notes from other users");
            System.out.println("7. Change password");
            System.out.println("8. logout");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> createNotes();
                case "2" -> viewNotes();
                case "3" -> changeNotes();
                case "4" -> deleteNotes();
                case "5" -> getAllNotes();
                case "6" -> deleteNoteAdmin();
                case "7" -> updatePassword();
                case "8" -> logout();
                default -> System.out.println("Invalid choice");
            }
        }
    }

    private void createNotes() {
        System.out.println("Write Note: ");
        String text = scanner.nextLine();

        try {
            success = notesService.createNotes(text, currentUser.getUserId());
        }catch (RuntimeException e){
            logger.severe(e.getMessage());
            System.out.println("Something went wrong, please try again");
        }

        if (success) {
            System.out.println("Note Saved");
        }else {
            System.out.println("Note Not Saved");
        }
    }

    private void viewNotes() {

        try {
            notes = notesService.viewNotes(currentUser.getUserId());
            int nr = 1;
            if (notes.isEmpty()) {
                System.out.println("No notes found");
            } else {
                for (Notes note : notes) {
                    System.out.println(nr++ + " | " + note.getCreatedAt() + ": " + note.getText());
                }
            }
        } catch (RuntimeException e){
            logger.severe(e.getMessage());
            System.out.println("Something went wrong, please try again");
        }
    }

    private void changeNotes() {
        viewNotes();
        System.out.println("What Note do you want to change?: ");
        int val = Integer.parseInt(scanner.nextLine());

        System.out.println("Change Note: ");
        String newText = scanner.nextLine();

        try {
            success = notesService.updateNotes(notes.get(val - 1).getNotesId(),
                    newText, currentUser.getUserId());
        } catch (RuntimeException e){
            logger.severe(e.getMessage());
            System.out.println("Something went wrong, please try again");
        }

        if (success) {
            System.out.println("Note Updated");
        }
        else {
            System.out.println("Note Not Updated");
        }
    }

    private void deleteNotes() {
        viewNotes();
        System.out.println("What Note do you want to delete?: ");
        int val = Integer.parseInt(scanner.nextLine());

        try {
            success = notesService.deleteNotes(notes.get(val - 1).getNotesId(), currentUser.getUserId());
        } catch (RuntimeException e) {
            logger.severe(e.getMessage());
            System.out.println("Something went wrong, please try again");
        }

        if (success) {
            System.out.println("Note Deleted");
        }
        else {
            System.out.println("Note Not Deleted");
        }
    }

    private void getAllNotes() {

        try {
            List<Notes> notesList = notesService.getAllNotes(currentUser);

            for (Notes note : notesList) {
                System.out.println("User ID: " + note.getUserId() + " | " + note.getCreatedAt() + ": " + note.getText());
            }
        }catch (RuntimeException e){
            logger.severe(e.getMessage());
            System.out.println("Something went wrong, please try again");
        }
    }

    private void deleteNoteAdmin() {
        List<Notes> notesList = notesService.getAllNotes(currentUser);
        for (Notes note : notesList) {
            System.out.println("User ID: " + note.getUserId() + " | " + "Note ID: " + note.getNotesId()
                    + note.getCreatedAt() + ": " + note.getText());
        }
        System.out.println("What Note do you want to delete?: ");
        int val = Integer.parseInt(scanner.nextLine());

        try {
            success = notesService.deleteNoteAdmin(currentUser, val);
        } catch (RuntimeException e){
            logger.severe(e.getMessage());
            System.out.println("Something went wrong, please try again");
        }

        if (success) {
            System.out.println("Note Deleted");
        }
        else {
            System.out.println("Note Not Deleted");
        }
    }

    private void updatePassword() {
        System.out.println("What Password do you want?: ");
        String password = scanner.nextLine();

        System.out.println("Confirm Password: ");
        String confirmPassword = scanner.nextLine();

        if (!password.equals(confirmPassword)) {
            System.out.println("Passwords do not match");
            return;
        }
        try {
            success = service.updatePassword(currentUser.getUsername(), password);
        } catch (RuntimeException e) {
            logger.severe(e.getMessage());
            System.out.println("Something went wrong, please try again");
        }

        password = null;

        if (success) {
            System.out.println("Password Updated");
        }
        else {
            System.out.println("Password Not Updated");
        }
    }

}
