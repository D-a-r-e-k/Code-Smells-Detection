//  
// Protected methods  
//  
/**
     * Ends an entity.
     *
     * @throws XNIException Thrown by entity handler to signal an error.
     */
void endEntity() throws XNIException {
    // call handler  
    if (DEBUG_BUFFER) {
        System.out.print("(endEntity: ");
        print(fCurrentEntity);
        System.out.println();
    }
    if (fEntityHandler != null) {
        fEntityHandler.endEntity(fCurrentEntity.name, null);
    }
    // Close the reader for the current entity once we're   
    // done with it, and remove it from our stack. If parsing  
    // is halted at some point, the rest of the readers on  
    // the stack will be closed during cleanup.  
    try {
        fCurrentEntity.reader.close();
    } catch (IOException e) {
    }
    // REVISIT: We should never encounter underflow if the calls  
    // to startEntity and endEntity are balanced, but guard  
    // against the EmptyStackException for now. -- mrglavas  
    if (!fReaderStack.isEmpty()) {
        fReaderStack.pop();
    }
    // Release the character buffer back to the pool for reuse  
    fCharacterBufferPool.returnBuffer(fCurrentEntity.fCharacterBuffer);
    // Release the byte buffer back to the pool for reuse  
    if (fCurrentEntity.fByteBuffer != null) {
        if (fCurrentEntity.fByteBuffer.length == fBufferSize) {
            fSmallByteBufferPool.returnBuffer(fCurrentEntity.fByteBuffer);
        } else {
            fLargeByteBufferPool.returnBuffer(fCurrentEntity.fByteBuffer);
        }
    }
    // Pop entity stack.  
    fCurrentEntity = fEntityStack.size() > 0 ? (ScannedEntity) fEntityStack.pop() : null;
    fEntityScanner.setCurrentEntity(fCurrentEntity);
    if (DEBUG_BUFFER) {
        System.out.print(")endEntity: ");
        print(fCurrentEntity);
        System.out.println();
    }
}
