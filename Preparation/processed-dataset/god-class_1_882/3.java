private String getFileName(String path, String url, String title) {
    final String filePrefix = javaNormalize(url) + "." + javaNormalize(title);
    File p = new File(path);
    String buf = filePrefix;
    for (int i = 1; prefixExists(p, buf); ++i) {
        buf = filePrefix + "_" + i;
    }
    return path + File.separator + buf + XML_BEAN_POSTFIX;
}
