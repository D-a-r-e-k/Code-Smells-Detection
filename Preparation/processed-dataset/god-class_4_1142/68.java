private Vector expandComponents(XSObject[] components, Hashtable dependencies) {
    Vector newComponents = new Vector();
    for (int i = 0; i < components.length; i++) {
        if (!newComponents.contains(components[i])) {
            newComponents.add(components[i]);
        }
    }
    for (int i = 0; i < newComponents.size(); i++) {
        final XSObject component = (XSObject) newComponents.elementAt(i);
        expandRelatedComponents(component, newComponents, dependencies);
    }
    return newComponents;
}
