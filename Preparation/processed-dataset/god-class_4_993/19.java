/**
     *  Returns a random string of six uppercase characters.
     *
     *  @return A random string
     */
private static String getUniqueID() {
    StringBuffer sb = new StringBuffer();
    Random rand = new Random();
    for (int i = 0; i < 6; i++) {
        char x = (char) ('A' + rand.nextInt(26));
        sb.append(x);
    }
    return sb.toString();
}
