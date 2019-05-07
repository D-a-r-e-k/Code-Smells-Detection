/**
     * @param string
     * @return
     */
public static int stringToInt(String string) {
    try {
        return Integer.valueOf(string).intValue();
    } catch (NumberFormatException e) {
        mLogger.debug("Invalid Integer:" + string);
    }
    return 0;
}
