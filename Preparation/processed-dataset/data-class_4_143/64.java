/**
   * Stops the Jext server which loads every Jext instance in the same JVM.
   */
public static void stopServer() {
    if (jextLoader != null) {
        jextLoader.stop();
        jextLoader = null;
    }
}
