/**
     * Searches a category and returns null if it doesn't exist.
     */
private SimpleCategory searchCategory(String name, CategoryNode root) {
    for (Enumeration e = root.children(); e.hasMoreElements(); ) {
        CategoryNode node = (CategoryNode) e.nextElement();
        Object obj = node.getUserObject();
        if (obj instanceof SimpleCategory) {
            SimpleCategory category = (SimpleCategory) obj;
            if (category.getCategoryName().equals(name))
                return category;
        }
    }
    return null;
}
