public static void getMovie2(Movie movie) {
    String imdb = movie.getIMDB();
    if (imdb == null || imdb.length() == 0) {
        imdb = getIMDBID(movie.getTitle());
    }
    if (imdb != null) {
        movie.setIMDB(imdb);
        StringBuffer buffer = new StringBuffer();
        byte[] buf = new byte[1024];
        int amount = 0;
        try {
            URL url = new URL("http://nicholls.us/imdb/imdbxml.php?mid=" + imdb);
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
            movie.setTitle(clean(Tools.getAttribute(root, "title")));
            try {
                movie.setDate(Integer.parseInt(clean(Tools.getAttribute(root, "year"))));
            } catch (Exception ex) {
            }
            movie.setThumbUrl(clean(Tools.getAttribute(root, "photoUrl")));
            try {
                movie.setDuration(Integer.parseInt(clean(Tools.getAttribute(root, "runtime"))));
            } catch (Exception ex) {
            }
            movie.setRating((int) Float.parseFloat(clean(Tools.getAttribute(root, "rating"))));
            movie.setRated(clean(Tools.getAttribute(root, "rated")));
            movie.setGenre(clean(Tools.getAttribute(root, "genres")));
            movie.setTagline(clean(Tools.getAttribute(root, "tagline")));
            movie.setDirector(clean(Tools.getAttribute(root, "director")));
            movie.setCredits(clean(Tools.getAttribute(root, "writer")));
            movie.setProducer(clean(Tools.getAttribute(root, "producer")));
            movie.setActors(clean(Tools.getAttribute(root, "cast")));
            movie.setPlotOutline(clean(Tools.getAttribute(root, "outline")));
            movie.setPlot(clean(Tools.getAttribute(root, "plot")));
        } catch (Exception ex) {
            Tools.logException(IMDB.class, ex, "Could not get IMDB data: " + movie.getTitle());
        }
    }
}
