/**
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided
 * that the following conditions are met:
 *
 * 1. Redistributions of source code must retain copyright
 *    statements and notices.  Redistributions must also contain a
 *    copy of this document.
 *
 * 2. Redistributions in binary form must reproduce the
 *    above copyright notice, this list of conditions and the
 *    following disclaimer in the documentation and/or other
 *    materials provided with the distribution.
 *
 * 3. The name "Exolab" must not be used to endorse or promote
 *    products derived from this Software without prior written
 *    permission of Exoffice Technologies.  For written permission,
 *    please contact info@exolab.org.
 *
 * 4. Products derived from this Software may not be called "Exolab"
 *    nor may "Exolab" appear in their names without prior written
 *    permission of Exoffice Technologies. Exolab is a registered
 *    trademark of Exoffice Technologies.
 *
 * 5. Due credit should be given to the Exolab Project
 *    (http://www.exolab.org/).
 *
 * THIS SOFTWARE IS PROVIDED BY EXOFFICE TECHNOLOGIES AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 * EXOFFICE TECHNOLOGIES OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright 2003-2005 (C) Exoffice Technologies Inc. All Rights Reserved.
 *
 * $Id: ProxyGenerator.java,v 1.6 2005/05/24 13:38:20 tanderson Exp $
 */
package org.exolab.jms.plugins.proxygen;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Method;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;


/**
 * Generates source code for a <code>Proxy</code> implementation of a class.
 *
 * @author <a href="mailto:tma@netspace.net.au">Tim Anderson</a>
 * @version $Revision: 1.6 $ $Date: 2005/05/24 13:38:20 $
 */
public class ProxyGenerator {

    /**
     * The class to generate a proxy source for.
     */
    private final Class _clazz;

    /**
     * The package.
     */
    private final String _package;

    /**
     * The class name.
     */
    private final String _className;

    /**
     * A map of adapters of {@link Throwable}. The key is the target
     * exception type, the value the adapter class. The adapter class must
     * implement <code>ThrowableAdapter</code>. It is accessed via
     * introspection to avoid circular build dependencies.
     */
    private HashMap _adapters = new HashMap();

    /**
     * Interfaces implemented by the class.
     */
    private Class[] _interfaces;

    /**
     * The methods implemented by the class.
     */
    private Method[] _methods;

    /**
     * Primitive mappings.
     */
    private static final Class[][] MAPPINGS = new Class[][]{
        {boolean.class, Boolean.class},
        {byte.class, Byte.class},
        {short.class, Short.class},
        {char.class, Character.class},
        {int.class, Integer.class},
        {long.class, Long.class},
        {float.class, Float.class},
        {double.class, Double.class}};

    /**
     * The fully qualified Delegate class name.
     */
    private static final String DELEGATE = "org.exolab.jms.net.proxy.Delegate";

    /**
     * The fully qualified Proxy class name.
     */
    private static final String PROXY = "org.exolab.jms.net.proxy.Proxy";

    /**
     * The suffix for generated proxies.
     */
    private static final String PROXY_SUFFIX = "__Proxy";

    /**
     * The fully qualified RemoteInvocationException class name.
     */
    private static final String REMOTE_INVOCATION_EXCEPTION =
            "org.exolab.jms.net.proxy.RemoteInvocationException";

    /**
     * The fully qualified ThrowableAdapter class name.
     */
    private static final String THROWABLE_ADAPTER =
            "org.exolab.jms.net.proxy.ThrowableAdapter";


