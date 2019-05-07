public static void resolveAndInvokePropertyMethod(Object obj, Method method, Map<String, String> props) throws Exception {
    String methodName = method.getName();
    Property annotation = method.getAnnotation(Property.class);
    if (annotation != null && isSetPropertyMethod(method)) {
        String propertyName = PropertyHelper.getPropertyName(method);
        String propertyValue = props.get(propertyName);
        // if there is a systemProperty attribute defined in the annotation, set the property value from the system property 
        String tmp = grabSystemProp(method.getAnnotation(Property.class));
        if (tmp != null)
            propertyValue = tmp;
        if (propertyName != null && propertyValue != null) {
            String deprecated_msg = annotation.deprecatedMessage();
            if (deprecated_msg != null && deprecated_msg.length() > 0) {
                log.warn(method.getDeclaringClass().getSimpleName() + "." + methodName + ": " + deprecated_msg);
            }
        }
        if (propertyValue != null) {
            Object converted = null;
            try {
                converted = PropertyHelper.getConvertedValue(obj, method, props, propertyValue, true);
                method.invoke(obj, converted);
            } catch (Exception e) {
                String name = obj instanceof Protocol ? ((Protocol) obj).getName() : obj.getClass().getName();
                throw new Exception("Could not assign property " + propertyName + " in " + name + ", method is " + methodName + ", converted value is " + converted, e);
            }
        }
        props.remove(propertyName);
    }
}
