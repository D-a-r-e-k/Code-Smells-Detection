// doctypeDecl(String,String,String,Augmentations) 
// 
// Private static methods 
// 
/** Returns the parser's sub-version number. */
private static int getParserSubVersion() {
    try {
        String VERSION = XercesBridge.getInstance().getVersion();
        int index1 = VERSION.indexOf('.') + 1;
        int index2 = VERSION.indexOf('.', index1);
        if (index2 == -1) {
            index2 = VERSION.length();
        }
        return Integer.parseInt(VERSION.substring(index1, index2));
    } catch (Exception e) {
        return -1;
    }
}
