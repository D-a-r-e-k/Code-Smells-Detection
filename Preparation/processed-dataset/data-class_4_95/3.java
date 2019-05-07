/** 
	 * Returns the numerical version of the library.
	 * @since 1.4.5
	 */
public static final float getVersionNo(String ver) {
    //String ver = getVersion();  
    float version = 0;
    int i = ver.indexOf(" ");
    //check if beta  
    if (i == -1)
        i = ver.length();
    ver = ver.substring(0, i);
    i = ver.indexOf(".");
    //check for sub version  
    if (i != -1) {
        int j = ver.indexOf(".", i);
        if (j != -1) {
            ver = ver.substring(0, i) + "." + MyString.replaceAll(ver.substring(i + 1), ".", "");
        }
    }
    try {
        version = Float.parseFloat(ver);
    } catch (NumberFormatException e) {
        throw new RuntimeException("Corrupt QuickServer");
    }
    return version;
}
