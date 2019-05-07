/** Gets the name from a composed TTC file name.
     * If I have for input "myfont.ttc,2" the return will
     * be "myfont.ttc".
     * @param name the full name
     * @return the simple file name
     */
protected static String getTTCName(String name) {
    int idx = name.toLowerCase().indexOf(".ttc,");
    if (idx < 0)
        return name;
    else
        return name.substring(0, idx + 4);
}
