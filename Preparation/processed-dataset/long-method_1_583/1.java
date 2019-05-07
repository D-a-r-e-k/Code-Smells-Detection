/**
     * Write out the converted template to the given named file
     * and base directory.
     */
private boolean writeTemplate(String file, String basedir, String newBasedir) {
    if (file.indexOf(WM_EXT) < 0) {
        return false;
    }
    System.out.println("Converting " + file + "...");
    String template = file;
    String newTemplate = convertName(file);
    if (basedir.length() > 0) {
        String templateDir = newBasedir + extractPath(file);
        File outputDirectory = new File(templateDir);
        template = basedir + File.separator + file;
        if (!outputDirectory.exists()) {
            outputDirectory.mkdirs();
        }
        newTemplate = newBasedir + File.separator + convertName(file);
    }
    String convertedTemplate = convertTemplate(template);
    FileWriter fw = null;
    try {
        fw = new FileWriter(newTemplate);
        fw.write(convertedTemplate);
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        if (fw != null) {
            try {
                fw.close();
            } catch (IOException io) {
            }
        }
    }
    return true;
}
