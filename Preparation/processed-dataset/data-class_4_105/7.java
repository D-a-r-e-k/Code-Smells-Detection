private String getTaggedValue(SimpleModel model, String taggedValueAttributeSqlType, SimpleModelElement att, String defaultValue) {
    String value = model.getTaggedValue(taggedValueAttributeSqlType, att);
    return value == null ? defaultValue : value;
}
