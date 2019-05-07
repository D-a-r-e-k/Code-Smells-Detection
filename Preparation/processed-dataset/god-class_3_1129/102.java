/**
     * Saves the list of files.
     * The first file is saved in the Filename/field/mimetype properties.
     * Any additional files are saved in the FILE_ARGS array.
     *
     * @param files list of files to save
     */
public void setHTTPFiles(HTTPFileArg[] files) {
    HTTPFileArgs fileArgs = new HTTPFileArgs();
    // Weed out the empty files 
    if (files.length > 0) {
        for (int i = 0; i < files.length; i++) {
            HTTPFileArg file = files[i];
            if (file.isNotEmpty()) {
                fileArgs.addHTTPFileArg(file);
            }
        }
    }
    setHTTPFileArgs(fileArgs);
}
