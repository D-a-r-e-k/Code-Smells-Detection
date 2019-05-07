public ExtensionBasedFileFilter(String fileExtension, String description) {
    myDescription = description;
    myPattern = Pattern.compile(fileExtension);
}
