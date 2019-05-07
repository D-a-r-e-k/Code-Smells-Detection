/**
     *  Scans file using server cgi
     *
     *@param  dir     directory to fetch scripts from
     *@param  addTo   Which vector to add info to
     */
private void scanFiles(String dir, Vector addTo) {
    URL cgiUrl = null;
    try {
        //String s = repository.toString().replaceAll("/AoIRepository/", "");  
        String s = repository.toString();
        String err = "";
        s = s.substring(0, s.lastIndexOf('/'));
        cgiUrl = new URL(s + "/cgi-bin/scripts.cgi?" + dir + "%20" + SPManagerPlugin.AOI_VERSION);
        //cgiUrl = new URL( s + "/cgi-bin/scripts.cgi?-z%20" + dir + "%20" + SPManagerPlugin.AOI_VERSION );  
        //cgiUrl = new URL( s + "/cgi-bin/RepoServer");  
        //cgiUrl = new URL( s + "/cgi-bin/RepoServer?HTTP_X_AOI_Dir=" + dir + "&HTTP_X_AOI_Version="  
        //	+ SPManagerPlugin.AOI_VERSION);  
        String content = null;
        boolean received = false;
        int attempts = 0;
        System.out.println(cgiUrl);
        while (!received && attempts++ < 5) {
            HttpURLConnection conn = (HttpURLConnection) cgiUrl.openConnection();
            conn.setRequestProperty("Accept-Encoding", "deflate");
            conn.setRequestProperty("X-AOI-Version", SPManagerPlugin.AOI_VERSION);
            conn.setRequestProperty("X-AOI-Dir", dir);
            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                err = conn.getResponseMessage();
                Thread.sleep(250);
                continue;
            }
            //InputStream is= cgiUrl.openStream();  
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            Document doc = SPManagerUtils.builder.parse(bis);
            NodeList tst = doc.getElementsByTagName("scriptcollection");
            if (tst.getLength() > 0) {
                received = true;
                NodeList nl = doc.getElementsByTagName("scriptreference");
                Node script;
                String location = "";
                long length = 0;
                for (int i = 0; i < nl.getLength(); i++) {
                    Node n = nl.item(i);
                    NodeList nnl = n.getChildNodes();
                    script = null;
                    location = null;
                    for (int j = 0; j < nnl.getLength(); j++) {
                        if ("scriptlocation".equals(nnl.item(j).getNodeName())) {
                            location = repository.toString() + "/" + dir + "/" + nnl.item(j).getChildNodes().item(0).getNodeValue();
                        } else if ("scriptlength".equals(nnl.item(j).getNodeName())) {
                            length = Long.parseLong(nnl.item(j).getChildNodes().item(0).getNodeValue());
                        } else if ("extension".equals(nnl.item(j).getNodeName()))
                            script = nnl.item(j);
                        else if ("script".equals(nnl.item(j).getNodeName()))
                            script = nnl.item(j);
                    }
                    //System.out.println( location + " / " + script + " / " + length);  
                    if (script != null && location != null) {
                        addTo.add(new SPMObjectInfo(script, new URL(location), length));
                    }
                }
            }
            bis.close();
        }
        if (!received) {
            JOptionPane.showMessageDialog(null, cgiUrl.toString() + ": " + SPMTranslate.text("scriptServerFailed"), SPMTranslate.text("error") + " " + err, JOptionPane.ERROR_MESSAGE);
            return;
        }
    } catch (Exception e) {
        if (e instanceof UnknownHostException) {
            JOptionPane.showMessageDialog(null, cgiUrl.toString() + ": " + SPMTranslate.text("unknownHost"), SPMTranslate.text("error"), JOptionPane.ERROR_MESSAGE);
            unknownHost = true;
        } else if (e instanceof FileNotFoundException) {
            JOptionPane.showMessageDialog(null, cgiUrl.toString() + ": " + SPMTranslate.text("fileNotFound"), SPMTranslate.text("error"), JOptionPane.ERROR_MESSAGE);
            unknownHost = true;
        } else {
            JOptionPane.showMessageDialog(null, cgiUrl.toString() + ": " + SPMTranslate.text("httpError"), SPMTranslate.text("error") + ": " + e.getMessage(), JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    // sort the result  
    SPMObjectInfo left, right;
    int i, j;
    for (i = addTo.size() - 1; i > 0; i--) {
        j = i;
        right = (SPMObjectInfo) addTo.get(i);
        while (j > 0) {
            left = (SPMObjectInfo) addTo.get(j - 1);
            if (right.getName().compareTo(left.getName()) >= 0)
                break;
            j--;
        }
        // relocate  
        if (j < i) {
            addTo.remove(i);
            addTo.add(j, right);
        }
    }
}
