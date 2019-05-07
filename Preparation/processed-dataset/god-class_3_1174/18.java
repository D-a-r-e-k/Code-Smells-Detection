/**
     * Returns a vector with all methods named <code>_fname</code>
     * after stripping its namespace or <code>null</code>
     * if no such methods exist.
     */
private Vector findMethods() {
    Vector result = null;
    final String namespace = _fname.getNamespace();
    if (_className != null && _className.length() > 0) {
        final int nArgs = _arguments.size();
        try {
            if (_clazz == null) {
                _clazz = ObjectFactory.findProviderClass(_className, ObjectFactory.findClassLoader(), true);
                if (_clazz == null) {
                    final ErrorMsg msg = new ErrorMsg(ErrorMsg.CLASS_NOT_FOUND_ERR, _className);
                    getParser().reportError(Constants.ERROR, msg);
                }
            }
            final String methodName = _fname.getLocalPart();
            final Method[] methods = _clazz.getMethods();
            for (int i = 0; i < methods.length; i++) {
                final int mods = methods[i].getModifiers();
                // Is it public and same number of args ?  
                if (Modifier.isPublic(mods) && methods[i].getName().equals(methodName) && methods[i].getParameterTypes().length == nArgs) {
                    if (result == null) {
                        result = new Vector();
                    }
                    result.addElement(methods[i]);
                }
            }
        } catch (ClassNotFoundException e) {
            final ErrorMsg msg = new ErrorMsg(ErrorMsg.CLASS_NOT_FOUND_ERR, _className);
            getParser().reportError(Constants.ERROR, msg);
        }
    }
    return result;
}
