private static Method findGetterSetter(Class<?> clazz, String name, Class<?> parm) {
    try {
        Class<?>[] params = { parm };
        Class<?>[] emptyparms = {};
        Method m = clazz.getDeclaredMethod(name, parm != null ? params : emptyparms);
        return m;
    } catch (Exception e) {
    }
    return null;
}
