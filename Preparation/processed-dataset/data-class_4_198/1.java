public static String getIMDBID(String key) {
    String imdb = null;
    if (key != null) {
        try {
            Parser parser = new Parser("http://us.imdb.com/Tsearch?title=" + URLEncoder.encode(key));
            NodeFilter filter = null;
            NodeList list = list = new NodeList();
            filter = new TagNameFilter("B");
            list = parser.extractAllNodesThatMatch(filter);
            if (list != null && list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    Tag tag = (Tag) list.elementAt(i);
                    CompositeTag parent = (CompositeTag) tag.getParent();
                    int position = parent.findPositionOf(tag);
                    Node value = parent.childAt(position + 1);
                    if (value != null) {
                        if (cleanSpaces(value.getText()).startsWith("populartitles")) {
                            //Popular Titles 
                            while (position < parent.getChildCount()) {
                                value = parent.childAt(++position);
                                if (value.getText().equals("ol")) {
                                    filter = new NodeClassFilter(LinkTag.class);
                                    NodeList linkList = new NodeList();
                                    value.collectInto(linkList, filter);
                                    for (int j = 0; j < linkList.size(); j++) {
                                        LinkTag linkTag = (LinkTag) linkList.elementAt(j);
                                        String REGEX = "/.*/tt(.*)/";
                                        Pattern p = Pattern.compile(REGEX);
                                        Matcher m = p.matcher(linkTag.getLink());
                                        if (m.find()) {
                                            return m.group(1);
                                        }
                                    }
                                    break;
                                }
                            }
                        } else if (cleanSpaces(value.getText()).startsWith("titles(exactmatches)")) {
                            //Titles (Exact Matches) 
                            while (position < parent.getChildCount()) {
                                value = parent.childAt(++position);
                                if (value.getText().equals("ol")) {
                                    filter = new NodeClassFilter(LinkTag.class);
                                    NodeList linkList = new NodeList();
                                    value.collectInto(linkList, filter);
                                    for (int j = 0; j < linkList.size(); j++) {
                                        LinkTag linkTag = (LinkTag) linkList.elementAt(j);
                                        String REGEX = "/.*/tt(.*)/";
                                        Pattern p = Pattern.compile(REGEX);
                                        Matcher m = p.matcher(linkTag.getLink());
                                        if (m.find()) {
                                            return m.group(1);
                                        }
                                    }
                                    break;
                                }
                            }
                        } else if (cleanSpaces(value.getText()).startsWith("titles(partialmatches)")) {
                            //Titles (Partial Matches) 
                            while (position < parent.getChildCount()) {
                                value = parent.childAt(++position);
                                if (value.getText().equals("ol")) {
                                    filter = new NodeClassFilter(LinkTag.class);
                                    NodeList linkList = new NodeList();
                                    value.collectInto(linkList, filter);
                                    for (int j = 0; j < linkList.size(); j++) {
                                        LinkTag linkTag = (LinkTag) linkList.elementAt(j);
                                        String REGEX = "/.*/tt(.*)/";
                                        Pattern p = Pattern.compile(REGEX);
                                        Matcher m = p.matcher(linkTag.getLink());
                                        if (m.find()) {
                                            return m.group(1);
                                        }
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            if (imdb == null) {
                parser.reset();
                filter = new NodeClassFilter(LinkTag.class);
                list = parser.extractAllNodesThatMatch(filter);
                for (int i = 0; i < list.size(); i++) {
                    LinkTag linkTag = (LinkTag) list.elementAt(i);
                    String REGEX = ".*/title/tt(.*)/";
                    Pattern p = Pattern.compile(REGEX);
                    Matcher m = p.matcher(linkTag.getLink());
                    if (m.find()) {
                        return m.group(1);
                    }
                }
            }
        } catch (Exception ex) {
            log.debug("Could not get IMDB ID1: " + key);
            return getIMDBID2(key);
        }
    }
    return imdb;
}
