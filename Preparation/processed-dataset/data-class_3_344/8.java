/**
     * Type check the actual arguments of this function call.
     */
public Vector typeCheckArgs(SymbolTable stable) throws TypeCheckError {
    final Vector result = new Vector();
    final Enumeration e = _arguments.elements();
    while (e.hasMoreElements()) {
        final Expression exp = (Expression) e.nextElement();
        result.addElement(exp.typeCheck(stable));
    }
    return result;
}
