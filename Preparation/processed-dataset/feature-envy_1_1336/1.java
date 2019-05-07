public Class<?> loadClass(String name) throws ClassNotFoundException {
    try {
        return super.loadClass(name);
    } catch (ClassNotFoundException e) {
        throw decorateException(name, e);
    }
}
