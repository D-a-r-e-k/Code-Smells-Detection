/**
     * Get the collection of files as a list.
     * The list is built up from the filename/filefield/mimetype properties,
     * plus any additional entries saved in the FILE_ARGS property.
     *
     * If there are no valid file entries, then an empty list is returned.
     *
     * @return an array of file arguments (never null)
     */
public HTTPFileArg[] getHTTPFiles() {
    final HTTPFileArgs fileArgs = getHTTPFileArgs();
    return fileArgs == null ? new HTTPFileArg[] {} : fileArgs.asArray();
}
