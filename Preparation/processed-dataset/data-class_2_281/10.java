/**
     *  Description of the Method
     *
     *@param  fileName          Description of the Parameter
     *@param  from              Description of the Parameter
     *@param  status            Description of the Parameter
     *@param  downloadedLength  Description of the Parameter
     *@param  lengthToDownload  Description of the Parameter
     *@return                   Description of the Return Value
     */
public static long downloadRemoteTextFile(URL from, String fileName, long size, StatusDialog status, long totalDownload, long downloadedLength, ArrayList errors) {
    //if (fileName.endsWith(".upd")) return 0;  
    BufferedReader in = null;
    BufferedWriter file = null;
    long initialValue = downloadedLength;
    //System.out.println( from + ": downloadedLength :" + downloadedLength + " " + lengthToDownload );  
    try {
        HttpURLConnection conn = (HttpURLConnection) from.openConnection();
        conn.setRequestProperty("Cache-Control", "no-cache");
        if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
            new BStandardDialog("SPManager", new String[] { SPMTranslate.text("httpError"), conn.getResponseMessage() + " (" + conn.getResponseCode() + ")" }, BStandardDialog.ERROR).showMessageDialog(SPManagerFrame.getInstance());
            return 0;
        }
        //in = new BufferedReader( new InputStreamReader( from.openStream() ) );  
        in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        file = new BufferedWriter(new FileWriter(fileName));
        double a;
        double b = totalDownload;
        int value;
        int newValue;
        value = status.getBarValue();
        int i = in.read();
        while (i != -1) {
            file.write(i);
            i = in.read();
            if (status != null) {
                ++downloadedLength;
                a = downloadedLength;
                newValue = (int) Math.round((a * 100.0) / b);
                if (newValue > value) {
                    status.setBarValue(newValue);
                    status.setProgressText(newValue + "%");
                    value = newValue;
                }
            }
        }
        file.flush();
        file.close();
        //System.out.println( "downloadedLength :" + downloadedLength + " " + ( downloadedLength - initialValue ) );  
        // check we got the expected data  
        long received = downloadedLength - initialValue;
        if (received != size)
            throw new IOException("SPManager: file incomplete." + " Only received " + received + " bytes of " + size);
    } catch (Exception e) {
        /*
            e.printStackTrace();
            JOptionPane.showMessageDialog( null, from.toString() + ": " + SPMTranslate.text( "fileNotFound" ), SPMTranslate.text( "error" ), JOptionPane.ERROR_MESSAGE );
	    */
        errors.add(SPMTranslate.text("error") + "(" + fileName + ")" + e);
    } finally {
        try {
            if (in != null)
                in.close();
            if (file != null)
                file.close();
        } catch (IOException e) {
            //e.printStackTrace();  
            System.out.println("SPManager: error closing " + fileName + ": " + e);
        }
    }
    return downloadedLength - initialValue;
}
