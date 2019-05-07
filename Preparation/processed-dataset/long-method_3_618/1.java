public static List getPhotoDescriptions(String url) {
    List photoDescriptions = new ArrayList();
    PersistentValue persistentValue = PersistentValueManager.loadPersistentValue(InternetSlideshows.class.getName() + "." + url);
    String content = persistentValue == null ? null : persistentValue.getValue();
    if (PersistentValueManager.isAged(persistentValue)) {
        try {
            String page = Tools.getPage(new URL(url));
            if (page != null && page.length() > 0)
                content = page;
        } catch (Exception ex) {
            Tools.logException(InternetSlideshows.class, ex, "Could not cache listing: " + url);
        }
    }
    if (content != null) {
        try {
            SAXReader saxReader = new SAXReader();
            StringReader stringReader = new StringReader(content);
            // Document document = saxReader.read(new 
            // File("d:/galleon/itunes2.rss.xml")); 
            Document document = saxReader.read(stringReader);
            stringReader.close();
            stringReader = null;
            Element root = document.getRootElement();
            // check for errors 
            if (root != null && root.getName().equals("rss")) {
                Element channel = root.element("channel");
                if (channel != null) {
                    for (Iterator i = channel.elementIterator("item"); i.hasNext(); ) {
                        Element item = (Element) i.next();
                        String value = null;
                        String title = null;
                        String link = null;
                        if ((value = Tools.getAttribute(item, "title")) != null) {
                            title = value;
                        }
                        if ((value = Tools.getAttribute(item, "description")) != null) {
                            title = Tools.cleanHTML(value);
                        }
                        Element contentElement = item.element("content");
                        if (contentElement != null) {
                            if ((value = contentElement.attributeValue("url")) != null) {
                                link = value;
                                if (url.startsWith("http://rss.news.yahoo.com")) {
                                    URL location = new URL(link);
                                    photoDescriptions.add(new PhotoDescription(location.getProtocol() + "://" + location.getHost() + location.getPath(), title));
                                } else
                                    photoDescriptions.add(new PhotoDescription(link, title));
                            }
                        }
                        Element enclosureElement = item.element("enclosure");
                        if (enclosureElement != null) {
                            if ((value = enclosureElement.attributeValue("url")) != null) {
                                link = value;
                                if (url.startsWith("http://rss.news.yahoo.com")) {
                                    URL location = new URL(link);
                                    photoDescriptions.add(new PhotoDescription(location.getProtocol() + "://" + location.getHost() + location.getPath(), title));
                                } else
                                    photoDescriptions.add(new PhotoDescription(link, title));
                            }
                        }
                    }
                }
            }
            document.clearContent();
            document = null;
            if (PersistentValueManager.isAged(persistentValue)) {
                PersistentValueManager.savePersistentValue(InternetSlideshows.class.getName() + "." + url, content, 60);
            }
        } catch (Exception ex) {
            Tools.logException(InternetSlideshows.class, ex, "Could not download listing: " + url);
            return null;
        }
    }
    return photoDescriptions;
}
