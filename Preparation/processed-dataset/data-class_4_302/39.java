// expandSystemIdStrictOff(String,String):String  
public static OutputStream createOutputStream(String uri) throws IOException {
    // URI was specified. Handle relative URIs.  
    final String expanded = XMLEntityManager.expandSystemId(uri, null, true);
    final URL url = new URL(expanded != null ? expanded : uri);
    OutputStream out = null;
    String protocol = url.getProtocol();
    String host = url.getHost();
    // Use FileOutputStream if this URI is for a local file.  
    if (protocol.equals("file") && (host == null || host.length() == 0 || host.equals("localhost"))) {
        File file = new File(getPathWithoutEscapes(url.getPath()));
        if (!file.exists()) {
            File parent = file.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }
        }
        out = new FileOutputStream(file);
    } else {
        URLConnection urlCon = url.openConnection();
        urlCon.setDoInput(false);
        urlCon.setDoOutput(true);
        urlCon.setUseCaches(false);
        // Enable tunneling.  
        if (urlCon instanceof HttpURLConnection) {
            // The DOM L3 REC says if we are writing to an HTTP URI  
            // it is to be done with an HTTP PUT.  
            HttpURLConnection httpCon = (HttpURLConnection) urlCon;
            httpCon.setRequestMethod("PUT");
        }
        out = urlCon.getOutputStream();
    }
    return out;
}
