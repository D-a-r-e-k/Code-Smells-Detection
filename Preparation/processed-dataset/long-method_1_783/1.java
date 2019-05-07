public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
    ResponseWriter w = context.getResponseWriter();
    UIStringInput uiInput = (UIStringInput) component;
    String value = uiInput.getValue();
    String type = "";
    String scripting = uiInput.getScripting();
    switch(uiInput.getType()) {
        case UIStringInput.TEXT:
            type = "text";
            break;
        case UIStringInput.PASSWORD:
            type = "password";
    }
    if (value == null)
        value = "";
    // Try to resolve elvalue text 
    if (FacesUtil.isValueReference(value)) {
        value = (String) FacesUtil.resolveBoundValueBinding(context, uiInput, "text");
    }
    w.write("<input name='");
    w.write(uiInput.getName());
    w.write("'");
    if (value == null)
        value = "";
    w.write(" value='");
    w.write(value);
    w.write("'");
    w.write(" type='");
    w.write(type);
    w.write("'");
    if (uiInput.getClazz() != null) {
        w.write(" class='");
        w.write(uiInput.getClazz());
        w.write("'");
    }
    if (uiInput.getTitle() != null) {
        w.write(" title='");
        w.write(uiInput.getTitle());
        w.write("'");
    }
    if (!uiInput.isEditable() || uiInput.isReadonly()) {
        w.write(" readonly='readonly' ");
    }
    if (scripting != null) {
        w.write(" " + scripting);
    }
    w.write("/>");
    if (uiInput.hasError()) {
        w.write("<span style='color: red'>*</span>");
    }
}
