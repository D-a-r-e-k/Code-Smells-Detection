//write  
/**
      *  Serialize the specified node as described above in the general
      * description of the <code>LSSerializer</code> interface. The output
      * is written to the supplied URI.
      * <br> When writing to a URI, the encoding is found by looking at the
      * encoding information that is reachable through the item to be written
      * (or its owner document) in this order:
      * <ol>
      * <li>
      * <code>Document.inputEncoding</code>,
      * </li>
      * <li>
      * <code>Document.xmlEncoding</code>.
      * </li>
      * </ol>
      * <br> If no encoding is reachable through the above properties, a
      * default encoding of "UTF-8" will be used.
      * <br> If the specified encoding is not supported an
      * "unsupported-encoding" error is raised.
      * @param node  The node to serialize.
      * @param URI The URI to write to.
      * @return  Returns <code>true</code> if <code>node</code> was
      *   successfully serialized and <code>false</code> in case the node
      *   couldn't be serialized.
      */
public boolean writeToURI(Node node, String URI) throws LSException {
    if (node == null) {
        return false;
    }
    XMLSerializer ser = null;
    String ver = _getXmlVersion(node);
    if (ver != null && ver.equals("1.1")) {
        if (xml11Serializer == null) {
            xml11Serializer = new XML11Serializer();
            initSerializer(xml11Serializer);
        }
        // copy setting from "main" serializer to XML 1.1 serializer  
        copySettings(serializer, xml11Serializer);
        ser = xml11Serializer;
    } else {
        ser = serializer;
    }
    String encoding = _getInputEncoding(node);
    if (encoding == null) {
        encoding = _getXmlEncoding(node);
        if (encoding == null) {
            encoding = "UTF-8";
        }
    }
    try {
        prepareForSerialization(ser, node);
        ser._format.setEncoding(encoding);
        ser.setOutputByteStream(XMLEntityManager.createOutputStream(URI));
        if (node.getNodeType() == Node.DOCUMENT_NODE)
            ser.serialize((Document) node);
        else if (node.getNodeType() == Node.DOCUMENT_FRAGMENT_NODE)
            ser.serialize((DocumentFragment) node);
        else if (node.getNodeType() == Node.ELEMENT_NODE)
            ser.serialize((Element) node);
        else
            return false;
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
