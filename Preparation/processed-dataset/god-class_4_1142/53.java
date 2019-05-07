private boolean canAddComponents(Vector components) {
    final int size = components.size();
    final XSDDescription desc = new XSDDescription();
    for (int i = 0; i < size; i++) {
        XSObject component = (XSObject) components.elementAt(i);
        if (!canAddComponent(component, desc)) {
            return false;
        }
    }
    return true;
}
