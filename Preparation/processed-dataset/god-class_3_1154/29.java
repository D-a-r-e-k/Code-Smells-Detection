public static void resolveAndAssignField(Object obj, Field field, Map<String, String> props) throws Exception {
    Property annotation = field.getAnnotation(Property.class);
    if (annotation != null) {
        String propertyName = PropertyHelper.getPropertyName(field, props);
        String propertyValue = props.get(propertyName);
        // if there is a systemProperty attribute defined in the annotation, set the property value from the system property 
        String tmp = grabSystemProp(field.getAnnotation(Property.class));
        if (tmp != null)
            propertyValue = tmp;
        if (propertyName != null && propertyValue != null) {
            String deprecated_msg = annotation.deprecatedMessage();
            if (deprecated_msg != null && deprecated_msg.length() > 0) {
                log.warn(field.getDeclaringClass().getSimpleName() + "." + field.getName() + ": " + deprecated_msg);
            }
        }
        if (propertyValue != null || !PropertyHelper.usesDefaultConverter(field)) {
            Object converted = null;
            try {
                converted = PropertyHelper.getConvertedValue(obj, field, props, propertyValue, true);
                if (converted != null)
                    setField(field, obj, converted);
            } catch (Exception e) {
                String name = obj instanceof Protocol ? ((Protocol) obj).getName() : obj.getClass().getName();
                throw new Exception("Property assignment of " + propertyName + " in " + name + " with original property value " + propertyValue + " and converted to " + converted + " could not be assigned", e);
            }
        }
        props.remove(propertyName);
    }
}
