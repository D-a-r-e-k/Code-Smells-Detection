/**
     * @see org.displaytag.decorator.TableDecorator#finish()
     */
public void finish() {
    if (!checkedIds.isEmpty()) {
        JspWriter writer = getPageContext().getOut();
        for (Iterator it = checkedIds.iterator(); it.hasNext(); ) {
            String name = (String) it.next();
            StringBuffer buffer = new StringBuffer();
            buffer.append("<input type=\"hidden\" name=\"");
            buffer.append(fieldName);
            buffer.append("\" value=\"");
            buffer.append(name);
            buffer.append("\">");
            try {
                writer.write(buffer.toString());
            } catch (IOException e) {
            }
        }
    }
    super.finish();
}
