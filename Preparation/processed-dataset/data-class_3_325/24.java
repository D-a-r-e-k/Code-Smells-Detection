/**
     *  DFS of dependency graph formed by Property annotations and dependsUpon parameter
     *  This is used to create a list of Properties in dependency order
     */
static void addPropertyToDependencyList(List<AccessibleObject> orderedList, Map<String, AccessibleObject> props, Stack<AccessibleObject> stack, AccessibleObject obj) {
    if (orderedList.contains(obj))
        return;
    if (stack.search(obj) > 0) {
        throw new RuntimeException("Deadlock in @Property dependency processing");
    }
    // record the fact that we are processing obj 
    stack.push(obj);
    // process dependencies for this object before adding it to the list 
    Property annotation = obj.getAnnotation(Property.class);
    String dependsClause = annotation.dependsUpon();
    StringTokenizer st = new StringTokenizer(dependsClause, ",");
    while (st.hasMoreTokens()) {
        String token = st.nextToken().trim();
        AccessibleObject dep = props.get(token);
        // if null, throw exception  
        addPropertyToDependencyList(orderedList, props, stack, dep);
    }
    // indicate we're done with processing dependencies 
    stack.pop();
    // we can now add in dependency order 
    orderedList.add(obj);
}
