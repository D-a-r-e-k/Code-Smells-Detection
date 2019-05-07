// startEntity(String,XMLInputSource)  
/**
     * This method uses the passed-in XMLInputSource to make 
     * fCurrentEntity usable for reading.
     * @param name  name of the entity (XML is it's the document entity)
     * @param xmlInputSource    the input source, with sufficient information
     *      to begin scanning characters.
     * @param literal        True if this entity is started within a
     *                       literal value.
     * @param isExternal    whether this entity should be treated as an internal or external entity.
     * @throws IOException  if anything can't be read
     *  XNIException    If any parser-specific goes wrong.
     * @return the encoding of the new entity or null if a character stream was employed
     */
public String setupCurrentEntity(String name, XMLInputSource xmlInputSource, boolean literal, boolean isExternal) throws IOException, XNIException {
    // get information  
    final String publicId = xmlInputSource.getPublicId();
    String literalSystemId = xmlInputSource.getSystemId();
    String baseSystemId = xmlInputSource.getBaseSystemId();
    String encoding = xmlInputSource.getEncoding();
    final boolean encodingExternallySpecified = (encoding != null);
    Boolean isBigEndian = null;
    fTempByteBuffer = null;
    // create reader  
    InputStream stream = null;
    Reader reader = xmlInputSource.getCharacterStream();
    // First chance checking strict URI  
    String expandedSystemId = expandSystemId(literalSystemId, baseSystemId, fStrictURI);
    if (baseSystemId == null) {
        baseSystemId = expandedSystemId;
    }
    if (reader == null) {
        stream = xmlInputSource.getByteStream();
        if (stream == null) {
            URL location = new URL(expandedSystemId);
            URLConnection connect = location.openConnection();
            if (!(connect instanceof HttpURLConnection)) {
                stream = connect.getInputStream();
            } else {
                boolean followRedirects = true;
                // setup URLConnection if we have an HTTPInputSource  
                if (xmlInputSource instanceof HTTPInputSource) {
                    final HttpURLConnection urlConnection = (HttpURLConnection) connect;
                    final HTTPInputSource httpInputSource = (HTTPInputSource) xmlInputSource;
                    // set request properties  
                    Iterator propIter = httpInputSource.getHTTPRequestProperties();
                    while (propIter.hasNext()) {
                        Map.Entry entry = (Map.Entry) propIter.next();
                        urlConnection.setRequestProperty((String) entry.getKey(), (String) entry.getValue());
                    }
                    // set preference for redirection  
                    followRedirects = httpInputSource.getFollowHTTPRedirects();
                    if (!followRedirects) {
                        urlConnection.setInstanceFollowRedirects(followRedirects);
                    }
                }
                stream = connect.getInputStream();
                // REVISIT: If the URLConnection has external encoding  
                // information, we should be reading it here. It's located  
                // in the charset parameter of Content-Type. -- mrglavas  
                if (followRedirects) {
                    String redirect = connect.getURL().toString();
                    // E43: Check if the URL was redirected, and then  
                    // update literal and expanded system IDs if needed.  
                    if (!redirect.equals(expandedSystemId)) {
                        literalSystemId = redirect;
                        expandedSystemId = redirect;
                    }
                }
            }
        }
        // wrap this stream in RewindableInputStream  
        RewindableInputStream rewindableStream = new RewindableInputStream(stream);
        stream = rewindableStream;
        // perform auto-detect of encoding if necessary  
        if (encoding == null) {
            // read first four bytes and determine encoding  
            final byte[] b4 = new byte[4];
            int count = 0;
            for (; count < 4; count++) {
                b4[count] = (byte) rewindableStream.readAndBuffer();
            }
            if (count == 4) {
                EncodingInfo info = getEncodingInfo(b4, count);
                encoding = info.encoding;
                isBigEndian = info.isBigEndian;
                stream.reset();
                if (info.hasBOM) {
                    // Special case UTF-8 files with BOM created by Microsoft  
                    // tools. It's more efficient to consume the BOM than make  
                    // the reader perform extra checks. -Ac  
                    if (encoding == "UTF-8") {
                        // UTF-8 BOM: 0xEF 0xBB 0xBF  
                        stream.skip(3);
                    } else if (encoding == "UTF-16") {
                        // UTF-16 BE BOM: 0xFE 0xFF   
                        // UTF-16 LE BOM: 0xFF 0xFE  
                        stream.skip(2);
                    }
                }
                reader = createReader(stream, encoding, isBigEndian);
            } else {
                reader = createReader(stream, encoding, isBigEndian);
            }
        } else {
            encoding = encoding.toUpperCase(Locale.ENGLISH);
            // If encoding is UTF-8, consume BOM if one is present.  
            if (encoding.equals("UTF-8")) {
                final int[] b3 = new int[3];
                int count = 0;
                for (; count < 3; ++count) {
                    b3[count] = rewindableStream.readAndBuffer();
                    if (b3[count] == -1)
                        break;
                }
                if (count == 3) {
                    if (b3[0] != 0xEF || b3[1] != 0xBB || b3[2] != 0xBF) {
                        // First three bytes are not BOM, so reset.  
                        stream.reset();
                    }
                } else {
                    stream.reset();
                }
                reader = createReader(stream, "UTF-8", isBigEndian);
            } else if (encoding.equals("UTF-16")) {
                final int[] b4 = new int[4];
                int count = 0;
                for (; count < 4; ++count) {
                    b4[count] = rewindableStream.readAndBuffer();
                    if (b4[count] == -1)
                        break;
                }
                stream.reset();
                if (count >= 2) {
                    final int b0 = b4[0];
                    final int b1 = b4[1];
                    if (b0 == 0xFE && b1 == 0xFF) {
                        // UTF-16, big-endian  
                        isBigEndian = Boolean.TRUE;
                        stream.skip(2);
                    } else if (b0 == 0xFF && b1 == 0xFE) {
                        // UTF-16, little-endian  
                        isBigEndian = Boolean.FALSE;
                        stream.skip(2);
                    } else if (count == 4) {
                        final int b2 = b4[2];
                        final int b3 = b4[3];
                        if (b0 == 0x00 && b1 == 0x3C && b2 == 0x00 && b3 == 0x3F) {
                            // UTF-16, big-endian, no BOM  
                            isBigEndian = Boolean.TRUE;
                        }
                        if (b0 == 0x3C && b1 == 0x00 && b2 == 0x3F && b3 == 0x00) {
                            // UTF-16, little-endian, no BOM  
                            isBigEndian = Boolean.FALSE;
                        }
                    }
                }
                reader = createReader(stream, "UTF-16", isBigEndian);
            } else if (encoding.equals("ISO-10646-UCS-4")) {
                final int[] b4 = new int[4];
                int count = 0;
                for (; count < 4; ++count) {
                    b4[count] = rewindableStream.readAndBuffer();
                    if (b4[count] == -1)
                        break;
                }
                stream.reset();
                // Ignore unusual octet order for now.  
                if (count == 4) {
                    // UCS-4, big endian (1234)  
                    if (b4[0] == 0x00 && b4[1] == 0x00 && b4[2] == 0x00 && b4[3] == 0x3C) {
                        isBigEndian = Boolean.TRUE;
                    } else if (b4[0] == 0x3C && b4[1] == 0x00 && b4[2] == 0x00 && b4[3] == 0x00) {
                        isBigEndian = Boolean.FALSE;
                    }
                }
                reader = createReader(stream, encoding, isBigEndian);
            } else if (encoding.equals("ISO-10646-UCS-2")) {
                final int[] b4 = new int[4];
                int count = 0;
                for (; count < 4; ++count) {
                    b4[count] = rewindableStream.readAndBuffer();
                    if (b4[count] == -1)
                        break;
                }
                stream.reset();
                if (count == 4) {
                    // UCS-2, big endian  
                    if (b4[0] == 0x00 && b4[1] == 0x3C && b4[2] == 0x00 && b4[3] == 0x3F) {
                        isBigEndian = Boolean.TRUE;
                    } else if (b4[0] == 0x3C && b4[1] == 0x00 && b4[2] == 0x3F && b4[3] == 0x00) {
                        isBigEndian = Boolean.FALSE;
                    }
                }
                reader = createReader(stream, encoding, isBigEndian);
            } else {
                reader = createReader(stream, encoding, isBigEndian);
            }
        }
        // read one character at a time so we don't jump too far  
        // ahead, converting characters from the byte stream in  
        // the wrong encoding  
        if (DEBUG_ENCODINGS) {
            System.out.println("$$$ no longer wrapping reader in OneCharReader");
        }
    }
    // We've seen a new Reader.  
    // Push it on the stack so we can close it later.  
    fReaderStack.push(reader);
    // push entity on stack  
    if (fCurrentEntity != null) {
        fEntityStack.push(fCurrentEntity);
    }
    // create entity  
    fCurrentEntity = new ScannedEntity(name, new XMLResourceIdentifierImpl(publicId, literalSystemId, baseSystemId, expandedSystemId), stream, reader, fTempByteBuffer, encoding, literal, false, isExternal);
    fCurrentEntity.setEncodingExternallySpecified(encodingExternallySpecified);
    fEntityScanner.setCurrentEntity(fCurrentEntity);
    fResourceIdentifier.setValues(publicId, literalSystemId, baseSystemId, expandedSystemId);
    return encoding;
}
