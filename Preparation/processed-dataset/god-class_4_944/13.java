public static Template getTemplate() {
    Enumeration children = jagGenerator.root.children();
    while (children.hasMoreElements()) {
        DefaultMutableTreeNode child = (DefaultMutableTreeNode) children.nextElement();
        if (child instanceof Config) {
            return ((Config) child).getTemplate();
        }
    }
    return null;
}
