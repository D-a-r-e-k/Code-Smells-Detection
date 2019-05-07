private boolean prefixExists(File path, final String filePrefix) {
    File[] files = path.listFiles(new FilenameFilter() {

        public boolean accept(File dir, String name) {
            if (name.toLowerCase().equals(filePrefix.toLowerCase() + XML_BEAN_POSTFIX)) {
                return true;
            }
            return false;
        }
    });
    return 0 < files.length;
}
