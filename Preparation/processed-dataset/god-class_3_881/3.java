/** 
     * Returns array of exception handlers, empty if the instruction
     * list was null or there were no exception handlers.
     * 
     * @return   Array of CodeExceptionGen.
     */
public CodeExceptionGen[] getExceptionHandlers() {
    if (ilist != null && ceg != null) {
        return ceg;
    } else {
        return new CodeExceptionGen[0];
    }
}