    /**
     * Construct a new <code>ProxyGenerator</code>.
     *
     * @param clazz   the class to generate proxy code for
     * @param adapters adapter classes for {@link RemoteException}. May be
     *                <code>null</code>
     * @throws Exception if <code>adapter</code> is specified but can't be
     *                   instantiated
     */
    public ProxyGenerator(Class clazz, Class[] adapters)
            throws Exception {
        if (clazz == null) {
            throw new IllegalArgumentException("Argument 'clazz' is null");
        }
        if (clazz.isArray()) {
            throw new IllegalArgumentException(
                    "Can't generate proxies for array types");
        }
        if (clazz.isPrimitive()) {
            throw new IllegalArgumentException(
                    "Can't generate proxies for primitive types");
        }
        _clazz = clazz;
        _package = ClassHelper.getPackage(_clazz);

        String name;
        if (_package != null) {
            name = _clazz.getName().substring(_package.length() + 1);
        } else {
            name = _clazz.getName();
        }
        _className = name + PROXY_SUFFIX;

        _interfaces = _clazz.getInterfaces();
        if (_interfaces.length == 0) {
            if (MethodHelper.getAllInterfaces(clazz).length == 0) {
                throw new IllegalArgumentException(
                        "Cannot generate proxy for class " + _clazz.getName()
                        + ": class doesn't implement any interfaces");
            }
        }

        if (adapters != null && adapters.length != 0) {
            _adapters = getAdapters(adapters);
        }

        _methods = MethodHelper.getInterfaceMethods(_clazz);
        Arrays.sort(_methods, new MethodComparator());
    }

    /**
     * Generates the code for the proxy implementation.
     *
     * @param stream the stream to write to
     * @throws IOException for any I/O error
     */
    public void generate(OutputStream stream) throws IOException {
        SourceWriter writer = new SourceWriter(new OutputStreamWriter(stream));

        if (_package != null) {
            writer.writeln("package " + _package + ";");
        }

        writer.writelnInc("public class " + _className);
        writer.writeln("extends " + getSuperclassProxy(_clazz));
        if (_interfaces.length != 0) {
            writer.write("implements ");
            for (int i = 0; i < _interfaces.length; ++i) {
                if (i > 0) {
                    writer.write(", ");
                }
                writer.write(_interfaces[i].getName());
            }
        }
        writer.writeln(" {");
        generateStaticDeclarations(writer);
        generateConstructor(writer);
        generateMethods(writer);
        generateStaticInitialiser(writer);
        writer.writelnDec();
        writer.writeln("}");
        writer.flush();
    }

    /**
     * Generates static declarations.
     *
     * @param writer the writer to write to
     * @throws IOException for any I/O error
     */
    protected void generateStaticDeclarations(SourceWriter writer)
            throws IOException {

        if (_methods.length > 0) {
            writer.writeln();
            for (int i = 0; i < _methods.length; ++i) {
                Method method = _methods[i];
                String name = getMethodVarName(method);
                writer.writeln("private static final java.lang.reflect.Method "
                               + name
                               + ";");
            }
            writer.writeln();

            Iterator iterator = _adapters.values().iterator();
            while (iterator.hasNext()) {
                Class adapter = (Class) iterator.next();
                String name = getAdapterInstanceName(adapter);
                writer.writeln("private static final "
                               + THROWABLE_ADAPTER + " " + name
                               + " = new " + adapter.getName()+ "();");
            }
            writer.writeln();
        }
    }

    /**
     * Generates the constructor.
     *
     * @param writer the writer to write to
     * @throws IOException for any I/O error
     */
    protected void generateConstructor(SourceWriter writer)
            throws IOException {

        writer.writelnInc("public " + _className + "(" + DELEGATE
                          + " delegate) {");
        writer.writelnDec("super(delegate);");
        writer.writeln("}");
    }

    /**
     * Generates the public methods.
     *
     * @param writer the writer to write to
     * @throws IOException for any I/O error
     */
    protected void generateMethods(SourceWriter writer) throws IOException {
        for (int i = 0; i < _methods.length; ++i) {
            writer.writeln();
            Method method = _methods[i];
            generateMethod(method, writer);
        }
    }

