/**
     * Iterate through the set of find/replace regexes
     * that will convert a given WM template to a VM template
     * @param target
     */
public void convert(String target) {
    File file = new File(target);
    if (!file.exists()) {
        throw new RuntimeException("The specified template or directory does not exist");
    }
    if (file.isDirectory()) {
        String basedir = file.getAbsolutePath();
        String newBasedir = basedir + VM_EXT;
        DirectoryScanner ds = new DirectoryScanner();
        ds.setBasedir(basedir);
        ds.addDefaultExcludes();
        ds.scan();
        String[] files = ds.getIncludedFiles();
        for (int i = 0; i < files.length; i++) {
            writeTemplate(files[i], basedir, newBasedir);
        }
    } else {
        writeTemplate(file.getAbsolutePath(), "", "");
    }
}
