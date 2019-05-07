// setScanner(Scanner) 
/** Sets the scanner state. */
protected void setScannerState(short state) {
    fScannerState = state;
    if (DEBUG_SCANNER_STATE) {
        System.out.print("$$$ setScannerState(");
        switch(fScannerState) {
            case STATE_CONTENT:
                {
                    System.out.print("STATE_CONTENT");
                    break;
                }
            case STATE_MARKUP_BRACKET:
                {
                    System.out.print("STATE_MARKUP_BRACKET");
                    break;
                }
            case STATE_START_DOCUMENT:
                {
                    System.out.print("STATE_START_DOCUMENT");
                    break;
                }
            case STATE_END_DOCUMENT:
                {
                    System.out.print("STATE_END_DOCUMENT");
                    break;
                }
        }
        System.out.println(");");
    }
}
