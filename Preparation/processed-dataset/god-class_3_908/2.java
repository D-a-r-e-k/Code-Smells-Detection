//}}} 
//{{{ getBuild() 
/**
     * Gets the internal build version for jsXe. An example is 00.03.15.00
     * @return a string of the form Major.Minor.Beta.Build
     * @since jsXe 0.3pre15
     */
public static String getBuild() {
    // Major.Minor.Beta.Build 
    String major = buildProps.getProperty("major.version");
    String minor = buildProps.getProperty("minor.version");
    String beta = buildProps.getProperty("beta.version");
    String bugfix = buildProps.getProperty("build.version");
    if (major.length() == 1) {
        major = "0" + major;
    }
    if (minor.length() == 1) {
        minor = "0" + minor;
    }
    if (beta.length() == 1) {
        beta = "0" + beta;
    }
    if (bugfix.length() == 1) {
        bugfix = "0" + bugfix;
    }
    return major + "." + minor + "." + beta + "." + bugfix;
}
