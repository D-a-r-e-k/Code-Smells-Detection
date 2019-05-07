/**
     *  Description of the Method
     *
     *@param  from              Description of the Parameter
     *@param  fileName          Description of the Parameter
     *@param  status            Description of the Parameter
     *@param  lengthToDownload  Description of the Parameter
     *@param  downloadedLength  Description of the Parameter
     *@return                   Description of the Return Value
     */
public static long downloadRemoteBinaryFile(URL from, String fileName, long size, StatusDialog status, long totalDownload, long downloadedLength, ArrayList errors) {
    System.out.println("download: size=" + size + "; total=" + totalDownload + "; downloaded=" + downloadedLength);
    //if (fileName.endsWith(".upd")) return 0;  
    File update = new File(fileName);
    Thread thread = Thread.currentThread();
    BufferedInputStream in = null;
    BufferedOutputStream file = null;
    long initialValue = downloadedLength;
    try {
        HttpURLConnection conn = (HttpURLConnection) from.openConnection();
        conn.setRequestProperty("Cache-Control", "no-cache");
        if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
            new BStandardDialog("SPManager", new String[] { SPMTranslate.text("httpError"), conn.getResponseMessage() + " (" + conn.getResponseCode() + ")" }, BStandardDialog.ERROR).showMessageDialog(SPManagerFrame.getInstance());
            return 0;
        }
        //in = new BufferedInputStream( from.openStream() );  
        in = new BufferedInputStream(conn.getInputStream());
        file = new BufferedOutputStream(new FileOutputStream(update));
        double a;
        double b = totalDownload;
        int value;
        int newValue;
        value = status.getBarValue();
        String mod = "", newMod;
        if (b <= 0)
            status.setIdle(true);
        int result = 0;
        while ((result = in.read()) != -1) {
            if (thread.interrupted()) {
                thread.interrupt();
                if (!update.delete()) {
                    RandomAccessFile raf = new RandomAccessFile(update, "rw");
                    raf.setLength(0);
                    raf.close();
                }
                throw new InterruptedException("download cancelled: " + fileName);
            }
            file.write((byte) result);
            ++downloadedLength;
            a = downloadedLength;
            if (b > a) {
                newValue = (int) Math.round(a * 100.0 / b);
                if (newValue > value) {
                    status.setBarValue(newValue);
                    status.setProgressText(newValue + "%");
                    value = newValue;
                }
            } else {
                newMod = (a > 1000000 ? ((int) (a / 100000.0)) + " MB" : a > 1000 ? ((int) (a / 1000.0)) + " kB" : a + " bytes");
                if (!newMod.equals(mod))
                    status.setProgressText(newMod);
            }
        }
        file.flush();
        file.close();
        // check we got the expected data  
        if (size > 0) {
            long received = downloadedLength - initialValue;
            if (received != size)
                throw new IOException("SPManager: file incomplete." + " Only received " + received + " bytes of " + size);
        }
        // test validity of zipfiles	      
        if (fileName.endsWith(".jar") || fileName.endsWith(".zip")) {
            new ZipFile(update);
            System.out.println("SPManager: ZipFile ok");
        }
    } catch (Exception e) {
        errors.add(SPMTranslate.text("error") + "(" + fileName + ")" + e);
    } finally {
        try {
            if (in != null)
                in.close();
            if (file != null)
                file.close();
        } catch (IOException e) {
            System.out.println("SPManager: error closing " + fileName + ": " + e);
        }
    }
    return downloadedLength - initialValue;
}
