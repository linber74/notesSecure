package service;

import model.Notes;
import repository.NotesRepository;

import java.util.List;

public class NotesService {

    private NotesRepository notesRepository = new NotesRepository();

    public boolean createNotes(String text, int userId) {
        if (text == null || text.isBlank()) {
            System.out.println("Notes can't be empty");
            return false;
        }
        return notesRepository.saveNote(text, userId);
    }

    public List<Notes> viewNotes( int userId) {

        return notesRepository.getNotesByUserId(userId);
    }


}
