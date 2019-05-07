private String getFilePath(File file, File directory) {
    try {
        if (file != null && directory != null) {
            if (directory.getCanonicalPath().length() > 0) {
                String sub = file.getCanonicalPath().substring(directory.getCanonicalPath().length() + 1);
                if (sub.length() > 0) {
                    return sub.replaceAll("\\\\", "/");
                }
            }
        }
    } catch (Exception ex) {
    }
    return "";
}
