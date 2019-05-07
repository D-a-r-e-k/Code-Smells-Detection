/**
     * This method creates a list of all properties (Field or Method) in dependency order, 
     * where dependencies are specified using the dependsUpon specifier of the Property annotation. 
     * In particular, it does the following:
     * (i) creates a master list of properties 
     * (ii) checks that all dependency references are present
     * (iii) creates a copy of the master list in dependency order
     */
static AccessibleObject[] computePropertyDependencies(Object obj, Map<String, String> properties) {
    // List of Fields and Methods of the protocol annotated with @Property 
    List<AccessibleObject> unorderedFieldsAndMethods = new LinkedList<AccessibleObject>();
    List<AccessibleObject> orderedFieldsAndMethods = new LinkedList<AccessibleObject>();
    // Maps property name to property object 
    Map<String, AccessibleObject> propertiesInventory = new HashMap<String, AccessibleObject>();
    // get the methods for this class and add them to the list if annotated with @Property 
    Method[] methods = obj.getClass().getMethods();
    for (int i = 0; i < methods.length; i++) {
        if (methods[i].isAnnotationPresent(Property.class) && isSetPropertyMethod(methods[i])) {
            String propertyName = PropertyHelper.getPropertyName(methods[i]);
            unorderedFieldsAndMethods.add(methods[i]);
            propertiesInventory.put(propertyName, methods[i]);
        }
    }
    //traverse class hierarchy and find all annotated fields and add them to the list if annotated 
    for (Class<?> clazz = obj.getClass(); clazz != null; clazz = clazz.getSuperclass()) {
        Field[] fields = clazz.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            if (fields[i].isAnnotationPresent(Property.class)) {
                String propertyName = PropertyHelper.getPropertyName(fields[i], properties);
                unorderedFieldsAndMethods.add(fields[i]);
                // may need to change this based on name parameter of Property 
                propertiesInventory.put(propertyName, fields[i]);
            }
        }
    }
    // at this stage, we have all Fields and Methods annotated with @Property 
    checkDependencyReferencesPresent(unorderedFieldsAndMethods, propertiesInventory);
    // order the fields and methods by dependency 
    orderedFieldsAndMethods = orderFieldsAndMethodsByDependency(unorderedFieldsAndMethods, propertiesInventory);
    // convert to array of Objects 
    AccessibleObject[] result = new AccessibleObject[orderedFieldsAndMethods.size()];
    for (int i = 0; i < orderedFieldsAndMethods.size(); i++) {
        result[i] = orderedFieldsAndMethods.get(i);
    }
    return result;
}
