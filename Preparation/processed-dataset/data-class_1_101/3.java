private ClassNotFoundException decorateException(String name, ClassNotFoundException e) {
    if (name.startsWith("class ")) {
        return new ClassNotFoundException("Class '" + name + "' is not a classInstance.getName(). " + "It's a classInstance.toString(). The clue is that it starts with 'class ', no classname contains a space.");
    }
    ClassLoader classLoader = this;
    StringBuffer sb = new StringBuffer("'").append(name).append("' classloader stack [");
    while (classLoader != null) {
        sb.append(classLoader.toString()).append("\n");
        final ClassLoader cl = classLoader;
        classLoader = AccessController.doPrivileged(new PrivilegedAction<ClassLoader>() {

            public ClassLoader run() {
                return cl.getParent();
            }
        });
    }
    return new ClassNotFoundException(sb.append("]").toString(), e);
}
