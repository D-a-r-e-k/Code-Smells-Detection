/**
     * Returns the category with the specified name. If it doesn't exist a new
     * category will be created.
     */
private SimpleCategory getCategory(String categoryName, Session session) {
    SimpleCategory category = searchCategory(categoryName, session.getCategories().getRootNode());
    if (category == null) {
        category = new SimpleCategory(categoryName);
        session.getCategories().insertNodeInto(category.getCategoryNode(), session.getCategories().getRootNode(), 0);
    }
    return category;
}
