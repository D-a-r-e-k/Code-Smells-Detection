/*
     * Send POST data from <code>Entry</code> to the open connection.
     *
     * @param connection
     *            <code>URLConnection</code> where POST data should be sent
     * @return a String show what was posted. Will not contain actual file upload content
     * @exception IOException
     *                if an I/O exception occurs
     */
private String sendPostData(PostMethod post) throws IOException {
    // Buffer to hold the post body, except file content 
    StringBuilder postedBody = new StringBuilder(1000);
    HTTPFileArg files[] = getHTTPFiles();
    // Check if we should do a multipart/form-data or an 
    // application/x-www-form-urlencoded post request 
    if (getUseMultipartForPost()) {
        // If a content encoding is specified, we use that as the 
        // encoding of any parameter values 
        String contentEncoding = getContentEncoding();
        if (isNullOrEmptyTrimmed(contentEncoding)) {
            contentEncoding = null;
        }
        final boolean browserCompatible = getDoBrowserCompatibleMultipart();
        // We don't know how many entries will be skipped 
        ArrayList<PartBase> partlist = new ArrayList<PartBase>();
        // Create the parts 
        // Add any parameters 
        PropertyIterator args = getArguments().iterator();
        while (args.hasNext()) {
            HTTPArgument arg = (HTTPArgument) args.next().getObjectValue();
            String parameterName = arg.getName();
            if (arg.isSkippable(parameterName)) {
                continue;
            }
            StringPart part = new StringPart(arg.getName(), arg.getValue(), contentEncoding);
            if (browserCompatible) {
                part.setTransferEncoding(null);
                part.setContentType(null);
            }
            partlist.add(part);
        }
        // Add any files 
        for (int i = 0; i < files.length; i++) {
            HTTPFileArg file = files[i];
            File inputFile = new File(file.getPath());
            // We do not know the char set of the file to be uploaded, so we set it to null 
            ViewableFilePart filePart = new ViewableFilePart(file.getParamName(), inputFile, file.getMimeType(), null);
            filePart.setCharSet(null);
            // We do not know what the char set of the file is 
            partlist.add(filePart);
        }
        // Set the multipart for the post 
        int partNo = partlist.size();
        Part[] parts = partlist.toArray(new Part[partNo]);
        MultipartRequestEntity multiPart = new MultipartRequestEntity(parts, post.getParams());
        post.setRequestEntity(multiPart);
        // Set the content type 
        String multiPartContentType = multiPart.getContentType();
        post.setRequestHeader(HTTPConstants.HEADER_CONTENT_TYPE, multiPartContentType);
        // If the Multipart is repeatable, we can send it first to 
        // our own stream, without the actual file content, so we can return it 
        if (multiPart.isRepeatable()) {
            // For all the file multiparts, we must tell it to not include 
            // the actual file content 
            for (int i = 0; i < partNo; i++) {
                if (parts[i] instanceof ViewableFilePart) {
                    ((ViewableFilePart) parts[i]).setHideFileData(true);
                }
            }
            // Write the request to our own stream 
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            multiPart.writeRequest(bos);
            bos.flush();
            // We get the posted bytes using the encoding used to create it 
            postedBody.append(new String(bos.toByteArray(), contentEncoding == null ? "US-ASCII" : contentEncoding));
            bos.close();
            // For all the file multiparts, we must revert the hiding of 
            // the actual file content 
            for (int i = 0; i < partNo; i++) {
                if (parts[i] instanceof ViewableFilePart) {
                    ((ViewableFilePart) parts[i]).setHideFileData(false);
                }
            }
        } else {
            postedBody.append("<Multipart was not repeatable, cannot view what was sent>");
        }
    } else {
        // Check if the header manager had a content type header 
        // This allows the user to specify his own content-type for a POST request 
        Header contentTypeHeader = post.getRequestHeader(HTTPConstants.HEADER_CONTENT_TYPE);
        boolean hasContentTypeHeader = contentTypeHeader != null && contentTypeHeader.getValue() != null && contentTypeHeader.getValue().length() > 0;
        // If there are no arguments, we can send a file as the body of the request 
        // TODO: needs a multiple file upload scenerio 
        if (!hasArguments() && getSendFileAsPostBody()) {
            // If getSendFileAsPostBody returned true, it's sure that file is not null 
            HTTPFileArg file = files[0];
            if (!hasContentTypeHeader) {
                // Allow the mimetype of the file to control the content type 
                if (file.getMimeType() != null && file.getMimeType().length() > 0) {
                    post.setRequestHeader(HTTPConstants.HEADER_CONTENT_TYPE, file.getMimeType());
                } else {
                    post.setRequestHeader(HTTPConstants.HEADER_CONTENT_TYPE, HTTPConstants.APPLICATION_X_WWW_FORM_URLENCODED);
                }
            }
            FileRequestEntity fileRequestEntity = new FileRequestEntity(new File(file.getPath()), null);
            post.setRequestEntity(fileRequestEntity);
            // We just add placeholder text for file content 
            postedBody.append("<actual file content, not shown here>");
        } else {
            // In a post request which is not multipart, we only support 
            // parameters, no file upload is allowed 
            // If a content encoding is specified, we set it as http parameter, so that 
            // the post body will be encoded in the specified content encoding 
            String contentEncoding = getContentEncoding();
            boolean haveContentEncoding = false;
            if (isNullOrEmptyTrimmed(contentEncoding)) {
                contentEncoding = null;
            } else {
                post.getParams().setContentCharset(contentEncoding);
                haveContentEncoding = true;
            }
            // If none of the arguments have a name specified, we 
            // just send all the values as the post body 
            if (getSendParameterValuesAsPostBody()) {
                // Allow the mimetype of the file to control the content type 
                // This is not obvious in GUI if you are not uploading any files, 
                // but just sending the content of nameless parameters 
                // TODO: needs a multiple file upload scenerio 
                if (!hasContentTypeHeader) {
                    HTTPFileArg file = files.length > 0 ? files[0] : null;
                    if (file != null && file.getMimeType() != null && file.getMimeType().length() > 0) {
                        post.setRequestHeader(HTTPConstants.HEADER_CONTENT_TYPE, file.getMimeType());
                    } else {
                        // TODO - is this the correct default? 
                        post.setRequestHeader(HTTPConstants.HEADER_CONTENT_TYPE, HTTPConstants.APPLICATION_X_WWW_FORM_URLENCODED);
                    }
                }
                // Just append all the parameter values, and use that as the post body 
                StringBuilder postBody = new StringBuilder();
                PropertyIterator args = getArguments().iterator();
                while (args.hasNext()) {
                    HTTPArgument arg = (HTTPArgument) args.next().getObjectValue();
                    String value;
                    if (haveContentEncoding) {
                        value = arg.getEncodedValue(contentEncoding);
                    } else {
                        value = arg.getEncodedValue();
                    }
                    postBody.append(value);
                }
                StringRequestEntity requestEntity = new StringRequestEntity(postBody.toString(), post.getRequestHeader(HTTPConstants.HEADER_CONTENT_TYPE).getValue(), contentEncoding);
                post.setRequestEntity(requestEntity);
            } else {
                // It is a normal post request, with parameter names and values 
                // Set the content type 
                if (!hasContentTypeHeader) {
                    post.setRequestHeader(HTTPConstants.HEADER_CONTENT_TYPE, HTTPConstants.APPLICATION_X_WWW_FORM_URLENCODED);
                }
                // Add the parameters 
                PropertyIterator args = getArguments().iterator();
                while (args.hasNext()) {
                    HTTPArgument arg = (HTTPArgument) args.next().getObjectValue();
                    // The HTTPClient always urlencodes both name and value, 
                    // so if the argument is already encoded, we have to decode 
                    // it before adding it to the post request 
                    String parameterName = arg.getName();
                    if (arg.isSkippable(parameterName)) {
                        continue;
                    }
                    String parameterValue = arg.getValue();
                    if (!arg.isAlwaysEncoded()) {
                        // The value is already encoded by the user 
                        // Must decode the value now, so that when the 
                        // httpclient encodes it, we end up with the same value 
                        // as the user had entered. 
                        String urlContentEncoding = contentEncoding;
                        if (urlContentEncoding == null || urlContentEncoding.length() == 0) {
                            // Use the default encoding for urls 
                            urlContentEncoding = EncoderCache.URL_ARGUMENT_ENCODING;
                        }
                        parameterName = URLDecoder.decode(parameterName, urlContentEncoding);
                        parameterValue = URLDecoder.decode(parameterValue, urlContentEncoding);
                    }
                    // Add the parameter, httpclient will urlencode it 
                    post.addParameter(parameterName, parameterValue);
                }
            }
            // If the request entity is repeatable, we can send it first to 
            // our own stream, so we can return it 
            if (post.getRequestEntity().isRepeatable()) {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                post.getRequestEntity().writeRequest(bos);
                bos.flush();
                // We get the posted bytes using the encoding used to create it 
                postedBody.append(new String(bos.toByteArray(), post.getRequestCharSet()));
                bos.close();
            } else {
                postedBody.append("<RequestEntity was not repeatable, cannot view what was sent>");
            }
        }
    }
    // Set the content length 
    post.setRequestHeader(HTTPConstants.HEADER_CONTENT_LENGTH, Long.toString(post.getRequestEntity().getContentLength()));
    return postedBody.toString();
}
