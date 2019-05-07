//-- startDocument  
/**
     * <p>ContentHandler#startElement</p>
     *
     * Signals the start of element.
     *
     * @param localName The name of the element.
     * @param atts The AttributeList containing the associated attributes for the element.
     */
public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws org.xml.sax.SAXException {
    if (LOG.isTraceEnabled()) {
        if ((qName != null) && (qName.length() > 0))
            LOG.trace("#startElement: " + qName);
        else
            LOG.trace("#startElement: " + localName);
    }
    //-- If we are skipping elements that have appeared in the XML but for  
    //-- which we have no mapping, increase the ignore depth counter and return  
    if ((!_strictElements) && (_ignoreElementDepth > 0)) {
        ++_ignoreElementDepth;
        return;
    }
    //-- if we are in an <any> section  
    //-- we delegate the event handling  
    if (_anyUnmarshaller != null) {
        _depth++;
        _anyUnmarshaller.startElement(namespaceURI, localName, qName, atts);
        return;
    }
    //-- Create a new namespace scope if necessary and  
    //-- make sure the flag is reset to true  
    if (_createNamespaceScope)
        _namespaces = _namespaces.createNamespaces();
    else
        _createNamespaceScope = true;
    if (_reusableAtts == null) {
        if (atts != null)
            _reusableAtts = new AttributeSetImpl(atts.getLength());
        else {
            //-- we can't pass a null AttributeSet to the  
            //-- startElement  
            _reusableAtts = new AttributeSetImpl();
        }
    } else {
        _reusableAtts.clear();
    }
    //-- process attributes  
    boolean hasQNameAtts = false;
    if ((atts != null) && (atts.getLength() > 0)) {
        //-- look for any potential namespace declarations  
        //-- in case namespace processing was disable  
        //-- on the parser  
        for (int i = 0; i < atts.getLength(); i++) {
            String attName = atts.getQName(i);
            if ((attName != null) && (attName.length() > 0)) {
                if (attName.equals(XMLNS)) {
                    _namespaces.addNamespace("", atts.getValue(i));
                } else if (attName.startsWith(XMLNS_PREFIX)) {
                    String prefix = attName.substring(XMLNS_PREFIX.length());
                    _namespaces.addNamespace(prefix, atts.getValue(i));
                } else {
                    //-- check for prefix  
                    if (attName.indexOf(':') < 0) {
                        _reusableAtts.setAttribute(attName, atts.getValue(i), atts.getURI(i));
                    } else
                        hasQNameAtts = true;
                }
            } else {
                //-- if attName is null or empty, just process as a normal  
                //-- attribute  
                attName = atts.getLocalName(i);
                if (XMLNS.equals(attName)) {
                    _namespaces.addNamespace("", atts.getValue(i));
                } else {
                    _reusableAtts.setAttribute(attName, atts.getValue(i), atts.getURI(i));
                }
            }
        }
    }
    //-- if we found any qName-only atts, process those  
    if (hasQNameAtts) {
        for (int i = 0; i < atts.getLength(); i++) {
            String attName = atts.getQName(i);
            if ((attName != null) && (attName.length() > 0)) {
                //-- process any non-namespace qName atts  
                if ((!attName.equals(XMLNS)) && (!attName.startsWith(XMLNS_PREFIX))) {
                    int idx = attName.indexOf(':');
                    if (idx >= 0) {
                        String prefix = attName.substring(0, idx);
                        attName = attName.substring(idx + 1);
                        String nsURI = atts.getURI(i);
                        if ((nsURI == null) || (nsURI.length() == 0)) {
                            nsURI = _namespaces.getNamespaceURI(prefix);
                        }
                        _reusableAtts.setAttribute(attName, atts.getValue(i), nsURI);
                    }
                }
            }
        }
    }
    //-- preserve parser passed arguments for any potential  
    //-- delegation  
    if (_elemInfo == null) {
        _elemInfo = new ElementInfo(null, atts);
    } else {
        _elemInfo.clear();
        _elemInfo._attributes = atts;
    }
    if ((localName == null) || (localName.length() == 0)) {
        if ((qName == null) || (qName.length() == 0)) {
            String error = "Missing either 'localName' or 'qName', both cannot be emtpy or null.";
            throw new SAXException(error);
        }
        localName = qName;
        _elemInfo._qName = qName;
    } else {
        if ((qName == null) || (qName.length() == 0)) {
            if ((namespaceURI == null) || (namespaceURI.length() == 0)) {
                _elemInfo._qName = localName;
            } else {
                String prefix = _namespaces.getNamespacePrefix(namespaceURI);
                if ((prefix != null) && (prefix.length() > 0)) {
                    _elemInfo._qName = prefix + ":" + localName;
                }
            }
        } else {
            _elemInfo._qName = qName;
        }
    }
    int idx = localName.indexOf(':');
    if (idx >= 0) {
        String prefix = localName.substring(0, idx);
        localName = localName.substring(idx + 1);
        if ((namespaceURI == null) || (namespaceURI.length() == 0)) {
            namespaceURI = _namespaces.getNamespaceURI(prefix);
        }
    } else {
        // check for default namespace declaration   
        String defaultNamespace = _namespaces.getNamespaceURI("");
        // TODO[WG]: remove unnecessary check as it simply is wrong  
        if (defaultNamespace != null && !defaultNamespace.equals("http://castor.exolab.org")) {
            namespaceURI = defaultNamespace;
        }
        //-- adjust empty namespace  
        if ((namespaceURI != null) && (namespaceURI.length() == 0))
            namespaceURI = null;
    }
    //-- call private startElement  
    startElement(localName, namespaceURI, _reusableAtts);
}
