public Enumeration getUsers(String domain) {
    String path = parent.getProperty("webmail.data.path") + System.getProperty("file.separator") + domain + System.getProperty("file.separator");
    File f = new File(path);
    if (f.canRead() && f.isDirectory()) {
        final String[] files = f.list(new FilenameFilter() {

            public boolean accept(File file, String s) {
                if (s.endsWith(".xml")) {
                    return true;
                } else {
                    return false;
                }
            }
        });
        return new Enumeration() {

            int i = 0;

            public boolean hasMoreElements() {
                return i < files.length;
            }

            public Object nextElement() {
                int cur = i++;
                return files[cur].substring(0, files[cur].length() - 4);
            }
        };
    } else {
        log(Storage.LOG_WARN, "SimpleStorage: Could not list files in directory " + path);
        return new Enumeration() {

            public boolean hasMoreElements() {
                return false;
            }

            public Object nextElement() {
                return null;
            }
        };
    }
}
