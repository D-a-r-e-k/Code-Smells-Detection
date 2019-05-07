/**
   * Process the source tree to the output result.
   * @param source  The input for the source tree.
   *
   * @param outputTarget The output target.
   *
   * @throws TransformerException If an unrecoverable error occurs
   * during the course of the transformation.
   */
public void transform(Source source, Result outputTarget) throws TransformerException {
    createResultContentHandler(outputTarget);
    /*
     * According to JAXP1.2, new SAXSource()/StreamSource()
     * should create an empty input tree, with a default root node. 
     * new DOMSource()creates an empty document using DocumentBuilder.
     * newDocument(); Use DocumentBuilder.newDocument() for all 3 situations,
     * since there is no clear spec. how to create an empty tree when
     * both SAXSource() and StreamSource() are used.
     */
    if ((source instanceof StreamSource && source.getSystemId() == null && ((StreamSource) source).getInputStream() == null && ((StreamSource) source).getReader() == null) || (source instanceof SAXSource && ((SAXSource) source).getInputSource() == null && ((SAXSource) source).getXMLReader() == null) || (source instanceof DOMSource && ((DOMSource) source).getNode() == null)) {
        try {
            DocumentBuilderFactory builderF = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderF.newDocumentBuilder();
            String systemID = source.getSystemId();
            source = new DOMSource(builder.newDocument());
            // Copy system ID from original, empty Source to new Source  
            if (systemID != null) {
                source.setSystemId(systemID);
            }
        } catch (ParserConfigurationException e) {
            throw new TransformerException(e.getMessage());
        }
    }
    try {
        if (source instanceof DOMSource) {
            DOMSource dsource = (DOMSource) source;
            m_systemID = dsource.getSystemId();
            Node dNode = dsource.getNode();
            if (null != dNode) {
                try {
                    if (dNode.getNodeType() == Node.ATTRIBUTE_NODE)
                        this.startDocument();
                    try {
                        if (dNode.getNodeType() == Node.ATTRIBUTE_NODE) {
                            String data = dNode.getNodeValue();
                            char[] chars = data.toCharArray();
                            characters(chars, 0, chars.length);
                        } else {
                            org.apache.xml.serializer.TreeWalker walker;
                            walker = new org.apache.xml.serializer.TreeWalker(this, m_systemID);
                            walker.traverse(dNode);
                        }
                    } finally {
                        if (dNode.getNodeType() == Node.ATTRIBUTE_NODE)
                            this.endDocument();
                    }
                } catch (SAXException se) {
                    throw new TransformerException(se);
                }
                return;
            } else {
                String messageStr = XSLMessages.createMessage(XSLTErrorResources.ER_ILLEGAL_DOMSOURCE_INPUT, null);
                throw new IllegalArgumentException(messageStr);
            }
        }
        InputSource xmlSource = SAXSource.sourceToInputSource(source);
        if (null == xmlSource) {
            throw new TransformerException(XSLMessages.createMessage(XSLTErrorResources.ER_CANNOT_TRANSFORM_SOURCE_TYPE, new Object[] { source.getClass().getName() }));
        }
        if (null != xmlSource.getSystemId())
            m_systemID = xmlSource.getSystemId();
        XMLReader reader = null;
        boolean managedReader = false;
        try {
            if (source instanceof SAXSource) {
                reader = ((SAXSource) source).getXMLReader();
            }
            if (null == reader) {
                try {
                    reader = XMLReaderManager.getInstance().getXMLReader();
                    managedReader = true;
                } catch (SAXException se) {
                    throw new TransformerException(se);
                }
            } else {
                try {
                    reader.setFeature("http://xml.org/sax/features/namespace-prefixes", true);
                } catch (org.xml.sax.SAXException se) {
                }
            }
            // Get the input content handler, which will handle the   
            // parse events and create the source tree.   
            ContentHandler inputHandler = this;
            reader.setContentHandler(inputHandler);
            if (inputHandler instanceof org.xml.sax.DTDHandler)
                reader.setDTDHandler((org.xml.sax.DTDHandler) inputHandler);
            try {
                if (inputHandler instanceof org.xml.sax.ext.LexicalHandler)
                    reader.setProperty("http://xml.org/sax/properties/lexical-handler", inputHandler);
                if (inputHandler instanceof org.xml.sax.ext.DeclHandler)
                    reader.setProperty("http://xml.org/sax/properties/declaration-handler", inputHandler);
            } catch (org.xml.sax.SAXException se) {
            }
            try {
                if (inputHandler instanceof org.xml.sax.ext.LexicalHandler)
                    reader.setProperty("http://xml.org/sax/handlers/LexicalHandler", inputHandler);
                if (inputHandler instanceof org.xml.sax.ext.DeclHandler)
                    reader.setProperty("http://xml.org/sax/handlers/DeclHandler", inputHandler);
            } catch (org.xml.sax.SAXNotRecognizedException snre) {
            }
            reader.parse(xmlSource);
        } catch (org.apache.xml.utils.WrappedRuntimeException wre) {
            Throwable throwable = wre.getException();
            while (throwable instanceof org.apache.xml.utils.WrappedRuntimeException) {
                throwable = ((org.apache.xml.utils.WrappedRuntimeException) throwable).getException();
            }
            throw new TransformerException(wre.getException());
        } catch (org.xml.sax.SAXException se) {
            throw new TransformerException(se);
        } catch (IOException ioe) {
            throw new TransformerException(ioe);
        } finally {
            if (managedReader) {
                XMLReaderManager.getInstance().releaseXMLReader(reader);
            }
        }
    } finally {
        if (null != m_outputStream) {
            try {
                m_outputStream.close();
            } catch (IOException ioe) {
            }
            m_outputStream = null;
        }
    }
}
