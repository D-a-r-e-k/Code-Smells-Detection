public AttributeList setAttributes(AttributeList arg0) {
    AttributeList result = new AttributeList();
    for (Iterator i = arg0.iterator(); i.hasNext(); ) {
        Attribute attr = (Attribute) i.next();
        // 
        //  Attempt to set the attribute.  If it succeeds (no exception), 
        //  then we just add it to the list of successfull sets. 
        // 
        try {
            setAttribute(attr);
            result.add(attr);
        } catch (AttributeNotFoundException e) {
            // TODO Auto-generated catch block 
            e.printStackTrace();
        } catch (InvalidAttributeValueException e) {
            // TODO Auto-generated catch block 
            e.printStackTrace();
        } catch (MBeanException e) {
            // TODO Auto-generated catch block 
            e.printStackTrace();
        } catch (ReflectionException e) {
            // TODO Auto-generated catch block 
            e.printStackTrace();
        }
    }
    return result;
}
