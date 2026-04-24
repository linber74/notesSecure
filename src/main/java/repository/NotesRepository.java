package repository;

import config.DatabaseConnection;
import model.Notes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NotesRepository {

    public boolean saveNote(String text, int userId) {

        String sql = "INSERT INTO notes (text, userId) VALUES (?, ?)";

        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, text);
            stmt.setInt(2, userId);

            int rows = stmt.executeUpdate();
            return (rows > 0);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Notes> getNotesByUserId(int userId) {
        String sql = "SELECT * FROM notes WHERE userId = ?";

        if (userId == -1){
            return new ArrayList<>();
        }

        try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);

            try (ResultSet rs = stmt.executeQuery()) {

                List<Notes> notesList = new ArrayList<>();
                while (rs.next()) {
                    Notes notes = new Notes();
                    notes.setNotesId(rs.getInt("notesId"));
                    notes.setText(rs.getString("text"));
                    notes.setCreatedAt(rs.getTimestamp("createdAt").toLocalDateTime());
                    notes.setUpdatedAt(rs.getTimestamp ("updatedAt").toLocalDateTime());
                    notesList.add(notes);
                }
                return notesList;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean updateNote(int notesId, String text, int userId) {

        Notes existing = getNotesById(notesId, userId);
        if (existing == null) return false;

        String sql = "UPDATE notes SET text = ? WHERE notesId = ? AND userId = ?";

        try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, text);
            stmt.setInt(2, notesId);
            stmt.setInt(3, userId);

            int rows = stmt.executeUpdate();
            return (rows > 0);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean deleteNote(int notesId, int userId) {

        Notes existing = getNotesById(notesId, userId);
        if (existing == null) return false;

        String sql = "DELETE FROM notes WHERE notesId = ? AND userId = ?";

        try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, notesId);
            stmt.setInt(2, userId);
            int rows = stmt.executeUpdate();
            return (rows > 0);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Notes> getAllNotes() {

        String sql = "SELECT * FROM notes";

        try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
            List<Notes> notesList = new ArrayList<>();

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Notes notes = new Notes();
                    notes.setNotesId(rs.getInt("notesId"));
                    notes.setText(rs.getString("text"));
                    notes.setCreatedAt(rs.getTimestamp("createdAt").toLocalDateTime());
                    notes.setUpdatedAt(rs.getTimestamp ("updatedAt").toLocalDateTime());
                    notes.setUserId(rs.getInt("userId"));
                     notesList.add(notes);
                }
                return notesList;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean deleteNoteAdmin(int notesId) {
        String sql = "DELETE FROM notes WHERE notesId = ?";
        if (notesId == -1){
            return false;
        }
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, notesId);
            int rows = stmt.executeUpdate();
            return (rows > 0);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Help function
    public Notes getNotesById(int notesId, int userId) {

        String sql = "SELECT * FROM notes WHERE notesId = ? AND userId = ?";

        if (notesId == -1 || userId == -1){
            return null;
        }

        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setInt(1, notesId);
            stmt.setInt(2, userId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Notes notes = new Notes();
                    notes.setNotesId(rs.getInt("notesId"));
                    notes.setText(rs.getString("text"));
                    notes.setCreatedAt(rs.getTimestamp("createdAt").toLocalDateTime());
                    notes.setUpdatedAt(rs.getTimestamp ("updatedAt").toLocalDateTime());
                    notes.setUserId(rs.getInt("userId"));
                    return notes;
                }
                else return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
