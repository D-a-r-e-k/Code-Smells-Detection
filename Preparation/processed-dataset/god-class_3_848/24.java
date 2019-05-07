public static String camel(String name) {
    StringBuffer b = new StringBuffer(name.length());
    StringTokenizer t = new StringTokenizer(name);
    if (!t.hasMoreTokens())
        return name;
    b.append(t.nextToken());
    while (t.hasMoreTokens()) {
        String token = t.nextToken();
        b.append(token.substring(0, 1).toUpperCase());
        // replace spaces with camelCase  
        b.append(token.substring(1));
    }
    return b.toString();
}
