public void visitCodeAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute) {
    //        DEBUG = DEBUG_RESULTS = 
    //            clazz.getName().equals("abc/Def") && 
    //            method.getName(clazz).equals("abc"); 
    // TODO: Remove this when the evaluation shrinker has stabilized. 
    // Catch any unexpected exceptions from the actual visiting method. 
    try {
        // Process the code. 
        visitCodeAttribute0(clazz, method, codeAttribute);
    } catch (RuntimeException ex) {
        System.err.println("Unexpected error while shrinking instructions after partial evaluation:");
        System.err.println("  Class       = [" + clazz.getName() + "]");
        System.err.println("  Method      = [" + method.getName(clazz) + method.getDescriptor(clazz) + "]");
        System.err.println("  Exception   = [" + ex.getClass().getName() + "] (" + ex.getMessage() + ")");
        System.err.println("Not optimizing this method");
        if (DEBUG) {
            method.accept(clazz, new ClassPrinter());
            throw ex;
        }
    }
}
