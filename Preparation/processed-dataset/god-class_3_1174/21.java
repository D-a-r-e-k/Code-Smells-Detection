/**
     * Compute the JVM method descriptor for the method.
     */
static final String getSignature(Method meth) {
    final StringBuffer sb = new StringBuffer();
    sb.append('(');
    final Class[] params = meth.getParameterTypes();
    // avoid clone  
    for (int j = 0; j < params.length; j++) {
        sb.append(getSignature(params[j]));
    }
    return sb.append(')').append(getSignature(meth.getReturnType())).toString();
}
