//copysettings  
/**
      *  Serialize the specified node as described above in the general
      * description of the <code>LSSerializer</code> interface. The output
      * is written to the supplied <code>LSOutput</code>.
      * <br> When writing to a <code>LSOutput</code>, the encoding is found by
      * looking at the encoding information that is reachable through the
      * <code>LSOutput</code> and the item to be written (or its owner
      * document) in this order:
      * <ol>
      * <li> <code>LSOutput.encoding</code>,
      * </li>
      * <li>
      * <code>Document.actualEncoding</code>,
      * </li>
      * <li>
      * <code>Document.xmlEncoding</code>.
      * </li>
      * </ol>
      * <br> If no encoding is reachable through the above properties, a
      * default encoding of "UTF-8" will be used.
      * <br> If the specified encoding is not supported an
      * "unsupported-encoding" error is raised.
      * <br> If no output is specified in the <code>LSOutput</code>, a
      * "no-output-specified" error is raised.
      * @param node  The node to serialize.
      * @param destination The destination for the serialized DOM.
      * @return  Returns <code>true</code> if <code>node</code> was
      *   successfully serialized and <code>false</code> in case the node
      *   couldn't be serialized.
      */
public boolean write(Node node, LSOutput destination) throws LSException {
    if (node == null)
        return false;
    XMLSerializer ser = null;
    String ver = _getXmlVersion(node);
    //determine which serializer to use:  
    if (ver != null && ver.equals("1.1")) {
        if (xml11Serializer == null) {
            xml11Serializer = new XML11Serializer();
            initSerializer(xml11Serializer);
        }
        //copy setting from "main" serializer to XML 1.1 serializer  
        copySettings(serializer, xml11Serializer);
        ser = xml11Serializer;
    } else {
        ser = serializer;
    }
    String encoding = null;
    if ((encoding = destination.getEncoding()) == null) {
        encoding = _getInputEncoding(node);
        if (encoding == null) {
            encoding = _getXmlEncoding(node);
            if (encoding == null) {
                encoding = "UTF-8";
            }
        }
    }
    try {
        prepareForSerialization(ser, node);
        ser._format.setEncoding(encoding);
        OutputStream outputStream = destination.getByteStream();
        Writer writer = destination.getCharacterStream();
        String uri = destination.getSystemId();
        if (writer == null) {
            if (outputStream == null) {
                if (uri == null) {
                    String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.SERIALIZER_DOMAIN, "no-output-specified", null);
                    if (ser.fDOMErrorHandler != null) {
                        DOMErrorImpl error = new DOMErrorImpl();
                        error.fType = "no-output-specified";
                        error.fMessage = msg;
                        error.fSeverity = DOMError.SEVERITY_FATAL_ERROR;
                        ser.fDOMErrorHandler.handleError(error);
                    }
                    throw new LSException(LSException.SERIALIZE_ERR, msg);
                } else {
                    ser.setOutputByteStream(XMLEntityManager.createOutputStream(uri));
                }
            } else {
                // byte stream was specified  
                ser.setOutputByteStream(outputStream);
            }
        } else {
            // character stream is specified  
            ser.setOutputCharStream(writer);
        }
        if (node.getNodeType() == Node.DOCUMENT_NODE)
            ser.serialize((Document) node);
        else if (node.getNodeType() == Node.DOCUMENT_FRAGMENT_NODE)
            ser.serialize((DocumentFragment) node);
        else if (node.getNodeType() == Node.ELEMENT_NODE)
            ser.serialize((Element) node);
        else
            return false;
    } catch (UnsupportedEncodingException ue) {
        if (ser.fDOMErrorHandler != null) {
            DOMErrorImpl error = new DOMErrorImpl();
            error.fException = ue;
            error.fType = "unsupported-encoding";
            error.fMessage = ue.getMessage();
            error.fSeverity = DOMError.SEVERITY_FATAL_ERROR;
            ser.fDOMErrorHandler.handleError(error);
        }
        throw new LSException(LSException.SERIALIZE_ERR, DOMMessageFormatter.formatMessage(DOMMessageFormatter.SERIALIZER_DOMAIN, "unsupported-encoding", null));
    } catch (LSException lse) {
        // Rethrow LSException.  
        throw lse;
    } catch (RuntimeException e) {
        if (e == DOMNormalizer.abort) {
            // stopped at user request  
            return false;
        }
        throw (LSException) DOMUtil.createLSException(LSException.SERIALIZE_ERR, e).fillInStackTrace();
    } catch (Exception e) {
        if (ser.fDOMErrorHandler != null) {
            DOMErrorImpl error = new DOMErrorImpl();
            error.fException = e;
            error.fMessage = e.getMessage();
            error.fSeverity = DOMError.SEVERITY_ERROR;
            ser.fDOMErrorHandler.handleError(error);
        }
        throw (LSException) DOMUtil.createLSException(LSException.SERIALIZE_ERR, e).fillInStackTrace();
    } finally {
        ser.clearDocumentState();
    }
    return true;
}
