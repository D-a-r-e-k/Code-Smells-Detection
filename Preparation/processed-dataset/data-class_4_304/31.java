//  
// Private methods  
//  
/** Returns the scanner state name. */
protected String getScannerStateName(int state) {
    switch(state) {
        case SCANNER_STATE_DOCTYPE:
            return "SCANNER_STATE_DOCTYPE";
        case SCANNER_STATE_ROOT_ELEMENT:
            return "SCANNER_STATE_ROOT_ELEMENT";
        case SCANNER_STATE_START_OF_MARKUP:
            return "SCANNER_STATE_START_OF_MARKUP";
        case SCANNER_STATE_COMMENT:
            return "SCANNER_STATE_COMMENT";
        case SCANNER_STATE_PI:
            return "SCANNER_STATE_PI";
        case SCANNER_STATE_CONTENT:
            return "SCANNER_STATE_CONTENT";
        case SCANNER_STATE_REFERENCE:
            return "SCANNER_STATE_REFERENCE";
        case SCANNER_STATE_END_OF_INPUT:
            return "SCANNER_STATE_END_OF_INPUT";
        case SCANNER_STATE_TERMINATED:
            return "SCANNER_STATE_TERMINATED";
        case SCANNER_STATE_CDATA:
            return "SCANNER_STATE_CDATA";
        case SCANNER_STATE_TEXT_DECL:
            return "SCANNER_STATE_TEXT_DECL";
    }
    return "??? (" + state + ')';
}
