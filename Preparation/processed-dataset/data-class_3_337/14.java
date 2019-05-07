/** Given a String, return a new String will all tabs converted to spaces.  Each tab is converted 
    * to one space, since changing the number of characters within insertString screws things up.
    * @param source the String to be converted.
    * @return a String will all the tabs converted to spaces
    */
static String _removeTabs(final String source) {
    return source.replace('\t', ' ');
}
