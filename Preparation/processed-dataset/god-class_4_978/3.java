/**
   * Stop the auto check function. We just interrupt the
   * <code>Thread</code> and then 'kill' it.
   */
public static void stopAutoCheck() {
    if (check != null) {
        check.interrupt();
        check = null;
    }
}
