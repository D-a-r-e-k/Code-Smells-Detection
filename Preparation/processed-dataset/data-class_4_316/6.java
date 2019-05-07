/**
     * DOM L3 EXPERIMENTAL:
     *  Serialize the specified node as described above in the description of
     * <code>LSSerializer</code>. The result of serializing the node is
     * returned as a string. Writing a Document or Entity node produces a
     * serialized form that is well formed XML. Writing other node types
     * produces a fragment of text in a form that is not fully defined by
     * this document, but that should be useful to a human for debugging or
     * diagnostic purposes.
     * @param wnode  The node to be written.
     * @return  Returns the serialized data
     * @exception DOMException
     *    DOMSTRING_SIZE_ERR: The resulting string is too long to fit in a
     *   <code>DOMString</code>.
     * @exception LSException
     *    SERIALIZE_ERR: Unable to serialize the node.  DOM applications should
     *    attach a <code>DOMErrorHandler</code> using the parameter 
     *    &quot;<i>error-handler</i>&quot; to get details on error.
     */
public String writeToString(Node wnode) throws DOMException, LSException {
    // determine which serializer to use:  
    XMLSerializer ser = null;
    String ver = _getXmlVersion(wnode);
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
    StringWriter destination = new StringWriter();
    try {
        prepareForSerialization(ser, wnode);
        ser._format.setEncoding("UTF-16");
        ser.setOutputCharStream(destination);
        if (wnode.getNodeType() == Node.DOCUMENT_NODE) {
            ser.serialize((Document) wnode);
        } else if (wnode.getNodeType() == Node.DOCUMENT_FRAGMENT_NODE) {
            ser.serialize((DocumentFragment) wnode);
        } else if (wnode.getNodeType() == Node.ELEMENT_NODE) {
            ser.serialize((Element) wnode);
        } else {
            String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.SERIALIZER_DOMAIN, "unable-to-serialize-node", null);
            if (ser.fDOMErrorHandler != null) {
                DOMErrorImpl error = new DOMErrorImpl();
                error.fType = "unable-to-serialize-node";
                error.fMessage = msg;
                error.fSeverity = DOMError.SEVERITY_FATAL_ERROR;
                ser.fDOMErrorHandler.handleError(error);
            }
            throw new LSException(LSException.SERIALIZE_ERR, msg);
        }
    } catch (LSException lse) {
        // Rethrow LSException.  
        throw lse;
    } catch (RuntimeException e) {
        if (e == DOMNormalizer.abort) {
            // stopped at user request  
            return null;
        }
        throw (LSException) DOMUtil.createLSException(LSException.SERIALIZE_ERR, e).fillInStackTrace();
    } catch (IOException ioe) {
        // REVISIT: A generic IOException doesn't provide enough information  
        // to determine that the serialized document is too large to fit  
        // into a string. This could have thrown for some other reason. -- mrglavas  
        String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "STRING_TOO_LONG", new Object[] { ioe.getMessage() });
        throw new DOMException(DOMException.DOMSTRING_SIZE_ERR, msg);
    } finally {
        ser.clearDocumentState();
    }
    return destination.toString();
}
