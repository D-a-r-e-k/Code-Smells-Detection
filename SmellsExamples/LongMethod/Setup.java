import java.awt.*;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;

public class Setup {

    private final String ERROR_LOG_FILENAME = "Errors.txt";
    private final String WARNING_LOG_FILENAME = "Warnings.txt";
    private final String DEBUG_LOG_FILENAME = "Debug.txt";

    private final String CURTAIN_R = "CURTAIN_R";
    private final String CURTAIN_G = "CURTAIN_G";
    private final String CURTAIN_B = "CURTAIN_B";
    private final String CURTAIN_A = "CURTAIN_A";

    private Color currentColor;

    public void doSetup() {

        // (1) make sure the code only runs on max os x
        boolean mrjVersionExists = System.getProperty("mrj.version") != null;
        boolean osNameExists = System.getProperty("os.name").startsWith("Mac OS");

        if (!mrjVersionExists || !osNameExists) {
            System.err.println("Not running on a Mac OS X system.");
            System.exit(1);
        }

        // (2) do all the logfile setup stuff
        Level currentLoggingLevel = Level.OFF;

        File errorFile = new File(ERROR_LOG_FILENAME);
        File warningFile = new File(WARNING_LOG_FILENAME);
        File debugFile= new File(DEBUG_LOG_FILENAME);

        if (errorFile.exists()) {
            currentLoggingLevel = Level.SEVERE;
        }

        if (warningFile.exists()) {
            currentLoggingLevel = Level.WARNING;
        }

        if (debugFile.exists()) {
            currentLoggingLevel = Level.INFO;
        }

        Logger logger = Logger.getLogger(Setup.class.getName());
        logger.setLevel(currentLoggingLevel);

        // (3, 4) do all the preferences stuff, and get the default color
        var preferences = Preferences.userNodeForPackage(this.getClass());
        int r = preferences.getInt(CURTAIN_R, 0);
        int g = preferences.getInt(CURTAIN_G, 0);
        int b = preferences.getInt(CURTAIN_B, 0);
        int a = preferences.getInt(CURTAIN_A, 255);

        currentColor = new Color(r,g,b,a);
    }
}
