public static List getTrafficResults(String street, String city, String state, String zip, String radius) {
    List trafficResults = new ArrayList();
    // http://api.local.yahoo.com/MapsService/V1/trafficData?appid=YahooDemo&street=701+First+Street&city=Sunnyvale&state=CA&include_map=1 
    String url = "http://api.local.yahoo.com/MapsService/V1/trafficData?appid=" + "galleonhme" + "&street=" + URLEncoder.encode(street) + "&city=" + URLEncoder.encode(city) + "&state=" + URLEncoder.encode(state) + "&radius=" + URLEncoder.encode(radius) + "&include_map=1&image_width=640&image_height=480";
    log.info(url);
    PersistentValue persistentValue = PersistentValueManager.loadPersistentValue(Traffic.class.getName() + "." + url);
    String content = persistentValue == null ? null : persistentValue.getValue();
    if (PersistentValueManager.isAged(persistentValue)) {
        try {
            String page = Tools.getPage(new URL(url));
            if (page != null && page.length() > 0)
                content = page;
        } catch (Exception ex) {
            Tools.logException(Traffic.class, ex, "Could not cache traffic: " + url);
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
            if (root != null && root.getName().equals("ResultSet")) {
                for (Iterator i = root.elementIterator("Result"); i.hasNext(); ) {
                    Element result = (Element) i.next();
                    String value = null;
                    String type = null;
                    String title = null;
                    String description = null;
                    String severity = null;
                    String direction = null;
                    String map = null;
                    if ((value = Tools.getAttribute(result, "type")) != null) {
                        type = value;
                    }
                    if ((value = Tools.getAttribute(result, "Title")) != null) {
                        title = value;
                    }
                    if ((value = Tools.getAttribute(result, "Description")) != null) {
                        description = Tools.cleanHTML(value);
                    }
                    if ((value = Tools.getAttribute(result, "Severity")) != null) {
                        severity = Tools.cleanHTML(value);
                    }
                    if ((value = Tools.getAttribute(result, "Direction")) != null) {
                        direction = Tools.cleanHTML(value);
                    }
                    if ((value = Tools.getAttribute(result, "ImageUrl")) != null) {
                        map = Tools.cleanHTML(value);
                    }
                    if ((value = Tools.getAttribute(result, "EndDate")) != null) {
                        try {
                            long time = Long.parseLong(value) * 1000;
                            Date date = new Date(time);
                            log.info(date);
                            if (date.after(new Date()))
                                trafficResults.add(new Result(type, title, description, severity, direction, map));
                        } catch (Exception ex) {
                            Tools.logException(Traffic.class, ex);
                        }
                    } else
                        trafficResults.add(new Result(type, title, description, severity, direction, map));
                }
            }
            document.clearContent();
            document = null;
            if (PersistentValueManager.isAged(persistentValue)) {
                PersistentValueManager.savePersistentValue(Traffic.class.getName() + "." + url, content, 60);
            }
        } catch (Exception ex) {
            Tools.logException(Traffic.class, ex, "Could not download traffic: " + url);
            return null;
        }
    }
    return trafficResults;
}
