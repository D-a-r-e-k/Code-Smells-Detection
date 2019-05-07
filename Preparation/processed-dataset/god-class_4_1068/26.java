private String getTaskLabel(Task task, int position) {
    StringBuffer result = new StringBuffer();
    Object property = TaskProperties.getProperty(task, myLabelOptions[position].getValue());
    if (property != null) {
        if (property instanceof Boolean)
            if (((Boolean) property).booleanValue())
                result.append(lang.getText("yes"));
            else
                result.append(lang.getText("no"));
        else
            result.append(property);
    }
    return result.toString();
}
