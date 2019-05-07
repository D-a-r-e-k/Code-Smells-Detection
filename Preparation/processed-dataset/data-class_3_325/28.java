public static void resolveAndAssignFields(Object obj, Map<String, String> props) throws Exception {
    //traverse class hierarchy and find all annotated fields 
    for (Class<?> clazz = obj.getClass(); clazz != null; clazz = clazz.getSuperclass()) {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            resolveAndAssignField(obj, field, props);
        }
    }
}
