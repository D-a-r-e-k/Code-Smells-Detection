//}}} 
//{{{ getVersion() 
/**
     * Gets the formatted, human readable version of jsXe.
     * @return The current version of jsXe.
     */
public static String getVersion() {
    return MiscUtilities.buildToVersion(getBuild());
}
