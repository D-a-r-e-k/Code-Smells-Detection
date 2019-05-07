//}}} 
//{{{ getDefaultDocument() 
/**
     * Gets the default XML document in jsXe. This is necessary 
     * as XML documents cannot be blank files.
     * @return jsXe's default XML document.
     */
public static InputStream getDefaultDocument() {
    try {
        return new ByteArrayInputStream(DefaultDocument.getBytes("UTF-8"));
    } catch (UnsupportedEncodingException e) {
        Log.log(Log.ERROR, jsXe.class, "Broken JVM doesn't support UTF-8");
        Log.log(Log.ERROR, jsXe.class, e);
        return null;
    }
}
