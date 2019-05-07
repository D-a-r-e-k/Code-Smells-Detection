/**
     *  This is just a simple helper method which will first check the context
     *  if there is already an override in place, and if there is not,
     *  it will then check the given properties.
     *
     *  @param context WikiContext to check first
     *  @param props   Properties to check next
     *  @param key     What key are we searching for?
     *  @param defValue Default value for the boolean
     *  @return True or false
     */
private static boolean getLocalBooleanProperty(WikiContext context, Properties props, String key, boolean defValue) {
    Object bool = context.getVariable(key);
    if (bool != null) {
        return TextUtil.isPositive((String) bool);
    }
    return TextUtil.getBooleanProperty(props, key, defValue);
}
