public static Element getDocument(String location) {
    try {
        Document document = null;
        PersistentValue persistentValue = PersistentValueManager.loadPersistentValue(Videocasting.class.getName() + "." + location);
        String content = persistentValue == null ? null : persistentValue.getValue();
        // log.debug("content1="+content); 
        if (PersistentValueManager.isAged(persistentValue) && !location.equals("local")) {
            try {
                String page = Tools.getPage(new URL(location));
                if (page != null && page.length() > 0) {
                    content = page;
                }
            } catch (Exception ex) {
                Tools.logException(Videocasting.class, ex, "Could not cache document: " + location);
            }
        } else {
            if (location.equals("local"))
                content = "";
        }
        // log.debug("content2="+content); 
        if (content != null) {
            SAXReader saxReader = new SAXReader();
            try {
                if (location.equals("local")) {
                    document = saxReader.read(new File("d:/galleon/ivideoder.opml"));
                } else {
                    StringReader stringReader = new StringReader(content);
                    document = saxReader.read(stringReader);
                    if (PersistentValueManager.isAged(persistentValue)) {
                        PersistentValueManager.savePersistentValue(Videocasting.class.getName() + "." + location, content, 60 * 60 * 6);
                    }
                    stringReader.close();
                    stringReader = null;
                }
            } catch (Throwable ex) {
                if (persistentValue != null) {
                    StringReader stringReader = new StringReader(persistentValue.getValue());
                    document = saxReader.read(stringReader);
                    stringReader.close();
                    stringReader = null;
                }
            }
            if (document != null) {
                Element root = document.getRootElement();
                document.clearContent();
                document = null;
                if (root.getName().equals("opml"))
                    return root.element("body");
            }
        }
    } catch (Exception ex) {
        Tools.logException(Videocasting.class, ex, "Could not download document: " + location);
    }
    return null;
}
