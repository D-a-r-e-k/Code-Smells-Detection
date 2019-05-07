/**
         * Tries to change the encoding used to read the input stream to the specified one
         * @param charset the charset that should be used
         * @return <code>true</code> when the encoding has been changed
         */
private boolean changeEncoding(String charset) {
    if (charset == null || fByteStream == null) {
        return false;
    }
    charset = charset.trim();
    boolean encodingChanged = false;
    try {
        String ianaEncoding = charset;
        String javaEncoding = EncodingMap.getIANA2JavaMapping(ianaEncoding.toUpperCase());
        if (DEBUG_CHARSET) {
            System.out.println("+++ ianaEncoding: " + ianaEncoding);
            System.out.println("+++ javaEncoding: " + javaEncoding);
        }
        if (javaEncoding == null) {
            javaEncoding = ianaEncoding;
            if (fReportErrors) {
                fErrorReporter.reportError("HTML1001", new Object[] { ianaEncoding });
            }
        }
        // patch: Marc Guillemot 
        if (!javaEncoding.equals(fJavaEncoding)) {
            if (!isEncodingCompatible(javaEncoding, fJavaEncoding)) {
                if (fReportErrors) {
                    fErrorReporter.reportError("HTML1015", new Object[] { javaEncoding, fJavaEncoding });
                }
            } else {
                fIso8859Encoding = ianaEncoding == null || ianaEncoding.toUpperCase().startsWith("ISO-8859") || ianaEncoding.equalsIgnoreCase(fDefaultIANAEncoding);
                fJavaEncoding = javaEncoding;
                fCurrentEntity.setStream(new InputStreamReader(fByteStream, javaEncoding));
                fByteStream.playback();
                fElementDepth = fElementCount;
                fElementCount = 0;
                encodingChanged = true;
            }
        }
    } catch (UnsupportedEncodingException e) {
        if (fReportErrors) {
            fErrorReporter.reportError("HTML1010", new Object[] { charset });
        }
        // NOTE: If the encoding change doesn't work,  
        //       then there's no point in continuing to  
        //       buffer the input stream. 
        fByteStream.clear();
        fByteStream = null;
    }
    return encodingChanged;
}
