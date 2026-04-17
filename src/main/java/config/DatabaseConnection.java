package config;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {

    private static final Properties prop = new Properties();

    static {
        try (InputStream input = DatabaseConnection.class
                .getClassLoader()
                .getResourceAsStream("Settings.properties")) {
            prop.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Kunde inte ladda Settings.properties", e);
        }
    }

    private static final String URL = prop.getProperty("db.url");
    private static final String USER = prop.getProperty("db.user");
    private static final String PASSWORD = prop.getProperty("db.password");

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
