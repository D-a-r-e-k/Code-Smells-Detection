/**
     * To support EXSLT extensions, convert names with dash to allowable Java names: 
     * e.g., convert abc-xyz to abcXyz.
     * Note: dashes only appear in middle of an EXSLT function or element name.
     */
protected static String replaceDash(String name) {
    char dash = '-';
    StringBuffer buff = new StringBuffer("");
    for (int i = 0; i < name.length(); i++) {
        if (i > 0 && name.charAt(i - 1) == dash)
            buff.append(Character.toUpperCase(name.charAt(i)));
        else if (name.charAt(i) != dash)
            buff.append(name.charAt(i));
    }
    return buff.toString();
}
