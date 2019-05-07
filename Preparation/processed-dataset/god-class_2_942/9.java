/**
    *
    * Define the basic types and add them to the model if they don't already exist
    * We assume the types are in the java.lang subpackage...
    *
    * @param simpleModel the uml model to create
    * @return HashMap with a mapping of the different types.
    */
public HashMap createBasicTypes(SimpleModel simpleModel) {
    if (typeMappings != null) {
        return typeMappings;
    }
    typeMappings = new HashMap();
    // First create the required uml packages.  
    javaLangPackage = simpleModel.addSimpleUmlPackage("java.lang");
    javaUtilPackage = simpleModel.addSimpleUmlPackage("java.util");
    javaSqlPackage = simpleModel.addSimpleUmlPackage("java.sql");
    javaMathPackage = simpleModel.addSimpleUmlPackage("java.math");
    // Create the default types:  
    HashMap basicTypes = new HashMap();
    basicTypes.put(stringType, stringValue);
    basicTypes.put(intType, intValue);
    basicTypes.put(byteType, byteValue);
    basicTypes.put(shortType, shortValue);
    basicTypes.put(floatType, floatValue);
    basicTypes.put(doubleType, doubleValue);
    basicTypes.put(longType, longValue);
    Set basicTypesSet = basicTypes.keySet();
    for (Iterator iterator = basicTypesSet.iterator(); iterator.hasNext(); ) {
        String key = (String) iterator.next();
        SimpleUmlClass simpleUmlClass = new SimpleUmlClass((String) basicTypes.get(key), SimpleModelElement.PUBLIC);
        javaLangPackage.addSimpleClassifier(simpleUmlClass);
        typeMappings.put(key, simpleUmlClass);
    }
    basicTypes.put(bigDecimalType, bigDecimalValue);
    basicTypes.put(sqlDateType, sqlDateValue);
    basicTypes.put(sqlTimestampType, sqlTimestampValue);
    basicTypes.put(sqlUtilType, sqlUtilValue);
    // Make string the default type.  
    typeMappings.put(defaultType, typeMappings.get(stringType));
    // Add bigdecimals  
    SimpleUmlClass simpleUmlClass = new SimpleUmlClass((String) basicTypes.get(bigDecimalType), SimpleModelElement.PUBLIC);
    javaMathPackage.addSimpleClassifier(simpleUmlClass);
    typeMappings.put(bigDecimalType, simpleUmlClass);
    simpleUmlClass = new SimpleUmlClass((String) basicTypes.get(sqlDateType), SimpleModelElement.PUBLIC);
    javaSqlPackage.addSimpleClassifier(simpleUmlClass);
    typeMappings.put(sqlDateType, simpleUmlClass);
    simpleUmlClass = new SimpleUmlClass((String) basicTypes.get(sqlTimestampType), SimpleModelElement.PUBLIC);
    javaSqlPackage.addSimpleClassifier(simpleUmlClass);
    typeMappings.put(sqlTimestampType, simpleUmlClass);
    simpleUmlClass = new SimpleUmlClass((String) basicTypes.get(sqlUtilType), SimpleModelElement.PUBLIC);
    javaUtilPackage.addSimpleClassifier(simpleUmlClass);
    typeMappings.put(sqlUtilType, simpleUmlClass);
    return typeMappings;
}
