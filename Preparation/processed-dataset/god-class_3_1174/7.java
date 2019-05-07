/**
     * Type check a call to an external (Java) method.
     * The method must be static an public, and a legal type conversion
     * must exist for all its arguments and its return type.
     * Every method of name <code>_fname</code> is inspected
     * as a possible candidate.
     */
public Type typeCheckExternal(SymbolTable stable) throws TypeCheckError {
    int nArgs = _arguments.size();
    final String name = _fname.getLocalPart();
    // check if function is a contructor 'new'  
    if (_fname.getLocalPart().equals("new")) {
        return typeCheckConstructor(stable);
    } else {
        boolean hasThisArgument = false;
        if (nArgs == 0)
            _isStatic = true;
        if (!_isStatic) {
            if (_namespace_format == NAMESPACE_FORMAT_JAVA || _namespace_format == NAMESPACE_FORMAT_PACKAGE)
                hasThisArgument = true;
            Expression firstArg = (Expression) _arguments.elementAt(0);
            Type firstArgType = (Type) firstArg.typeCheck(stable);
            if (_namespace_format == NAMESPACE_FORMAT_CLASS && firstArgType instanceof ObjectType && _clazz != null && _clazz.isAssignableFrom(((ObjectType) firstArgType).getJavaClass()))
                hasThisArgument = true;
            if (hasThisArgument) {
                _thisArgument = (Expression) _arguments.elementAt(0);
                _arguments.remove(0);
                nArgs--;
                if (firstArgType instanceof ObjectType) {
                    _className = ((ObjectType) firstArgType).getJavaClassName();
                } else
                    throw new TypeCheckError(ErrorMsg.NO_JAVA_FUNCT_THIS_REF, name);
            }
        } else if (_className.length() == 0) {
            /*
		 * Warn user if external function could not be resolved.
		 * Warning will _NOT_ be issued is the call is properly
		 * wrapped in an <xsl:if> or <xsl:when> element. For details
		 * see If.parserContents() and When.parserContents()
		 */
            final Parser parser = getParser();
            if (parser != null) {
                reportWarning(this, parser, ErrorMsg.FUNCTION_RESOLVE_ERR, _fname.toString());
            }
            unresolvedExternal = true;
            return _type = Type.Int;
        }
    }
    final Vector methods = findMethods();
    if (methods == null) {
        // Method not found in this class  
        throw new TypeCheckError(ErrorMsg.METHOD_NOT_FOUND_ERR, _className + "." + name);
    }
    Class extType = null;
    final int nMethods = methods.size();
    final Vector argsType = typeCheckArgs(stable);
    // Try all methods to identify the best fit   
    int bestMethodDistance = Integer.MAX_VALUE;
    _type = null;
    // reset internal type   
    for (int j, i = 0; i < nMethods; i++) {
        // Check if all paramteters to this method can be converted  
        final Method method = (Method) methods.elementAt(i);
        final Class[] paramTypes = method.getParameterTypes();
        int currMethodDistance = 0;
        for (j = 0; j < nArgs; j++) {
            // Convert from internal (translet) type to external (Java) type  
            extType = paramTypes[j];
            final Type intType = (Type) argsType.elementAt(j);
            Object match = _internal2Java.maps(intType, extType);
            if (match != null) {
                currMethodDistance += ((JavaType) match).distance;
            } else {
                // no mapping available  
                //  
                // Allow a Reference type to match any external (Java) type at  
                // the moment. The real type checking is performed at runtime.  
                if (intType instanceof ReferenceType) {
                    currMethodDistance += 1;
                } else if (intType instanceof ObjectType) {
                    ObjectType object = (ObjectType) intType;
                    if (extType.getName().equals(object.getJavaClassName()))
                        currMethodDistance += 0;
                    else if (extType.isAssignableFrom(object.getJavaClass()))
                        currMethodDistance += 1;
                    else {
                        currMethodDistance = Integer.MAX_VALUE;
                        break;
                    }
                } else {
                    currMethodDistance = Integer.MAX_VALUE;
                    break;
                }
            }
        }
        if (j == nArgs) {
            // Check if the return type can be converted  
            extType = method.getReturnType();
            _type = (Type) _java2Internal.get(extType);
            if (_type == null) {
                _type = Type.newObjectType(extType);
            }
            // Use this method if all parameters & return type match  
            if (_type != null && currMethodDistance < bestMethodDistance) {
                _chosenMethod = method;
                bestMethodDistance = currMethodDistance;
            }
        }
    }
    // It is an error if the chosen method is an instance menthod but we don't  
    // have a this argument.  
    if (_chosenMethod != null && _thisArgument == null && !Modifier.isStatic(_chosenMethod.getModifiers())) {
        throw new TypeCheckError(ErrorMsg.NO_JAVA_FUNCT_THIS_REF, getMethodSignature(argsType));
    }
    if (_type != null) {
        if (_type == Type.NodeSet) {
            getXSLTC().setMultiDocument(true);
        }
        return _type;
    }
    throw new TypeCheckError(ErrorMsg.ARGUMENT_CONVERSION_ERR, getMethodSignature(argsType));
}
