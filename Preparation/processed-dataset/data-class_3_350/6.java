/**
   * Create a result ContentHandler from a Result object, based
   * on the current OutputProperties.
   *
   * @param outputTarget Where the transform result should go,
   * should not be null.
   *
   * @return A valid ContentHandler that will create the
   * result tree when it is fed SAX events.
   *
   * @throws TransformerException
   */
private void createResultContentHandler(Result outputTarget) throws TransformerException {
    if (outputTarget instanceof SAXResult) {
        SAXResult saxResult = (SAXResult) outputTarget;
        m_resultContentHandler = saxResult.getHandler();
        m_resultLexicalHandler = saxResult.getLexicalHandler();
        if (m_resultContentHandler instanceof Serializer) {
            // Dubious but needed, I think.  
            m_serializer = (Serializer) m_resultContentHandler;
        }
    } else if (outputTarget instanceof DOMResult) {
        DOMResult domResult = (DOMResult) outputTarget;
        Node outputNode = domResult.getNode();
        Node nextSibling = domResult.getNextSibling();
        Document doc;
        short type;
        if (null != outputNode) {
            type = outputNode.getNodeType();
            doc = (Node.DOCUMENT_NODE == type) ? (Document) outputNode : outputNode.getOwnerDocument();
        } else {
            try {
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                dbf.setNamespaceAware(true);
                if (m_isSecureProcessing) {
                    try {
                        dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
                    } catch (ParserConfigurationException pce) {
                    }
                }
                DocumentBuilder db = dbf.newDocumentBuilder();
                doc = db.newDocument();
            } catch (ParserConfigurationException pce) {
                throw new TransformerException(pce);
            }
            outputNode = doc;
            type = outputNode.getNodeType();
            ((DOMResult) outputTarget).setNode(outputNode);
        }
        DOMBuilder domBuilder = (Node.DOCUMENT_FRAGMENT_NODE == type) ? new DOMBuilder(doc, (DocumentFragment) outputNode) : new DOMBuilder(doc, outputNode);
        if (nextSibling != null)
            domBuilder.setNextSibling(nextSibling);
        m_resultContentHandler = domBuilder;
        m_resultLexicalHandler = domBuilder;
    } else if (outputTarget instanceof StreamResult) {
        StreamResult sresult = (StreamResult) outputTarget;
        try {
            Serializer serializer = SerializerFactory.getSerializer(m_outputFormat.getProperties());
            m_serializer = serializer;
            if (null != sresult.getWriter())
                serializer.setWriter(sresult.getWriter());
            else if (null != sresult.getOutputStream())
                serializer.setOutputStream(sresult.getOutputStream());
            else if (null != sresult.getSystemId()) {
                String fileURL = sresult.getSystemId();
                if (fileURL.startsWith("file:///")) {
                    if (fileURL.substring(8).indexOf(":") > 0) {
                        fileURL = fileURL.substring(8);
                    } else {
                        fileURL = fileURL.substring(7);
                    }
                } else if (fileURL.startsWith("file:/")) {
                    if (fileURL.substring(6).indexOf(":") > 0) {
                        fileURL = fileURL.substring(6);
                    } else {
                        fileURL = fileURL.substring(5);
                    }
                }
                m_outputStream = new java.io.FileOutputStream(fileURL);
                serializer.setOutputStream(m_outputStream);
            } else
                throw new TransformerException(XSLMessages.createMessage(XSLTErrorResources.ER_NO_OUTPUT_SPECIFIED, null));
            //"No output specified!");  
            m_resultContentHandler = serializer.asContentHandler();
        } catch (IOException ioe) {
            throw new TransformerException(ioe);
        }
    } else {
        throw new TransformerException(XSLMessages.createMessage(XSLTErrorResources.ER_CANNOT_TRANSFORM_TO_RESULT_TYPE, new Object[] { outputTarget.getClass().getName() }));
    }
    if (m_resultContentHandler instanceof DTDHandler)
        m_resultDTDHandler = (DTDHandler) m_resultContentHandler;
    if (m_resultContentHandler instanceof DeclHandler)
        m_resultDeclHandler = (DeclHandler) m_resultContentHandler;
    if (m_resultContentHandler instanceof LexicalHandler)
        m_resultLexicalHandler = (LexicalHandler) m_resultContentHandler;
}
