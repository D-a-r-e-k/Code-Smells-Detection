void setProp(String name, String val, boolean defaultVal) {
    if (val != null) {
        char first = getFirstCharLocName(name);
        switch(first) {
            case 'c':
                if (OutputKeys.CDATA_SECTION_ELEMENTS.equals(name)) {
                    String cdataSectionNames = val;
                    addCdataSectionElements(cdataSectionNames);
                }
                break;
            case 'd':
                if (OutputKeys.DOCTYPE_SYSTEM.equals(name)) {
                    this.m_doctypeSystem = val;
                } else if (OutputKeys.DOCTYPE_PUBLIC.equals(name)) {
                    this.m_doctypePublic = val;
                    if (val.startsWith("-//W3C//DTD XHTML"))
                        m_spaceBeforeClose = true;
                }
                break;
            case 'e':
                String newEncoding = val;
                if (OutputKeys.ENCODING.equals(name)) {
                    String possible_encoding = Encodings.getMimeEncoding(val);
                    if (possible_encoding != null) {
                        // if the encoding is being set, try to get the  
                        // preferred  
                        // mime-name and set it too.  
                        super.setProp("mime-name", possible_encoding, defaultVal);
                    }
                    final String oldExplicitEncoding = getOutputPropertyNonDefault(OutputKeys.ENCODING);
                    final String oldDefaultEncoding = getOutputPropertyDefault(OutputKeys.ENCODING);
                    if ((defaultVal && (oldDefaultEncoding == null || !oldDefaultEncoding.equalsIgnoreCase(newEncoding))) || (!defaultVal && (oldExplicitEncoding == null || !oldExplicitEncoding.equalsIgnoreCase(newEncoding)))) {
                        // We are trying to change the default or the non-default setting of the encoding to a different value  
                        // from what it was  
                        EncodingInfo encodingInfo = Encodings.getEncodingInfo(newEncoding);
                        if (newEncoding != null && encodingInfo.name == null) {
                            // We tried to get an EncodingInfo for Object for the given  
                            // encoding, but it came back with an internall null name  
                            // so the encoding is not supported by the JDK, issue a message.  
                            final String msg = Utils.messages.createMessage(MsgKey.ER_ENCODING_NOT_SUPPORTED, new Object[] { newEncoding });
                            final String msg2 = "Warning: encoding \"" + newEncoding + "\" not supported, using " + Encodings.DEFAULT_MIME_ENCODING;
                            try {
                                // Prepare to issue the warning message  
                                final Transformer tran = super.getTransformer();
                                if (tran != null) {
                                    final ErrorListener errHandler = tran.getErrorListener();
                                    // Issue the warning message  
                                    if (null != errHandler && m_sourceLocator != null) {
                                        errHandler.warning(new TransformerException(msg, m_sourceLocator));
                                        errHandler.warning(new TransformerException(msg2, m_sourceLocator));
                                    } else {
                                        System.out.println(msg);
                                        System.out.println(msg2);
                                    }
                                } else {
                                    System.out.println(msg);
                                    System.out.println(msg2);
                                }
                            } catch (Exception e) {
                            }
                            // We said we are using UTF-8, so use it  
                            newEncoding = Encodings.DEFAULT_MIME_ENCODING;
                            val = Encodings.DEFAULT_MIME_ENCODING;
                            // to store the modified value into the properties a little later  
                            encodingInfo = Encodings.getEncodingInfo(newEncoding);
                        }
                        // The encoding was good, or was forced to UTF-8 above  
                        // If there is already a non-default set encoding and we   
                        // are trying to set the default encoding, skip the this block  
                        // as the non-default value is already the one to use.  
                        if (defaultVal == false || oldExplicitEncoding == null) {
                            m_encodingInfo = encodingInfo;
                            if (newEncoding != null)
                                m_isUTF8 = newEncoding.equals(Encodings.DEFAULT_MIME_ENCODING);
                            // if there was a previously set OutputStream  
                            OutputStream os = getOutputStream();
                            if (os != null) {
                                Writer w = getWriter();
                                // If the writer was previously set, but  
                                // set by the user, or if the new encoding is the same  
                                // as the old encoding, skip this block  
                                String oldEncoding = getOutputProperty(OutputKeys.ENCODING);
                                if ((w == null || !m_writer_set_by_user) && !newEncoding.equalsIgnoreCase(oldEncoding)) {
                                    // Make the change of encoding in our internal  
                                    // table, then call setOutputStreamInternal  
                                    // which will stomp on the old Writer (if any)  
                                    // with a new Writer with the new encoding.  
                                    super.setProp(name, val, defaultVal);
                                    setOutputStreamInternal(os, false);
                                }
                            }
                        }
                    }
                }
                break;
            case 'i':
                if (OutputPropertiesFactory.S_KEY_INDENT_AMOUNT.equals(name)) {
                    setIndentAmount(Integer.parseInt(val));
                } else if (OutputKeys.INDENT.equals(name)) {
                    boolean b = "yes".equals(val) ? true : false;
                    m_doIndent = b;
                }
                break;
            case 'l':
                if (OutputPropertiesFactory.S_KEY_LINE_SEPARATOR.equals(name)) {
                    m_lineSep = val.toCharArray();
                    m_lineSepLen = m_lineSep.length;
                }
                break;
            case 'm':
                if (OutputKeys.MEDIA_TYPE.equals(name)) {
                    m_mediatype = val;
                }
                break;
            case 'o':
                if (OutputKeys.OMIT_XML_DECLARATION.equals(name)) {
                    boolean b = "yes".equals(val) ? true : false;
                    this.m_shouldNotWriteXMLHeader = b;
                }
                break;
            case 's':
                // if standalone was explicitly specified  
                if (OutputKeys.STANDALONE.equals(name)) {
                    if (defaultVal) {
                        setStandaloneInternal(val);
                    } else {
                        m_standaloneWasSpecified = true;
                        setStandaloneInternal(val);
                    }
                }
                break;
            case 'v':
                if (OutputKeys.VERSION.equals(name)) {
                    m_version = val;
                }
                break;
            default:
                break;
        }
        super.setProp(name, val, defaultVal);
    }
}
