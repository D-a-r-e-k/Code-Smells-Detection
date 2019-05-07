public static String getIMDBID2(String key) {
    String imdb = null;
    if (key != null) {
        StringBuffer buffer = new StringBuffer();
        byte[] buf = new byte[1024];
        int amount = 0;
        try {
            URL url = new URL("http://nicholls.us/imdb/imdbsearchxml.php?name=" + URLEncoder.encode(key));
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("User-Agent", "Galleon " + Tools.getVersion());
            conn.setInstanceFollowRedirects(true);
            InputStream input = conn.getInputStream();
            while ((amount = input.read(buf)) > 0) {
                buffer.append(new String(buf, 0, amount));
            }
            input.close();
            conn.disconnect();
            SAXReader saxReader = new SAXReader();
            StringReader stringReader = new StringReader(buffer.toString().trim());
            Document document = saxReader.read(stringReader);
            //Document document = saxReader.read(new File("d:/galleon/imdb.xml")); 
            Element root = document.getRootElement();
            return Tools.getAttribute(root, "imdb");
        } catch (Exception ex) {
            Tools.logException(IMDB.class, ex, "Could not get IMDB ID: " + key);
        }
    }
    return imdb;
}
