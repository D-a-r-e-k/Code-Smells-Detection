/**
     *  Description of the Method
     *
     *@param  is  Description of the Parameter
     *@return     Description of the Return Value
     */
private Vector htmlFindFiles(InputStream is) {
    Vector v = new Vector();
    HtmlParserCallback callback = new HtmlParserCallback(v);
    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
    try {
        new ParserDelegator().parse(bufferedReader, callback, false);
        is.close();
    } catch (IOException e) {
        e.printStackTrace();
    }
    return v;
}
