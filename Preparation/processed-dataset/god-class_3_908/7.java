//}}} 
//{{{ getAppTitle() 
/**
     * Gets the title of the jsXe application. Most likely "jsXe"
     * @return The title of the jsXe application.
     */
public static String getAppTitle() {
    return buildProps.getProperty("application.name");
}
