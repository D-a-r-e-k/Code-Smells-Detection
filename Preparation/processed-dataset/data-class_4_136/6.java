/******************************************************************************/
/**
 * Helping method. Transforms a String, containing only the characters "true" or
 * "false", regardless if upper or lower case, into a boolean value.
 * 
 * @param boolValue String containing a boolean value
 * @return boolean value represented by the given string
 */
protected boolean isTrue(String boolValue) {
    if (boolValue != null) {
        if ("TRUE".equals(boolValue.toUpperCase())) {
            return true;
        } else if ("FALSE".equals(boolValue.toUpperCase())) {
            return false;
        }
    }
    return false;
}
