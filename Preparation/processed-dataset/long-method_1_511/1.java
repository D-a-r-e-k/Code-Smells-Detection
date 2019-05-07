protected Class<?> findClass(String name) throws ClassNotFoundException {
    try {
        return super.findClass(name);
    } catch (ClassNotFoundException e) {
        throw decorateException(name, e);
    }
}
