/**
     * Return the signature of the current method
     */
private String getMethodSignature(Vector argsType) {
    final StringBuffer buf = new StringBuffer(_className);
    buf.append('.').append(_fname.getLocalPart()).append('(');
    int nArgs = argsType.size();
    for (int i = 0; i < nArgs; i++) {
        final Type intType = (Type) argsType.elementAt(i);
        buf.append(intType.toString());
        if (i < nArgs - 1)
            buf.append(", ");
    }
    buf.append(')');
    return buf.toString();
}
