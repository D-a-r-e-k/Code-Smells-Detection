/*
     * @see org.argouml.uml.ui.UMLModelElementListModel2#buildModelList()
     */
protected void buildModelList() {
    if (getTarget() != null) {
        setAllElements(Model.getFacade().getTagDefinitions(getTarget()));
    }
}
