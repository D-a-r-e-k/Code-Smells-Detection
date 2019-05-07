/** Recolors the rest of the document based on the change that triggered this call. */
protected void _styleChanged() {
    int length = getLength() - _currentLocation;
    //DrJava.consoleErr().println("Changed: " + _currentLocation + ", " + length); 
    DocumentEvent evt = new DefaultDocumentEvent(_currentLocation, length, DocumentEvent.EventType.CHANGE);
    fireChangedUpdate(evt);
}
