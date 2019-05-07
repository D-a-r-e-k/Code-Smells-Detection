/**
     * Returns the output format for this serializer.
     *
     * @return The output format in use
     */
public Properties getOutputFormat() {
    Properties def = new Properties();
    {
        Set s = getOutputPropDefaultKeys();
        Iterator i = s.iterator();
        while (i.hasNext()) {
            String key = (String) i.next();
            String val = getOutputPropertyDefault(key);
            def.put(key, val);
        }
    }
    Properties props = new Properties(def);
    {
        Set s = getOutputPropKeys();
        Iterator i = s.iterator();
        while (i.hasNext()) {
            String key = (String) i.next();
            String val = getOutputPropertyNonDefault(key);
            if (val != null)
                props.put(key, val);
        }
    }
    return props;
}
