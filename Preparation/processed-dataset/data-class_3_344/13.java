/**
     * Compile the function call and treat as an expression
     * Update true/false-lists.
     */
public void translateDesynthesized(ClassGenerator classGen, MethodGenerator methodGen) {
    Type type = Type.Boolean;
    if (_chosenMethodType != null)
        type = _chosenMethodType.resultType();
    final InstructionList il = methodGen.getInstructionList();
    translate(classGen, methodGen);
    if ((type instanceof BooleanType) || (type instanceof IntType)) {
        _falseList.add(il.append(new IFEQ(null)));
    }
}
