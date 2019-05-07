void method0() { 
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
private static final Class[][] MAPPINGS = new Class[][] { { boolean.class, Boolean.class }, { byte.class, Byte.class }, { short.class, Short.class }, { char.class, Character.class }, { int.class, Integer.class }, { long.class, Long.class }, { float.class, Float.class }, { double.class, Double.class } };
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
private static final String REMOTE_INVOCATION_EXCEPTION = "org.exolab.jms.net.proxy.RemoteInvocationException";
/**
     * The fully qualified ThrowableAdapter class name.
     */
private static final String THROWABLE_ADAPTER = "org.exolab.jms.net.proxy.ThrowableAdapter";
}
