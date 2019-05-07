static List<AccessibleObject> orderFieldsAndMethodsByDependency(List<AccessibleObject> unorderedList, Map<String, AccessibleObject> propertiesMap) {
    // Stack to detect cycle in depends relation 
    Stack<AccessibleObject> stack = new Stack<AccessibleObject>();
    // the result list 
    List<AccessibleObject> orderedList = new LinkedList<AccessibleObject>();
    // add the elements from the unordered list to the ordered list 
    // any dependencies will be checked and added first, in recursive manner 
    for (int i = 0; i < unorderedList.size(); i++) {
        AccessibleObject obj = unorderedList.get(i);
        addPropertyToDependencyList(orderedList, propertiesMap, stack, obj);
    }
    return orderedList;
}
