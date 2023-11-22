package util;

public class MessageTemplatesStorage {

    public static final String ERROR_MSG = "ERROR: %s;\n";
    public static final String WARNING_MSG = "WARNING: %s;\n";
    public static final String MSG = "MSG: %s;";

    public static void log(String logMessage) {
        System.out.println(String.format(MSG, logMessage));
    }

    public static void logWarning(String errorMsg) {
        System.out.println(String.format(WARNING_MSG, errorMsg));
    }

    public static void logError(String errorMsg, Exception exception) {
        System.out.println(String.format(ERROR_MSG, errorMsg));
        exception.printStackTrace();
    }

}
