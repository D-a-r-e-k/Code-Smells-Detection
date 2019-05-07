/**
     *  Scans file on a general basis
     *
     *@param  from    URL to scan files from
     *@param  addTo   Which vector to add info to
     *@param  suffix  Scanned files suffix
     */
private void scanFiles(URL from, Vector addTo, String suffix) {
    SPMObjectInfo info;
    boolean eligible;
    Vector v = null;
    try {
        Object obj = from.getContent();
        if (obj instanceof InputStream) {
            v = htmlFindFilesVersioning((InputStream) (obj), from);
        }
        ((InputStream) obj).close();
    } catch (IOException e) {
        if (e instanceof UnknownHostException) {
            JOptionPane.showMessageDialog(null, from.toString() + ": " + SPMTranslate.text("unknownHost"), SPMTranslate.text("error"), JOptionPane.ERROR_MESSAGE);
            unknownHost = true;
        } else if (e instanceof FileNotFoundException) {
            JOptionPane.showMessageDialog(null, from.toString() + ": " + SPMTranslate.text("fileNotFound"), SPMTranslate.text("error"), JOptionPane.ERROR_MESSAGE);
            unknownHost = true;
        } else
            e.printStackTrace();
    }
    if (v != null) {
        // sort the list  
        String[] sarray = (String[]) v.toArray(EMPTY_STRING_ARRAY);
        Arrays.sort(sarray);
        for (int i = 0; i < sarray.length; i++) {
            //String s = (String) v.elementAt( i );  
            String s = sarray[i];
            System.out.println(s);
            if (s.endsWith(suffix)) {
                //check if file candidate for update or install  
                eligible = true;
                String name = s.substring(0, s.length() - 4);
                if (suffix.equals(".jar")) {
                    //look for xml file  
                    /*
			 *  NTJ: for AOI 2.5: The XML file name has changed to
			 *  'extensions.xml'. For compatibility, the old name
			 *  is checked if the new name does not exist.
			 */
                    eligible = true;
                    String sxml;
                    sxml = s.substring(0, s.lastIndexOf('/')) + "extensions.xml";
                    URL xmlURL = null;
                    try {
                        xmlURL = new URL(from, sxml);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    try {
                        HttpURLConnection.setFollowRedirects(false);
                        HttpURLConnection conn = (HttpURLConnection) xmlURL.openConnection();
                        if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                            eligible = false;
                        } else {
                            sxml = s.substring(0, s.lastIndexOf('.')) + ".xml";
                            xmlURL = new URL(from, sxml);
                            conn = (HttpURLConnection) xmlURL.openConnection();
                            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                                eligible = false;
                            }
                        }
                        if (eligible) {
                            InputStreamReader in = new InputStreamReader(conn.getInputStream());
                            in.close();
                        }
                    } catch (IOException e) {
                        eligible = false;
                    }
                }
                info = null;
                if (eligible) {
                    System.out.println("adding: " + s);
                    try {
                        info = new SPMObjectInfo(new URL(from, s));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (info != null)
                    addTo.add(info);
            }
        }
    }
}
