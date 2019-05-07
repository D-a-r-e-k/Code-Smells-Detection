/**
     * Tell if this character can be written without escaping.
     */
protected boolean escapingNotNeeded(char ch) {
    final boolean ret;
    if (ch < 127) {
        // This is the old/fast code here, but is this   
        // correct for all encodings?  
        if (ch >= CharInfo.S_SPACE || (CharInfo.S_LINEFEED == ch || CharInfo.S_CARRIAGERETURN == ch || CharInfo.S_HORIZONAL_TAB == ch))
            ret = true;
        else
            ret = false;
    } else {
        ret = m_encodingInfo.isInEncoding(ch);
    }
    return ret;
}
