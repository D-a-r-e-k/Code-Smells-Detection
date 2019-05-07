/**
   * Constructor for a variable stack.
   * @param initStackSize The initial stack size.  Must be at least one.  The
   *                      stack can grow if needed.
   */
public VariableStack(int initStackSize) {
    // Allow for twice as many variables as stack link entries  
    reset(initStackSize, initStackSize * 2);
}
