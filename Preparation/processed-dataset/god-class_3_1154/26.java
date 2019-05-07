public static void resolveAndInvokePropertyMethods(Object obj, Map<String, String> props) throws Exception {
    Method[] methods = obj.getClass().getMethods();
    for (Method method : methods) {
        resolveAndInvokePropertyMethod(obj, method, props);
    }
}
