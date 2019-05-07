// This method prepends "length" chars from the char array,  
// from offset 0, to the manager's fCurrentEntity.ch.  
private void fixupCurrentEntity(XMLEntityManager manager, char[] scannedChars, int length) {
    XMLEntityManager.ScannedEntity currentEntity = manager.getCurrentEntity();
    if (currentEntity.count - currentEntity.position + length > currentEntity.ch.length) {
        //resize array; this case is hard to imagine...  
        char[] tempCh = currentEntity.ch;
        currentEntity.ch = new char[length + currentEntity.count - currentEntity.position + 1];
        System.arraycopy(tempCh, 0, currentEntity.ch, 0, tempCh.length);
    }
    if (currentEntity.position < length) {
        // have to move sensitive stuff out of the way...  
        System.arraycopy(currentEntity.ch, currentEntity.position, currentEntity.ch, length, currentEntity.count - currentEntity.position);
        currentEntity.count += length - currentEntity.position;
    } else {
        // have to reintroduce some whitespace so this parses:  
        for (int i = length; i < currentEntity.position; i++) currentEntity.ch[i] = ' ';
    }
    // prepend contents...  
    System.arraycopy(scannedChars, 0, currentEntity.ch, 0, length);
    currentEntity.position = 0;
    currentEntity.baseCharOffset = 0;
    currentEntity.startPosition = 0;
    currentEntity.columnNumber = currentEntity.lineNumber = 1;
}
