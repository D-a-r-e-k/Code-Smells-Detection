/**
     * Reset all of the fields owned by ToStream class
     *
     */
private void resetToStream() {
    this.m_cdataStartCalled = false;
    /* The stream is being reset. It is one of
          * ToXMLStream, ToHTMLStream ... and this type can't be changed
          * so neither should m_charInfo which is associated with the
          * type of Stream. Just leave m_charInfo as-is for the next re-use.
          * 
          */
    // this.m_charInfo = null; // don't set to null   
    this.m_disableOutputEscapingStates.clear();
    // this.m_encodingInfo = null; // don't set to null  
    this.m_escaping = true;
    // Leave m_format alone for now - Brian M.  
    // this.m_format = null;  
    this.m_expandDTDEntities = true;
    this.m_inDoctype = false;
    this.m_ispreserve = false;
    this.m_isprevtext = false;
    this.m_isUTF8 = false;
    //  ?? used anywhere ??  
    this.m_lineSep = s_systemLineSep;
    this.m_lineSepLen = s_systemLineSep.length;
    this.m_lineSepUse = true;
    // this.m_outputStream = null; // Don't reset it may be re-used  
    this.m_preserves.clear();
    this.m_shouldFlush = true;
    this.m_spaceBeforeClose = false;
    this.m_startNewLine = false;
    this.m_writer_set_by_user = false;
}
