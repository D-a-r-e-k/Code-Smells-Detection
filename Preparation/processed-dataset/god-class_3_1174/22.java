/**
     * Compute the JVM constructor descriptor for the constructor.
     */
static final String getSignature(Constructor cons) {
    final StringBuffer sb = new StringBuffer();
    sb.append('(');
    final Class[] params = cons.getParameterTypes();
    // avoid clone  
    for (int j = 0; j < params.length; j++) {
        sb.append(getSignature(params[j]));
    }
    return sb.append(")V").toString();
}
