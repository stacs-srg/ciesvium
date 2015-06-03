package uk.ac.standrews.cs;

import java.util.logging.Logger;

/**
 * The entry point of the application.
 */
public class Application {
    
    private static final Logger LOGGER = Logger.getLogger(Application.class.getName());
    
    public static void main(String... args) {
        
        LOGGER.entering(Application.class.getName(), "main", args);
    }
}
