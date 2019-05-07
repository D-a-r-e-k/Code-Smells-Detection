/**
     * For the given <code>fileName</code>, attempt to expand it to its full path
     * for use as a system id.
     * 
     * @see #getURL(String)
     * @see #processFile()
     * @see #processFile(String)
     * @see #processFileAndScheduleJobs(Scheduler, boolean)
     * @see #processFileAndScheduleJobs(String, Scheduler, boolean)
     */
protected String getSystemIdForFileName(String fileName) {
    InputStream fileInputStream = null;
    try {
        String urlPath = null;
        File file = new File(fileName);
        // files in filesystem 
        if (!file.exists()) {
            URL url = getURL(fileName);
            if (url != null) {
                try {
                    urlPath = URLDecoder.decode(url.getPath(), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    log.warn("Unable to decode file path URL", e);
                }
                try {
                    if (url != null)
                        fileInputStream = url.openStream();
                } catch (IOException ignore) {
                }
            }
        } else {
            try {
                fileInputStream = new FileInputStream(file);
            } catch (FileNotFoundException ignore) {
            }
        }
        if (fileInputStream == null) {
            log.debug("Unable to resolve '" + fileName + "' to full path, so using it as is for system id.");
            return fileName;
        } else {
            return (urlPath != null) ? urlPath : file.getAbsolutePath();
        }
    } finally {
        try {
            if (fileInputStream != null) {
                fileInputStream.close();
            }
        } catch (IOException ioe) {
            log.warn("Error closing jobs file: " + fileName, ioe);
        }
    }
}
