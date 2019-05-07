/**
     * Returns the subcategory with the specified name. If it doesn't exist a new
     * subcategory will be created.
     */
private SimpleCategory getSubcategory(String name, SimpleCategory category, Session session) {
    SimpleCategory subcategory = searchCategory(name, category.getCategoryNode());
    if (subcategory == null) {
        subcategory = new SimpleCategory(name);
        session.getCategories().insertNodeInto(subcategory.getCategoryNode(), category.getCategoryNode(), 0);
    }
    return subcategory;
}
