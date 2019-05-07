/**
     *  Writes HTML for error message.  Does not add it to the document, you
     *  have to do it yourself.
     *  
     *  @param error The error string.
     *  @return An Element containing the error.
     */
public static Element makeError(String error) {
    return new Element("span").setAttribute("class", "error").addContent(error);
}
