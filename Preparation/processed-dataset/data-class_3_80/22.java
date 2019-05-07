//}}} 
//{{{ exiterror() 
/**
     * Called when crashing jsXe. jsXe prints an error message and
     * exits with the error code specifed.
     * @param view The view from which the exit was called.
     * @param error The error. Either a string or Exception.
     * @param errorcode The errorcode to exit with.
     */
public static void exiterror(Object source, Object error, int errorcode) {
    String errorhdr = "jsXe has encountered a fatal error and is unable to continue.\n";
    errorhdr += "This is most likely a bug and should be reported to the jsXe\n";
    errorhdr += "developers. Please include your jsXe.log in a bug report at\n";
    errorhdr += "http://www.sourceforge.net/projects/jsxe/\n\n";
    Log.log(Log.ERROR, source, errorhdr);
    Log.log(Log.ERROR, source, error);
    if (source != null && source instanceof Component) {
        JOptionPane.showMessageDialog((Component) source, errorhdr + error, "Fatal Error", JOptionPane.WARNING_MESSAGE);
    }
    //stop logging 
    Log.closeStream();
    System.exit(errorcode);
}
