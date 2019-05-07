/**
     * Type check a function call. Since different type conversions apply,
     * type checking is different for standard and external (Java) functions.
     */
public Type typeCheck(SymbolTable stable) throws TypeCheckError {
    if (_type != null)
        return _type;
    final String namespace = _fname.getNamespace();
    String local = _fname.getLocalPart();
    if (isExtension()) {
        _fname = new QName(null, null, local);
        return typeCheckStandard(stable);
    } else if (isStandard()) {
        return typeCheckStandard(stable);
    } else {
        try {
            _className = getClassNameFromUri(namespace);
            final int pos = local.lastIndexOf('.');
            if (pos > 0) {
                _isStatic = true;
                if (_className != null && _className.length() > 0) {
                    _namespace_format = NAMESPACE_FORMAT_PACKAGE;
                    _className = _className + "." + local.substring(0, pos);
                } else {
                    _namespace_format = NAMESPACE_FORMAT_JAVA;
                    _className = local.substring(0, pos);
                }
                _fname = new QName(namespace, null, local.substring(pos + 1));
            } else {
                if (_className != null && _className.length() > 0) {
                    try {
                        _clazz = ObjectFactory.findProviderClass(_className, ObjectFactory.findClassLoader(), true);
                        _namespace_format = NAMESPACE_FORMAT_CLASS;
                    } catch (ClassNotFoundException e) {
                        _namespace_format = NAMESPACE_FORMAT_PACKAGE;
                    }
                } else
                    _namespace_format = NAMESPACE_FORMAT_JAVA;
                if (local.indexOf('-') > 0) {
                    local = replaceDash(local);
                }
                String extFunction = (String) _extensionFunctionTable.get(namespace + ":" + local);
                if (extFunction != null) {
                    _fname = new QName(null, null, extFunction);
                    return typeCheckStandard(stable);
                } else
                    _fname = new QName(namespace, null, local);
            }
            return typeCheckExternal(stable);
        } catch (TypeCheckError e) {
            ErrorMsg errorMsg = e.getErrorMsg();
            if (errorMsg == null) {
                final String name = _fname.getLocalPart();
                errorMsg = new ErrorMsg(ErrorMsg.METHOD_NOT_FOUND_ERR, name);
            }
            getParser().reportError(ERROR, errorMsg);
            return _type = Type.Void;
        }
    }
}