    /**
     * Generates a method.
     *
     * @param method the method to generate code for
     * @param writer the writer to write to
     * @throws IOException for any I/O error
     */
    protected void generateMethod(Method method, SourceWriter writer)
            throws IOException {

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
            } else if (exceptionType.getName().equals(
                    REMOTE_INVOCATION_EXCEPTION)) {
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
        writer.writelnDec("invoke(" + getMethodVarName(method) + ", "
                          + argValue + ", 0x" + Long.toHexString(methodId)
                          + "L);");

        boolean caughtRIE = false;
        boolean rethrowRIE = false;
        if (!declaresThrowable && !declaresException
                && !declaresRuntimeException
                && !declaresRemoteInvocationException) {
            rethrowRIE = true;
        }

        for (int i = 0; i < catchTypes.length; ++i) {
            Class catchType = catchTypes[i];
            if (rethrowRIE && !caughtRIE) {
                if (catchType.equals(Throwable.class)
                    || catchType.equals(Exception.class)
                    || catchType.equals(RuntimeException.class)) {
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
                writer.writeln(adaptType.getName() + " error = ("
                        + adaptType.getName() + ") " + instance
                               + ".adapt(exception);");
                writer.writelnDec("throw error;");
            } else if (declaresRemoteException) {
                writer.writelnDec("throw new "
                                  + RemoteException.class.getName()
                                  + "(exception.getMessage(), exception);");
            } else {
                writer.writelnDec("throw new " + REMOTE_INVOCATION_EXCEPTION
                                  + "(exception);");
            }
        }
        writer.writeln("}");

        if (hasReturn) {
            writer.writelnDec("return " + unwrapReturn(method.getReturnType(),
                                                       "result"));
        } else {
            writer.writelnDec();
        }
        writer.writeln("}");
    }

    /**
     * Generates the class static initialiser.
     *
     * @param writer the writer to write to
     * @throws IOException for any I/O error
     */
    protected void generateStaticInitialiser(SourceWriter writer)
            throws IOException {

        if (_methods.length > 0) {
            writer.writeln();
            writer.writelnInc("static {");
            writer.writelnInc("try {");
            for (int i = 0; i < _methods.length; ++i) {
                Method method = _methods[i];
                String name = getMethodVarName(method);
                Class clazz = method.getDeclaringClass();
                writer.write(name + " = " + clazz.getName()
                             + ".class.getMethod(\"" + method.getName()
                             + "\", " + "new Class[] {");
                Class[] args = method.getParameterTypes();
                for (int j = 0; j < args.length; ++j) {
                    if (j > 0) {
                        writer.write(", ");
                    }
                    writer.write(ClassHelper.getQualifiedName(args[j]) + ".class");
                }
                writer.writeln("});");
            }
            writer.decIndent();
            writer.writelnInc("} catch (NoSuchMethodException exception) {");
            writer.writelnDec(
                    "throw new NoSuchMethodError(exception.getMessage());");
            writer.writelnDec("}");
            writer.writeln("}");
        }
    }

    /**
     * Generates a catch/rethrow clause.
     *
     * @param writer the writer to write to
     * @param name   the type name catch
     * @throws IOException for any I/O error
     */
    protected void generateRethrow(SourceWriter writer, String name)
        throws IOException {
        writer.writelnInc("} catch (" + name + " exception) {");
        writer.writelnDec("throw exception;");
    }

    /**
     * Wraps primitive arguments into their objectified equivalents.
     *
     * @param clazz the argument class type
     * @param name  the argument name
     * @return the wrapped argument name, or <code>name</code> if
     *         <code>clazz</code> isn't a primitive type
     */
    protected String wrapArgument(Class clazz, String name) {
        String result;
        if (clazz.isPrimitive()) {
            Class wrapper = null;
            for (int i = 0; i < MAPPINGS.length; ++i) {
                if (MAPPINGS[i][0] == clazz) {
                    wrapper = MAPPINGS[i][1];
                }
            }
            result = "new " + ClassHelper.getQualifiedName(wrapper)
                    + "(" + name + ")";
        } else {
            result = name;
        }
        return result;
    }

    /**
     * Generates code to unwrap a return type If the return class type is a
     * primitve, generates code to unbox the objectified primitve. If the return
     * class type is an object, generates code to cast the variable name to that
     * type.
     *
     * @param clazz the return class type
     * @param name  the variable name
     * @return code to unwrap the return type
     */
    protected String unwrapReturn(Class clazz, String name) {
        String result = null;
        if (clazz.isPrimitive()) {
            Class wrapper = null;
            for (int i = 0; i < MAPPINGS.length; ++i) {
                if (MAPPINGS[i][0] == clazz) {
                    wrapper = MAPPINGS[i][1];
                    break;
                }
            }
            result = "((" + wrapper.getName() + ") " + name + ")."
                    + clazz.getName() + "Value();";
        } else {
            result = "(" + ClassHelper.getQualifiedName(clazz) + ") " + name +
                    ";";
        }
        return result;
    }

    /**
     * Generates a name for a static Method variable.
     *
     * @param method the method
     * @return a name for the variable
     */
    protected String getMethodVarName(Method method) {
        return method.getName().toUpperCase() + "_"
                + Long.toHexString(MethodHelper.getMethodID(method));
    }

    /**
     * Generates a unique instance name for an adapter.

     * @param adapter the adapter class
     * @return a unique instance name
     */
    protected String getAdapterInstanceName(Class adapter) {
        // determine the class name, minus its package
        String name;
        String qualifiedName = adapter.getName();
        int lastDot = qualifiedName.lastIndexOf(".");
        if (lastDot != -1) {
            name = qualifiedName.substring(lastDot + 1);
        } else {
            name = qualifiedName;
        }
        StringBuffer result = new StringBuffer(name.toUpperCase());
        result.append("_");
        result.append(Long.toHexString(qualifiedName.hashCode()));
        return result.toString();
    }

    /**
     * Returns a set of <code>ThrowableAdapter</code>s.
     */
    private HashMap getAdapters(Class[] adapterClasses)
            throws Exception {
        HashMap result = new HashMap();

        for (int i = 0; i < adapterClasses.length; ++i) {
            Class adapterClass = adapterClasses[i];
            Object adapter = adapterClass.newInstance();
            Method method = adapterClass.getMethod("getTarget", new Class[0]);
            Class exceptionType = (Class) method.invoke(adapter, new Object[0]);
            if (!Throwable.class.isAssignableFrom(exceptionType)) {
                throw new Exception(
                        "Invalid exception class " + exceptionType.getName()
                        + ": class doesn't extend " + Throwable.class);
            }
            result.put(exceptionType, adapterClass);
        }
        return result;
    }

    /**
     * Returns the proxy superclass name for a given class.
     *
     * @param clazz the class
     * @return the proxy superclass name
     */
    private static String getSuperclassProxy(Class clazz) {
        String name = PROXY;
        Class superClass = clazz.getSuperclass();
        if (superClass != null) {
            if (superClass.getInterfaces().length != 0) {
                name = superClass.getName() + PROXY_SUFFIX;
            } else {
                name = getSuperclassProxy(superClass);
            }
        }
        return name;
    }

    /**
     * Helper class to compare two classes.
     */
    private static class ClassComparator implements Comparator {

        /**
         * Compare two classes.
         *
         * @param o1 the first class
         * @param o2 the second class
         * @return a negative integer, zero, or a positive integer if the first
         *         class is less than, equal to, or greater than the second.
         */
        public int compare(Object o1, Object o2) {
            int result;
            Class c1 = (Class) o1;
            Class c2 = (Class) o2;
            if (c1 == c2) {
                result = 0;
            } else if (c1.isAssignableFrom(c2)) {
                result = 1;
            } else {
                result = -1;
            }
            return result;
        }

    }

    /**
     * Helper to compare two methods on their names.
     */
    private static class MethodComparator implements Comparator {

        /**
         * Compare two methods.
         *
         * @param o1 the first method
         * @param o2 the second method
         * @return a negative integer, zero, or a positive integer if the first
         *         method is less than, equal to, or greater than the second.
         */
        public int compare(Object o1, Object o2) {
            Method m1 = (Method) o1;
            Method m2 = (Method) o2;
            return m1.getName().compareTo(m2.getName());
        }

    }

}
