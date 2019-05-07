/**
     * Translate a function call. The compiled code will leave the function's
     * return value on the JVM's stack.
     */
public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
    final int n = argumentCount();
    final ConstantPoolGen cpg = classGen.getConstantPool();
    final InstructionList il = methodGen.getInstructionList();
    final boolean isSecureProcessing = classGen.getParser().getXSLTC().isSecureProcessing();
    int index;
    // Translate calls to methods in the BasisLibrary  
    if (isStandard() || isExtension()) {
        for (int i = 0; i < n; i++) {
            final Expression exp = argument(i);
            exp.translate(classGen, methodGen);
            exp.startIterator(classGen, methodGen);
        }
        // append "F" to the function's name  
        final String name = _fname.toString().replace('-', '_') + "F";
        String args = Constants.EMPTYSTRING;
        // Special precautions for some method calls  
        if (name.equals("sumF")) {
            args = DOM_INTF_SIG;
            il.append(methodGen.loadDOM());
        } else if (name.equals("normalize_spaceF")) {
            if (_chosenMethodType.toSignature(args).equals("()Ljava/lang/String;")) {
                args = "I" + DOM_INTF_SIG;
                il.append(methodGen.loadContextNode());
                il.append(methodGen.loadDOM());
            }
        }
        // Invoke the method in the basis library  
        index = cpg.addMethodref(BASIS_LIBRARY_CLASS, name, _chosenMethodType.toSignature(args));
        il.append(new INVOKESTATIC(index));
    } else if (unresolvedExternal) {
        index = cpg.addMethodref(BASIS_LIBRARY_CLASS, "unresolved_externalF", "(Ljava/lang/String;)V");
        il.append(new PUSH(cpg, _fname.toString()));
        il.append(new INVOKESTATIC(index));
    } else if (_isExtConstructor) {
        if (isSecureProcessing)
            translateUnallowedExtension(cpg, il);
        final String clazz = _chosenConstructor.getDeclaringClass().getName();
        Class[] paramTypes = _chosenConstructor.getParameterTypes();
        LocalVariableGen[] paramTemp = new LocalVariableGen[n];
        // Backwards branches are prohibited if an uninitialized object is  
        // on the stack by section 4.9.4 of the JVM Specification, 2nd Ed.  
        // We don't know whether this code might contain backwards branches  
        // so we mustn't create the new object until after we've created  
        // the suspect arguments to its constructor.  Instead we calculate  
        // the values of the arguments to the constructor first, store them  
        // in temporary variables, create the object and reload the  
        // arguments from the temporaries to avoid the problem.  
        for (int i = 0; i < n; i++) {
            final Expression exp = argument(i);
            Type expType = exp.getType();
            exp.translate(classGen, methodGen);
            // Convert the argument to its Java type  
            exp.startIterator(classGen, methodGen);
            expType.translateTo(classGen, methodGen, paramTypes[i]);
            paramTemp[i] = methodGen.addLocalVariable("function_call_tmp" + i, expType.toJCType(), null, null);
            paramTemp[i].setStart(il.append(expType.STORE(paramTemp[i].getIndex())));
        }
        il.append(new NEW(cpg.addClass(_className)));
        il.append(InstructionConstants.DUP);
        for (int i = 0; i < n; i++) {
            final Expression arg = argument(i);
            paramTemp[i].setEnd(il.append(arg.getType().LOAD(paramTemp[i].getIndex())));
        }
        final StringBuffer buffer = new StringBuffer();
        buffer.append('(');
        for (int i = 0; i < paramTypes.length; i++) {
            buffer.append(getSignature(paramTypes[i]));
        }
        buffer.append(')');
        buffer.append("V");
        index = cpg.addMethodref(clazz, "<init>", buffer.toString());
        il.append(new INVOKESPECIAL(index));
        // Convert the return type back to our internal type  
        (Type.Object).translateFrom(classGen, methodGen, _chosenConstructor.getDeclaringClass());
    } else {
        if (isSecureProcessing)
            translateUnallowedExtension(cpg, il);
        final String clazz = _chosenMethod.getDeclaringClass().getName();
        Class[] paramTypes = _chosenMethod.getParameterTypes();
        // Push "this" if it is an instance method  
        if (_thisArgument != null) {
            _thisArgument.translate(classGen, methodGen);
        }
        for (int i = 0; i < n; i++) {
            final Expression exp = argument(i);
            exp.translate(classGen, methodGen);
            // Convert the argument to its Java type  
            exp.startIterator(classGen, methodGen);
            exp.getType().translateTo(classGen, methodGen, paramTypes[i]);
        }
        final StringBuffer buffer = new StringBuffer();
        buffer.append('(');
        for (int i = 0; i < paramTypes.length; i++) {
            buffer.append(getSignature(paramTypes[i]));
        }
        buffer.append(')');
        buffer.append(getSignature(_chosenMethod.getReturnType()));
        if (_thisArgument != null && _clazz.isInterface()) {
            index = cpg.addInterfaceMethodref(clazz, _fname.getLocalPart(), buffer.toString());
            il.append(new INVOKEINTERFACE(index, n + 1));
        } else {
            index = cpg.addMethodref(clazz, _fname.getLocalPart(), buffer.toString());
            il.append(_thisArgument != null ? (InvokeInstruction) new INVOKEVIRTUAL(index) : (InvokeInstruction) new INVOKESTATIC(index));
        }
        // Convert the return type back to our internal type  
        _type.translateFrom(classGen, methodGen, _chosenMethod.getReturnType());
    }
}
