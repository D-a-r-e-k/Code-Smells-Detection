// createObject(String,String,String):Object 
// 
// Private static methods 
// 
/** Prints a message to standard error if debugging is enabled. */
private static void debugPrintln(String msg) {
    if (DEBUG) {
        System.err.println("JAXP: " + msg);
    }
}
