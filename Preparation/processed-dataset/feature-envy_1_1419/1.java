/**
     * Generates a method.
     *
     * @param method the method to generate code for
     * @param writer the writer to write to
     * @throws IOException for any I/O error
     */
protected void generateMethod(Method method, SourceWriter writer) throws IOException {
    Class returnType = method.getReturnType();
    Class[] argTypes = method.getParameterTypes();
    Class[] exceptionTypes = method.getExceptionTypes();
    boolean declaresThrowable = false;
    boolean declaresException = false;
    boolean declaresRuntimeException = false;
    boolean declaresRemoteException = false;
    boolean declaresRemoteInvocationException = false;
    boolean adaptThrowable = false;
    Class adaptType = null;
    for (int i = 0; i < exceptionTypes.length; ++i) {
        Class exceptionType = exceptionTypes[i];
        if (exceptionType.equals(Throwable.class)) {
            declaresThrowable = true;
        } else if (exceptionType.equals(Exception.class)) {
            declaresException = true;
        } else if (exceptionType.equals(RuntimeException.class)) {
            declaresRuntimeException = true;
        } else if (exceptionType.equals(RemoteException.class)) {
            declaresRemoteException = true;
        } else if (exceptionType.getName().equals(REMOTE_INVOCATION_EXCEPTION)) {
            declaresRemoteInvocationException = true;
        } else if (_adapters.get(exceptionType) != null) {
            adaptType = exceptionType;
        }
    }
    if (!declaresThrowable && adaptType != null) {
        // rethrow all uncaught exceptions as an instance of adaptType 
        adaptThrowable = true;
    }
    // determine the set of exceptions to catch. 
    Class[] catchTypes = method.getExceptionTypes();
    Arrays.sort(catchTypes, new ClassComparator());
    // generate the method signature 
    String returnClass = ClassHelper.getQualifiedName(returnType);
    writer.write("public " + returnClass + " " + method.getName() + "(");
    for (int i = 0; i < argTypes.length; ++i) {
        if (i > 0) {
            writer.write(", ");
        }
        String argClass = ClassHelper.getQualifiedName(argTypes[i]);
        writer.write(argClass + " arg" + i);
    }
    writer.write(")");
    // generate throws clause 
    if (exceptionTypes.length > 0) {
        writer.writelnInc();
        writer.write("throws ");
        for (int i = 0; i < exceptionTypes.length; ++i) {
            if (i > 0) {
                writer.write(", ");
            }
            writer.write(exceptionTypes[i].getName());
        }
        writer.writeln(" { ");
    } else {
        writer.writelnInc(" {");
    }
    // generate the invocation arguments, if the method takes arguments 
    String argValue = null;
    if (argTypes.length > 0) {
        argValue = "args";
        writer.write("Object[] " + argValue + " = new Object[] {");
        for (int i = 0; i < argTypes.length; ++i) {
            if (i > 0) {
                writer.write(", ");
            }
            Class arg = argTypes[i];
            String name = "arg" + i;
            writer.write(wrapArgument(arg, name));
        }
        writer.writeln("};");
    } else {
        argValue = "null";
    }
    // generate the invoke() call 
    boolean hasReturn = (returnType != void.class);
    if (hasReturn) {
        writer.writeln("Object result;");
    }
    writer.writelnInc("try {");
    if (hasReturn) {
        writer.write("result = ");
    }
    long methodId = MethodHelper.getMethodID(method);
    writer.writelnDec("invoke(" + getMethodVarName(method) + ", " + argValue + ", 0x" + Long.toHexString(methodId) + "L);");
    boolean caughtRIE = false;
    boolean rethrowRIE = false;
    if (!declaresThrowable && !declaresException && !declaresRuntimeException && !declaresRemoteInvocationException) {
        rethrowRIE = true;
    }
    for (int i = 0; i < catchTypes.length; ++i) {
        Class catchType = catchTypes[i];
        if (rethrowRIE && !caughtRIE) {
            if (catchType.equals(Throwable.class) || catchType.equals(Exception.class) || catchType.equals(RuntimeException.class)) {
                generateRethrow(writer, REMOTE_INVOCATION_EXCEPTION);
                caughtRIE = true;
            }
        }
        generateRethrow(writer, catchType.getName());
    }
    if (rethrowRIE && !caughtRIE) {
        generateRethrow(writer, REMOTE_INVOCATION_EXCEPTION);
    }
    if (!declaresThrowable) {
        writer.writelnInc("} catch (java.lang.Throwable exception) {");
        if (adaptThrowable) {
            Class adapter = (Class) _adapters.get(adaptType);
            String instance = getAdapterInstanceName(adapter);
            writer.writeln(adaptType.getName() + " error = (" + adaptType.getName() + ") " + instance + ".adapt(exception);");
            writer.writelnDec("throw error;");
        } else if (declaresRemoteException) {
            writer.writelnDec("throw new " + RemoteException.class.getName() + "(exception.getMessage(), exception);");
        } else {
            writer.writelnDec("throw new " + REMOTE_INVOCATION_EXCEPTION + "(exception);");
        }
    }
    writer.writeln("}");
    if (hasReturn) {
        writer.writelnDec("return " + unwrapReturn(method.getReturnType(), "result"));
    } else {
        writer.writelnDec();
    }
    writer.writeln("}");
}
