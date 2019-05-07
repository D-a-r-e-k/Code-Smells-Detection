/** Gets the name of the document's main class: the document's only public class/interface or 
    * first top level class if document contains no public classes or interfaces. */
public String getMainClassName() throws ClassNameNotFoundException {
    final int oldPos = _currentLocation;
    try {
        setCurrentLocation(0);
        final String text = getText();
        // getText() is cheap if document is not resident 
        final int indexOfClass = _findKeywordAtToplevel("class", text, 0);
        final int indexOfInterface = _findKeywordAtToplevel("interface", text, 0);
        final int indexOfEnum = _findKeywordAtToplevel("enum", text, 0);
        final int indexOfPublic = _findKeywordAtToplevel("public", text, 0);
        if (indexOfPublic == -1)
            return getFirstClassName(indexOfClass, indexOfInterface, indexOfEnum);
        //        _log.log("text =\n" + text); 
        //        _log.log("indexOfClass = " + indexOfClass + "; indexOfPublic = " + indexOfPublic); 
        // There is an explicit public declaration 
        final int afterPublic = indexOfPublic + "public".length();
        final String subText = text.substring(afterPublic);
        setCurrentLocation(afterPublic);
        //        _log.log("After public text = '" + subText + "'"); 
        int indexOfPublicClass = _findKeywordAtToplevel("class", subText, afterPublic);
        // relative offset 
        if (indexOfPublicClass != -1)
            indexOfPublicClass += afterPublic;
        int indexOfPublicInterface = _findKeywordAtToplevel("interface", subText, afterPublic);
        // relative offset 
        if (indexOfPublicInterface != -1)
            indexOfPublicInterface += afterPublic;
        int indexOfPublicEnum = _findKeywordAtToplevel("enum", subText, afterPublic);
        // relative offset 
        if (indexOfPublicEnum != -1)
            indexOfPublicEnum += afterPublic;
        //        _log.log("indexOfPublicClass = " + indexOfPublicClass + " indexOfPublicInterface = " + indexOfPublicInterface); 
        return getFirstClassName(indexOfPublicClass, indexOfPublicInterface, indexOfPublicEnum);
    } finally {
        setCurrentLocation(oldPos);
    }
}
