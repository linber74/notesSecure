package service;

import model.Notes;
import model.User;
import repository.NotesRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class NotesService {

    private final NotesRepository notesRepository = new NotesRepository();

    public boolean createNotes(String text, int userId) {
        if (text == null || text.isBlank()) {
            System.out.println("Notes can't be empty");
            return false;
        }
        if (text.length() > 500) {
            System.out.println("Notes can't be longer than 500 characters");
            return false;
        }
        return notesRepository.saveNote(text, userId);
    }

    public List<Notes> viewNotes(int userId) {

        return notesRepository.getNotesByUserId(userId);
    }

    public boolean updateNotes(int notesId, String text, int userId) {
        if (text == null || text.isBlank()) {
            System.out.println("Notes can't be empty");
            return false;
        }

        return notesRepository.updateNote(notesId, text, userId);
    }

    public boolean deleteNotes(int notesId, int userId) {
        return notesRepository.deleteNote(notesId, userId);
    }

    public List<Notes> getAllNotes(User currentUser) {
        if(!Objects.equals(currentUser.getRole(), "Admin")){
            return new ArrayList<>();
        }
        return notesRepository.getAllNotes();
    }

    public boolean deleteNoteAdmin(User currentUser, int notesId) {
        if (!Objects.equals(currentUser.getRole(), "Admin")){
            return false;
        }
        return notesRepository.deleteNoteAdmin(notesId);
    }

}
