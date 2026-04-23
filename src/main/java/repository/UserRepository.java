package repository;

import config.DatabaseConnection;
import model.User;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class UserRepository {

    public boolean findUsername(String username) {
        String sql = "SELECT username FROM users WHERE username = ?";

        try(Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, username);

            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean saveUser(String username, String password, String role) {

        String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";

        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setString(1, username);
            statement.setString(2, password);
            statement.setString(3, role);

            int rows = statement.executeUpdate();

            return (rows > 0);

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public User loginUser(String username, String password) {

        if (username == null || username.isBlank())
            return null;
        if (password == null || password.isBlank())
            return null;

        try (Connection connection = DatabaseConnection.getConnection();
        PreparedStatement stm = connection.prepareStatement("SELECT * FROM users WHERE username = ?");) {
            stm.setString(1, username);

            try (ResultSet rs = stm.executeQuery()){
                if (rs.next()) {
                    User user = new User();
                    user.setUserId(rs.getInt("userId"));
                    user.setUsername(rs.getString("username"));
                    user.setPassword(rs.getString("password"));
                    user.setRole(rs.getString("role"));

                    if (BCrypt.checkpw(password, user.getPassword())) {
                        return user;
                    }
                    else  {
                        return null;
                    }
                }
                else {
                    return null;
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
