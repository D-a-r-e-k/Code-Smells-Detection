/**
     * Type check a call to a standard function. Insert CastExprs when needed.
     * If as a result of the insertion of a CastExpr a type check error is 
     * thrown, then catch it and re-throw it with a new "this".
     */
public Type typeCheckStandard(SymbolTable stable) throws TypeCheckError {
    _fname.clearNamespace();
    // HACK!!!  
    final int n = _arguments.size();
    final Vector argsType = typeCheckArgs(stable);
    final MethodType args = new MethodType(Type.Void, argsType);
    final MethodType ptype = lookupPrimop(stable, _fname.getLocalPart(), args);
    if (ptype != null) {
        for (int i = 0; i < n; i++) {
            final Type argType = (Type) ptype.argsType().elementAt(i);
            final Expression exp = (Expression) _arguments.elementAt(i);
            if (!argType.identicalTo(exp.getType())) {
                try {
                    _arguments.setElementAt(new CastExpr(exp, argType), i);
                } catch (TypeCheckError e) {
                    throw new TypeCheckError(this);
                }
            }
        }
        _chosenMethodType = ptype;
        return _type = ptype.resultType();
    }
    throw new TypeCheckError(this);
}
