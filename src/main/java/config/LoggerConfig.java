package config;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


public class LoggerConfig {

    static Logger logger = Logger.getLogger
            (LoggerConfig.class.getName());

    static {
        logger.setUseParentHandlers(false);
        boolean ignored = new File("logs").mkdirs();
        FileHandler fileHandler;
        try {
            fileHandler = new FileHandler("logs/app.log", true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        fileHandler.setFormatter(new SimpleFormatter());
        logger.addHandler(fileHandler);
    }

    public static Logger getLogger() {
        return logger;
    }

}
