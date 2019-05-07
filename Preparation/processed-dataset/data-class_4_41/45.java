// resourceId():XMLResourceIdentifier 
// 
// Protected static methods 
// 
/** Returns true if the name is a built-in XML general entity reference. */
protected static boolean builtinXmlRef(String name) {
    return name.equals("amp") || name.equals("lt") || name.equals("gt") || name.equals("quot") || name.equals("apos");
}
