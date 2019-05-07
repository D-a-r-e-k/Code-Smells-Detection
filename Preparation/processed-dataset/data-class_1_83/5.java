public String getCheckbox() {
    String evaluatedId = ObjectUtils.toString(evaluate(id));
    boolean checked = checkedIds.contains(evaluatedId);
    StringBuffer buffer = new StringBuffer();
    buffer.append("<input type=\"checkbox\" name=\"_chk\" value=\"");
    buffer.append(evaluatedId);
    buffer.append("\"");
    if (checked) {
        checkedIds.remove(evaluatedId);
        buffer.append(" checked=\"checked\"");
    }
    buffer.append("/>");
    return buffer.toString();
}
