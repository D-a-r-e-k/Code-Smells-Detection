/**
   * Set the splash screen progress value after
   * having checked if it isn't null (in case of a new window).
   * @param val The new value
   */
public static void setSplashProgress(int val) {
    if (splash != null)
        splash.setProgress(val);
}
