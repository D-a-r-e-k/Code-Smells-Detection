//------------------------------------------------------------------- 
/** Convert integer array to a string. */
public static String intArrayToString(int[] intArray) {
    String ret = "";
    for (int i = 0; i < intArray.length; i++) {
        if (ret.length() > 0)
            ret = ret + "," + Integer.toString(intArray[i]);
        else
            ret = Integer.toString(intArray[i]);
    }
    return ret;
}
