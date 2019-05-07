/**
   * Notify all instances of Jext and all properties listeners to reload properties.
   */
public static void propertiesChanged() {
    // we send the event to all the listeners available 
    for (int i = 0; i < instances.size(); i++) ((JextFrame) instances.get(i)).loadProperties();
}
