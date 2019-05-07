public AttributeList setAttributes(AttributeList attributes) {
    if (attributes == null)
        throw new RuntimeOperationsException(new IllegalArgumentException("Null values not possible"));
    Object[] array = attributes.toArray();
    if (array == null)
        return attributes;
    for (int i = 0; i < array.length; i++) {
        Attribute attr = null;
        try {
            attr = (Attribute) array[i];
        } catch (ClassCastException ce) {
            continue;
        }
        try {
            setAttribute(attr);
        } catch (Exception e) {
            int index = attributes.indexOf(attr);
            attributes.remove(index);
            attr = new Attribute(attr.getName(), e);
            attributes.add(index, attr);
        }
    }
    return attributes;
}
