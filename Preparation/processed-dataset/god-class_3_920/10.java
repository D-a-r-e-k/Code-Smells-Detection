public AttributeList getAttributes(String[] attributes) //throws Exception 
{
    AttributeList toRet = null;
    if (attributes == null)
        return toRet;
    toRet = new AttributeList();
    for (int i = 0; i < attributes.length; i++) {
        Attribute attr = null;
        try {
            attr = new Attribute(attributes[i], getAttribute(attributes[i]));
        } catch (Exception e) {
            attr = new Attribute(attributes[i], e);
        }
        toRet.add(attr);
    }
    return toRet;
}
