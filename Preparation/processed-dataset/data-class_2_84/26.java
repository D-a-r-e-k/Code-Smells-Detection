public String getTotalsTdOpen(HeaderCell header, String totalClass) {
    String cssClass = ObjectUtils.toString(header.getHtmlAttributes().get("class"));
    StringBuffer buffer = new StringBuffer();
    buffer.append(TagConstants.TAG_OPEN);
    buffer.append(TagConstants.TAGNAME_COLUMN);
    if (cssClass != null || totalClass != null) {
        buffer.append(" class=\"");
        if (cssClass != null) {
            buffer.append(cssClass);
            if (totalClass != null) {
                buffer.append(" ");
            }
        }
        if (totalClass != null) {
            buffer.append(totalClass);
        }
        buffer.append("\"");
    }
    buffer.append(TagConstants.TAG_CLOSE);
    return buffer.toString();
}
