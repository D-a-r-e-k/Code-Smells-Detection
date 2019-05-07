public static void removeDeprecatedProperties(Object obj, Map<String, String> props) throws Exception {
    //traverse class hierarchy and find all deprecated properties 
    for (Class<?> clazz = obj.getClass(); clazz != null; clazz = clazz.getSuperclass()) {
        if (clazz.isAnnotationPresent(DeprecatedProperty.class)) {
            DeprecatedProperty declaredAnnotation = clazz.getAnnotation(DeprecatedProperty.class);
            String[] deprecatedProperties = declaredAnnotation.names();
            for (String propertyName : deprecatedProperties) {
                String propertyValue = props.get(propertyName);
                if (propertyValue != null) {
                    if (log.isWarnEnabled()) {
                        String name = obj instanceof Protocol ? ((Protocol) obj).getName() : obj.getClass().getName();
                        log.warn(name + " property " + propertyName + " was deprecated and is ignored");
                    }
                    props.remove(propertyName);
                }
            }
        }
    }
}
