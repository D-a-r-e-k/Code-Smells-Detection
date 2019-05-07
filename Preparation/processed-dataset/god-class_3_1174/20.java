/**
     * Compute the JVM signature for the class.
     */
static final String getSignature(Class clazz) {
    if (clazz.isArray()) {
        final StringBuffer sb = new StringBuffer();
        Class cl = clazz;
        while (cl.isArray()) {
            sb.append("[");
            cl = cl.getComponentType();
        }
        sb.append(getSignature(cl));
        return sb.toString();
    } else if (clazz.isPrimitive()) {
        if (clazz == Integer.TYPE) {
            return "I";
        } else if (clazz == Byte.TYPE) {
            return "B";
        } else if (clazz == Long.TYPE) {
            return "J";
        } else if (clazz == Float.TYPE) {
            return "F";
        } else if (clazz == Double.TYPE) {
            return "D";
        } else if (clazz == Short.TYPE) {
            return "S";
        } else if (clazz == Character.TYPE) {
            return "C";
        } else if (clazz == Boolean.TYPE) {
            return "Z";
        } else if (clazz == Void.TYPE) {
            return "V";
        } else {
            final String name = clazz.toString();
            ErrorMsg err = new ErrorMsg(ErrorMsg.UNKNOWN_SIG_TYPE_ERR, name);
            throw new Error(err.toString());
        }
    } else {
        return "L" + clazz.getName().replace('.', '/') + ';';
    }
}
