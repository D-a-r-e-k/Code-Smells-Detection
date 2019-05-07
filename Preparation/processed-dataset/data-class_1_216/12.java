//------------------------------------------------------------------------ 
/**
     * @param stringArray
     * @param delim
     * @return
     */
public static String stringArrayToString(String[] stringArray, String delim) {
    String ret = "";
    for (int i = 0; i < stringArray.length; i++) {
        if (ret.length() > 0)
            ret = ret + delim + stringArray[i];
        else
            ret = stringArray[i];
    }
    return ret;
}
