// getEncodingName(byte[],int):Object[]  
/**
     * Creates a reader capable of reading the given input stream in
     * the specified encoding.
     *
     * @param inputStream  The input stream.
     * @param encoding     The encoding name that the input stream is
     *                     encoded using. If the user has specified that
     *                     Java encoding names are allowed, then the
     *                     encoding name may be a Java encoding name;
     *                     otherwise, it is an ianaEncoding name.
     * @param isBigEndian   For encodings (like uCS-4), whose names cannot
     *                      specify a byte order, this tells whether the order is bigEndian. Null means
     *                      unknown or not relevant.
     *
     * @return Returns a reader.
     */
protected Reader createReader(InputStream inputStream, String encoding, Boolean isBigEndian) throws IOException {
    // if the encoding is UTF-8 use the optimized UTF-8 reader  
    if (encoding == "UTF-8" || encoding == null) {
        return createUTF8Reader(inputStream);
    }
    // If the encoding is UTF-16 use the optimized UTF-16 reader  
    if (encoding == "UTF-16" && isBigEndian != null) {
        return createUTF16Reader(inputStream, isBigEndian.booleanValue());
    }
    // try to use an optimized reader  
    String ENCODING = encoding.toUpperCase(Locale.ENGLISH);
    if (ENCODING.equals("UTF-8")) {
        return createUTF8Reader(inputStream);
    }
    if (ENCODING.equals("UTF-16BE")) {
        return createUTF16Reader(inputStream, true);
    }
    if (ENCODING.equals("UTF-16LE")) {
        return createUTF16Reader(inputStream, false);
    }
    if (ENCODING.equals("ISO-10646-UCS-4")) {
        if (isBigEndian != null) {
            boolean isBE = isBigEndian.booleanValue();
            if (isBE) {
                return new UCSReader(inputStream, UCSReader.UCS4BE);
            } else {
                return new UCSReader(inputStream, UCSReader.UCS4LE);
            }
        } else {
            fErrorReporter.reportError(XMLMessageFormatter.XML_DOMAIN, "EncodingByteOrderUnsupported", new Object[] { encoding }, XMLErrorReporter.SEVERITY_FATAL_ERROR);
        }
    }
    if (ENCODING.equals("ISO-10646-UCS-2")) {
        if (isBigEndian != null) {
            // should never happen with this encoding...  
            boolean isBE = isBigEndian.booleanValue();
            if (isBE) {
                return new UCSReader(inputStream, UCSReader.UCS2BE);
            } else {
                return new UCSReader(inputStream, UCSReader.UCS2LE);
            }
        } else {
            fErrorReporter.reportError(XMLMessageFormatter.XML_DOMAIN, "EncodingByteOrderUnsupported", new Object[] { encoding }, XMLErrorReporter.SEVERITY_FATAL_ERROR);
        }
    }
    // check for valid name  
    boolean validIANA = XMLChar.isValidIANAEncoding(encoding);
    boolean validJava = XMLChar.isValidJavaEncoding(encoding);
    if (!validIANA || (fAllowJavaEncodings && !validJava)) {
        fErrorReporter.reportError(XMLMessageFormatter.XML_DOMAIN, "EncodingDeclInvalid", new Object[] { encoding }, XMLErrorReporter.SEVERITY_FATAL_ERROR);
        // NOTE: AndyH suggested that, on failure, we use ISO Latin 1  
        //       because every byte is a valid ISO Latin 1 character.  
        //       It may not translate correctly but if we failed on  
        //       the encoding anyway, then we're expecting the content  
        //       of the document to be bad. This will just prevent an  
        //       invalid UTF-8 sequence to be detected. This is only  
        //       important when continue-after-fatal-error is turned  
        //       on. -Ac  
        return createLatin1Reader(inputStream);
    }
    // try to use a Java reader  
    String javaEncoding = EncodingMap.getIANA2JavaMapping(ENCODING);
    if (javaEncoding == null) {
        if (fAllowJavaEncodings) {
            javaEncoding = encoding;
        } else {
            fErrorReporter.reportError(XMLMessageFormatter.XML_DOMAIN, "EncodingDeclInvalid", new Object[] { encoding }, XMLErrorReporter.SEVERITY_FATAL_ERROR);
            // see comment above.  
            return createLatin1Reader(inputStream);
        }
    } else if (javaEncoding.equals("ASCII")) {
        return createASCIIReader(inputStream);
    } else if (javaEncoding.equals("ISO8859_1")) {
        return createLatin1Reader(inputStream);
    }
    if (DEBUG_ENCODINGS) {
        System.out.print("$$$ creating Java InputStreamReader: encoding=" + javaEncoding);
        if (javaEncoding == encoding) {
            System.out.print(" (IANA encoding)");
        }
        System.out.println();
    }
    return new InputStreamReader(inputStream, javaEncoding);
}
