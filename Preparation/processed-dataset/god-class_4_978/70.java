/**
   * As Jext can be runned in background mode, some operations may need to know wether
   * or not current instance is "up" or "crouched". This is the purpose of this method.
   * @return A true boolean value is returned whenever Jext is running in background mode
   */
public static boolean isRunningBg() {
    return runInBg;
}
