/**
     * @see org.displaytag.decorator.Decorator#init(javax.servlet.jsp.PageContext, java.lang.Object,
     * org.displaytag.model.TableModel)
     */
public void init(PageContext pageContext, Object decorated, TableModel tableModel) {
    super.init(pageContext, decorated, tableModel);
    String[] params = pageContext.getRequest().getParameterValues(fieldName);
    checkedIds = params != null ? new ArrayList(Arrays.asList(params)) : new ArrayList(0);
}
