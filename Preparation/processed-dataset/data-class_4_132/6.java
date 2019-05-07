/******************************************************************************/
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
