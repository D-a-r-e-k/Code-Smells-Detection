// callEndElement(QName,boolean):int  
// helper methods  
/**
     * Sets the scanner state.
     *
     * @param state The new scanner state.
     */
protected final void setScannerState(int state) {
    fScannerState = state;
    if (DEBUG_SCANNER_STATE) {
        System.out.print("### setScannerState: ");
        System.out.print(getScannerStateName(state));
        System.out.println();
    }
}
