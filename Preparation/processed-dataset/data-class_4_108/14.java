public static List getObjectsFromTree(Class clazz) {
    ArrayList list = new ArrayList();
    Enumeration children = jagGenerator.root.children();
    while (children.hasMoreElements()) {
        DefaultMutableTreeNode child = (DefaultMutableTreeNode) children.nextElement();
        if (child != null && child.getClass().equals(clazz)) {
            list.add(child);
        }
    }
    return list;
}
