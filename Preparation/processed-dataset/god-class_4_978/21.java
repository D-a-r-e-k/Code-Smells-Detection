/**
   * Notify all instances of Jext but the one which
   * saved the file to reload recent menu
   * @param The instance which saved a file
   */
public static void recentChanged(JextFrame instance) {
    // we send the event to all the listeners available 
    JextFrame listener;
    for (int i = 0; i < instances.size(); i++) {
        listener = (JextFrame) instances.get(i);
        if (listener != instance && listener != null)
            listener.reloadRecent();
    }
}
