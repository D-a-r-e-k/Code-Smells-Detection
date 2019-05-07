/**
   * Kills splash screen
   */
public static void killSplashScreen() {
    if (splash != null) {
        splash.dispose();
        splash = null;
    }
}
