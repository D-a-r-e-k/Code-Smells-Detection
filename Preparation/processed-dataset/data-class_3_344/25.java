/**
     * Translate code to call the BasisLibrary.unallowed_extensionF(String)
     * method.
     */
private void translateUnallowedExtension(ConstantPoolGen cpg, InstructionList il) {
    int index = cpg.addMethodref(BASIS_LIBRARY_CLASS, "unallowed_extension_functionF", "(Ljava/lang/String;)V");
    il.append(new PUSH(cpg, _fname.toString()));
    il.append(new INVOKESTATIC(index));
}
