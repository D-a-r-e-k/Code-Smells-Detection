/* (non-Javadoc)
     * @see net.sf.jmoney.FileFormat#importFile(net.sf.jmoney.model.Session, java.io.File)
     */
public void importFile(Session session, File qifFile) {
    try {
        BufferedReader reader = new BufferedReader(new FileReader(qifFile));
        String header = reader.readLine();
        int count = 0;
        boolean finished = false;
        while (!finished) {
            finished = importTransaction(session, reader);
            count++;
        }
    } catch (IOException e) {
        mainFrame.fileReadError(qifFile);
    }
}
