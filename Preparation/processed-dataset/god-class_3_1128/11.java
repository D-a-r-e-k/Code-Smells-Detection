/**
     * Set up the PUT/PATCH data
     */
private String sendEntityData(EntityEnclosingMethod put) throws IOException {
    // Buffer to hold the put body, except file content 
    StringBuilder putBody = new StringBuilder(1000);
    boolean hasPutBody = false;
    // Check if the header manager had a content type header 
    // This allows the user to specify his own content-type for a POST request 
    Header contentTypeHeader = put.getRequestHeader(HTTPConstants.HEADER_CONTENT_TYPE);
    boolean hasContentTypeHeader = contentTypeHeader != null && contentTypeHeader.getValue() != null && contentTypeHeader.getValue().length() > 0;
    HTTPFileArg files[] = getHTTPFiles();
    // If there are no arguments, we can send a file as the body of the request 
    if (!hasArguments() && getSendFileAsPostBody()) {
        hasPutBody = true;
        // If getSendFileAsPostBody returned true, it's sure that file is not null 
        FileRequestEntity fileRequestEntity = new FileRequestEntity(new File(files[0].getPath()), null);
        put.setRequestEntity(fileRequestEntity);
    } else if (getSendParameterValuesAsPostBody()) {
        hasPutBody = true;
        // If a content encoding is specified, we set it as http parameter, so that 
        // the post body will be encoded in the specified content encoding 
        String contentEncoding = getContentEncoding();
        boolean haveContentEncoding = false;
        if (isNullOrEmptyTrimmed(contentEncoding)) {
            contentEncoding = null;
        } else {
            put.getParams().setContentCharset(contentEncoding);
            haveContentEncoding = true;
        }
        // Just append all the parameter values, and use that as the post body 
        StringBuilder putBodyContent = new StringBuilder();
        PropertyIterator args = getArguments().iterator();
        while (args.hasNext()) {
            HTTPArgument arg = (HTTPArgument) args.next().getObjectValue();
            String value = null;
            if (haveContentEncoding) {
                value = arg.getEncodedValue(contentEncoding);
            } else {
                value = arg.getEncodedValue();
            }
            putBodyContent.append(value);
        }
        String contentTypeValue = null;
        if (hasContentTypeHeader) {
            contentTypeValue = put.getRequestHeader(HTTPConstants.HEADER_CONTENT_TYPE).getValue();
        }
        StringRequestEntity requestEntity = new StringRequestEntity(putBodyContent.toString(), contentTypeValue, put.getRequestCharSet());
        put.setRequestEntity(requestEntity);
    }
    // Check if we have any content to send for body 
    if (hasPutBody) {
        // If the request entity is repeatable, we can send it first to 
        // our own stream, so we can return it 
        if (put.getRequestEntity().isRepeatable()) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            put.getRequestEntity().writeRequest(bos);
            bos.flush();
            // We get the posted bytes using the charset that was used to create them 
            putBody.append(new String(bos.toByteArray(), put.getRequestCharSet()));
            bos.close();
        } else {
            putBody.append("<RequestEntity was not repeatable, cannot view what was sent>");
        }
        if (!hasContentTypeHeader) {
            // Allow the mimetype of the file to control the content type 
            // This is not obvious in GUI if you are not uploading any files, 
            // but just sending the content of nameless parameters 
            // TODO: needs a multiple file upload scenerio 
            HTTPFileArg file = files.length > 0 ? files[0] : null;
            if (file != null && file.getMimeType() != null && file.getMimeType().length() > 0) {
                put.setRequestHeader(HTTPConstants.HEADER_CONTENT_TYPE, file.getMimeType());
            }
        }
        // Set the content length 
        put.setRequestHeader(HTTPConstants.HEADER_CONTENT_LENGTH, Long.toString(put.getRequestEntity().getContentLength()));
    }
    return putBody.toString();
}
