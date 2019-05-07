/**
     *  Gets multiple attributes at the same time.
     *  
     *  @param arg0 The attribute names to get
     *  @return A list of attributes 
     */
public AttributeList getAttributes(String[] arg0) {
    AttributeList list = new AttributeList();
    for (int i = 0; i < arg0.length; i++) {
        try {
            list.add(new Attribute(arg0[i], getAttribute(arg0[i])));
        } catch (AttributeNotFoundException e) {
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
    return list;
}
