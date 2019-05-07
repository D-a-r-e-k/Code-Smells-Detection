/**
     *  Description of the Method
     *
     *@param  is    Description of the Parameter
     *@param  from  Description of the Parameter
     *@return       Description of the Return Value
     */
private Vector htmlFindFilesVersioning(InputStream is, URL from) {
    Vector v = new Vector();
    HtmlVersioningParserCallback callback = new HtmlVersioningParserCallback(v, from);
    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
    try {
        new ParserDelegator().parse(bufferedReader, callback, false);
    } catch (IOException e) {
        e.printStackTrace();
    }
    return v;
}
