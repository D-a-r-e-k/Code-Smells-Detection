/** Finds the first occurrence of the keyword within the text (located at textOffset in this documennt) that is not 
    * enclosed within a brace or comment and is followed by whitespace.
    * @param keyword the keyword for which to search
    * @param text in which to search
    * @param textOffset Offset at which the text occurs in the document
    * @return index of the keyword in text, or -1 if the keyword is not found or not followed by whitespace
    */
private int _findKeywordAtToplevel(String keyword, String text, int textOffset) {
    int oldPos = _currentLocation;
    int index = 0;
    while (true) {
        index = text.indexOf(keyword, index);
        if (index == -1)
            break;
        else {
            // found a match, check quality 
            setCurrentLocation(textOffset + index);
            // check that the keyword is not in a comment and is followed by whitespace 
            int indexPastKeyword = index + keyword.length();
            if (indexPastKeyword < text.length()) {
                if (!isShadowed() && Character.isWhitespace(text.charAt(indexPastKeyword))) {
                    // found a match but may not be at top level 
                    if (!notInBlock(index))
                        index = -1;
                    //in a paren phrase, gone too far 
                    break;
                } else
                    index++;
            } else {
                // No space found past the keyword 
                index = -1;
                break;
            }
        }
    }
    setCurrentLocation(oldPos);
    //        _log.log("findKeyWord(" + keyword + ", ..., " + textOffset + ")"); 
    return index;
}
