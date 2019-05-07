/**
     *  Figure out which image suffixes should be inlined.
     *  @return Collection of Strings with patterns.
     *  
     *  @param engine The WikiEngine from which the patterns are read.
     */
// FIXME: Does not belong here; should be elsewhere 
public static Collection getImagePatterns(WikiEngine engine) {
    Properties props = engine.getWikiProperties();
    ArrayList<String> ptrnlist = new ArrayList<String>();
    for (Enumeration e = props.propertyNames(); e.hasMoreElements(); ) {
        String name = (String) e.nextElement();
        if (name.startsWith(PROP_INLINEIMAGEPTRN)) {
            String ptrn = TextUtil.getStringProperty(props, name, null);
            ptrnlist.add(ptrn);
        }
    }
    if (ptrnlist.size() == 0) {
        ptrnlist.add(DEFAULT_INLINEPATTERN);
    }
    return ptrnlist;
}
