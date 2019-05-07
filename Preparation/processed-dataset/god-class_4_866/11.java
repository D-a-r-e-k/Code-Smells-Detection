// getLineNumber():int 
/** Returns the current column number. */
public int getColumnNumber() {
    return fCurrentEntity != null ? fCurrentEntity.getColumnNumber() : -1;
}
