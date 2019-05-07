public void decode(FacesContext context, UIComponent component) {
    Map paramMap = context.getExternalContext().getRequestParameterMap();
    String action = (String) paramMap.get(FacesConstants.ACTION);
    if ("selectTab".equals(action)) {
        String tabId = (String) paramMap.get("tabId");
        UIExoComponent uiComponent = (UIExoComponent) component;
        UIExoComponent target = uiComponent.findComponentById(tabId);
        if (target != null) {
            UINode uiNode = (UINode) target.getParent();
            uiNode.setRenderedComponent(tabId);
            context.renderResponse();
        }
    }
}
