/******************************************************************************/
/**
     * Helper-method. Transforms a String into a Boolean Value. The String
     * has to contain the characters "true" or "false". upper case writings
     * of some letters doesn't matter. if the String doesn't contain "true"
     * or "false" the method returns false (easier to handle than throwing
     * an exception).
     */
private boolean isTrue(String boolValue) {
    if (boolValue != null) {
        if ("TRUE".equals(boolValue.toUpperCase())) {
            return true;
        } else if ("FALSE".equals(boolValue.toUpperCase())) {
            return false;
        }
    }
    return false;
}
