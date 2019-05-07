public static String compileSortRecordFactory(Vector sortObjects, ClassGenerator classGen, MethodGenerator methodGen, String sortRecordClass) {
    final XSLTC xsltc = ((Sort) sortObjects.firstElement()).getXSLTC();
    final String className = xsltc.getHelperClassName();
    final NodeSortRecordFactGenerator sortRecordFactory = new NodeSortRecordFactGenerator(className, NODE_SORT_FACTORY, className + ".java", ACC_PUBLIC | ACC_SUPER | ACC_FINAL, new String[] {}, classGen.getStylesheet());
    ConstantPoolGen cpg = sortRecordFactory.getConstantPool();
    // Add a new instance variable for each var in closure  
    final int nsorts = sortObjects.size();
    final ArrayList dups = new ArrayList();
    for (int j = 0; j < nsorts; j++) {
        final Sort sort = (Sort) sortObjects.get(j);
        final int length = (sort._closureVars == null) ? 0 : sort._closureVars.size();
        for (int i = 0; i < length; i++) {
            final VariableRefBase varRef = (VariableRefBase) sort._closureVars.get(i);
            // Discard duplicate variable references  
            if (dups.contains(varRef))
                continue;
            final VariableBase var = varRef.getVariable();
            sortRecordFactory.addField(new Field(ACC_PUBLIC, cpg.addUtf8(var.getEscapedName()), cpg.addUtf8(var.getType().toSignature()), null, cpg.getConstantPool()));
            dups.add(varRef);
        }
    }
    // Define a constructor for this class  
    final org.apache.bcel.generic.Type[] argTypes = new org.apache.bcel.generic.Type[7];
    argTypes[0] = Util.getJCRefType(DOM_INTF_SIG);
    argTypes[1] = Util.getJCRefType(STRING_SIG);
    argTypes[2] = Util.getJCRefType(TRANSLET_INTF_SIG);
    argTypes[3] = Util.getJCRefType("[" + STRING_SIG);
    argTypes[4] = Util.getJCRefType("[" + STRING_SIG);
    argTypes[5] = Util.getJCRefType("[" + STRING_SIG);
    argTypes[6] = Util.getJCRefType("[" + STRING_SIG);
    final String[] argNames = new String[7];
    argNames[0] = DOCUMENT_PNAME;
    argNames[1] = "className";
    argNames[2] = TRANSLET_PNAME;
    argNames[3] = "order";
    argNames[4] = "type";
    argNames[5] = "lang";
    argNames[6] = "case_order";
    InstructionList il = new InstructionList();
    final MethodGenerator constructor = new MethodGenerator(ACC_PUBLIC, org.apache.bcel.generic.Type.VOID, argTypes, argNames, "<init>", className, il, cpg);
    // Push all parameters onto the stack and called super.<init>()  
    il.append(ALOAD_0);
    il.append(ALOAD_1);
    il.append(ALOAD_2);
    il.append(new ALOAD(3));
    il.append(new ALOAD(4));
    il.append(new ALOAD(5));
    il.append(new ALOAD(6));
    il.append(new ALOAD(7));
    il.append(new INVOKESPECIAL(cpg.addMethodref(NODE_SORT_FACTORY, "<init>", "(" + DOM_INTF_SIG + STRING_SIG + TRANSLET_INTF_SIG + "[" + STRING_SIG + "[" + STRING_SIG + "[" + STRING_SIG + "[" + STRING_SIG + ")V")));
    il.append(RETURN);
    // Override the definition of makeNodeSortRecord()  
    il = new InstructionList();
    final MethodGenerator makeNodeSortRecord = new MethodGenerator(ACC_PUBLIC, Util.getJCRefType(NODE_SORT_RECORD_SIG), new org.apache.bcel.generic.Type[] { org.apache.bcel.generic.Type.INT, org.apache.bcel.generic.Type.INT }, new String[] { "node", "last" }, "makeNodeSortRecord", className, il, cpg);
    il.append(ALOAD_0);
    il.append(ILOAD_1);
    il.append(ILOAD_2);
    il.append(new INVOKESPECIAL(cpg.addMethodref(NODE_SORT_FACTORY, "makeNodeSortRecord", "(II)" + NODE_SORT_RECORD_SIG)));
    il.append(DUP);
    il.append(new CHECKCAST(cpg.addClass(sortRecordClass)));
    // Initialize closure in record class  
    final int ndups = dups.size();
    for (int i = 0; i < ndups; i++) {
        final VariableRefBase varRef = (VariableRefBase) dups.get(i);
        final VariableBase var = varRef.getVariable();
        final Type varType = var.getType();
        il.append(DUP);
        // Get field from factory class  
        il.append(ALOAD_0);
        il.append(new GETFIELD(cpg.addFieldref(className, var.getEscapedName(), varType.toSignature())));
        // Put field in record class  
        il.append(new PUTFIELD(cpg.addFieldref(sortRecordClass, var.getEscapedName(), varType.toSignature())));
    }
    il.append(POP);
    il.append(ARETURN);
    constructor.setMaxLocals();
    constructor.setMaxStack();
    sortRecordFactory.addMethod(constructor);
    makeNodeSortRecord.setMaxLocals();
    makeNodeSortRecord.setMaxStack();
    sortRecordFactory.addMethod(makeNodeSortRecord);
    xsltc.dumpClass(sortRecordFactory.getJavaClass());
    return className;
}
