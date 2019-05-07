/*
     * @see org.argouml.uml.ui.UMLModelElementListModel2#isValidElement(Object)
     */
protected boolean isValidElement(Object element) {
    return Model.getFacade().isATagDefinition(element) && Model.getFacade().getTagDefinitions(getTarget()).contains(element);
}
