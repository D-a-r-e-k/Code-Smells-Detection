/**
   * Set the splash screen text value after having checked if it
   * isn't null (in case of a new window).
   * @param text The new text
   */
public static void setSplashText(String text) {
    if (splash != null)
        splash.setText(text);
}
