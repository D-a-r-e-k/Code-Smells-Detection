public Type typeCheckConstructor(SymbolTable stable) throws TypeCheckError {
    final Vector constructors = findConstructors();
    if (constructors == null) {
        // Constructor not found in this class  
        throw new TypeCheckError(ErrorMsg.CONSTRUCTOR_NOT_FOUND, _className);
    }
    final int nConstructors = constructors.size();
    final int nArgs = _arguments.size();
    final Vector argsType = typeCheckArgs(stable);
    // Try all constructors   
    int bestConstrDistance = Integer.MAX_VALUE;
    _type = null;
    // reset  
    for (int j, i = 0; i < nConstructors; i++) {
        // Check if all parameters to this constructor can be converted  
        final Constructor constructor = (Constructor) constructors.elementAt(i);
        final Class[] paramTypes = constructor.getParameterTypes();
        Class extType = null;
        int currConstrDistance = 0;
        for (j = 0; j < nArgs; j++) {
            // Convert from internal (translet) type to external (Java) type  
            extType = paramTypes[j];
            final Type intType = (Type) argsType.elementAt(j);
            Object match = _internal2Java.maps(intType, extType);
            if (match != null) {
                currConstrDistance += ((JavaType) match).distance;
            } else if (intType instanceof ObjectType) {
                ObjectType objectType = (ObjectType) intType;
                if (objectType.getJavaClass() == extType)
                    continue;
                else if (extType.isAssignableFrom(objectType.getJavaClass()))
                    currConstrDistance += 1;
                else {
                    currConstrDistance = Integer.MAX_VALUE;
                    break;
                }
            } else {
                // no mapping available  
                currConstrDistance = Integer.MAX_VALUE;
                break;
            }
        }
        if (j == nArgs && currConstrDistance < bestConstrDistance) {
            _chosenConstructor = constructor;
            _isExtConstructor = true;
            bestConstrDistance = currConstrDistance;
            _type = (_clazz != null) ? Type.newObjectType(_clazz) : Type.newObjectType(_className);
        }
    }
    if (_type != null) {
        return _type;
    }
    throw new TypeCheckError(ErrorMsg.ARGUMENT_CONVERSION_ERR, getMethodSignature(argsType));
}
