package config;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

public class DatabaseConnection {

    private static final Dotenv dotenv = Dotenv.load();

    private static final String URL = dotenv.get("db_url");
    private static final String USER = dotenv.get("db_user");
    private static final String PASSWORD = dotenv.get("db_password");
    private static final Logger logger = LoggerConfig.getLogger();

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {

            logger.severe(e.getMessage());
        }
        return null;
    }
}
