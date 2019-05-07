//}}} 
// {{{ getActivityLogContents() 
/**
     * Gets contents of the ativity log jsxe.log
     * @return ArrayList containing lines from the activity log
     * @since jsXe 0.3pre15
     */
public ArrayList getActivityLogContents() {
    String homeDir = System.getProperty("user.home");
    File activityLog = new File(homeDir + System.getProperty("file.separator") + ".jsxe" + System.getProperty("file.separator") + "jsXe.log");
    String line;
    ArrayList logContents = new ArrayList();
    try {
        BufferedReader reader = new BufferedReader(new FileReader(activityLog));
        try {
            while ((line = reader.readLine()) != null) {
                logContents.add(line);
            }
            reader.close();
        } catch (IOException e1) {
            Log.log(Log.ERROR, this, e1);
        }
    } catch (FileNotFoundException e) {
        Log.log(Log.ERROR, this, e);
    }
    return logContents;
}
