// processingInstruction(String,XMLString,Augmentations) 
/** Start element. */
public void startElement(final QName elem, XMLAttributes attrs, final Augmentations augs) throws XNIException {
    fSeenAnything = true;
    final boolean isForcedCreation = forcedStartElement_;
    forcedStartElement_ = false;
    // check for end of document 
    if (fSeenRootElementEnd) {
        notifyDiscardedStartElement(elem, attrs, augs);
        return;
    }
    // get element information 
    final HTMLElements.Element element = getElement(elem);
    final short elementCode = element.code;
    // the creation of some elements like TABLE or SELECT can't be forced. Any others?  
    if (isForcedCreation && (elementCode == HTMLElements.TABLE || elementCode == HTMLElements.SELECT)) {
        return;
    }
    // ignore multiple html, head, body elements 
    if (fSeenRootElement && elementCode == HTMLElements.HTML) {
        notifyDiscardedStartElement(elem, attrs, augs);
        return;
    }
    if (elementCode == HTMLElements.HEAD) {
        if (fSeenHeadElement) {
            notifyDiscardedStartElement(elem, attrs, augs);
            return;
        }
        fSeenHeadElement = true;
    } else if (elementCode == HTMLElements.FRAMESET) {
        consumeBufferedEndElements();
    } else if (elementCode == HTMLElements.BODY) {
        // create <head></head> if none was present 
        if (!fSeenHeadElement) {
            final QName head = createQName("head");
            forceStartElement(head, null, synthesizedAugs());
            endElement(head, synthesizedAugs());
        }
        consumeBufferedEndElements();
        // </head> (if any) has been buffered 
        if (fSeenBodyElement) {
            notifyDiscardedStartElement(elem, attrs, augs);
            return;
        }
        fSeenBodyElement = true;
    } else if (elementCode == HTMLElements.FORM) {
        if (fOpenedForm) {
            notifyDiscardedStartElement(elem, attrs, augs);
            return;
        }
        fOpenedForm = true;
    } else if (elementCode == HTMLElements.UNKNOWN) {
        consumeBufferedEndElements();
    }
    // check proper parent 
    if (element.parent != null) {
        final HTMLElements.Element preferedParent = element.parent[0];
        if (fDocumentFragment && (preferedParent.code == HTMLElements.HEAD || preferedParent.code == HTMLElements.BODY)) {
        } else if (!fSeenRootElement && !fDocumentFragment) {
            String pname = preferedParent.name;
            pname = modifyName(pname, fNamesElems);
            if (fReportErrors) {
                String ename = elem.rawname;
                fErrorReporter.reportWarning("HTML2002", new Object[] { ename, pname });
            }
            final QName qname = new QName(null, pname, pname, null);
            final boolean parentCreated = forceStartElement(qname, null, synthesizedAugs());
            if (!parentCreated) {
                if (!isForcedCreation) {
                    notifyDiscardedStartElement(elem, attrs, augs);
                }
                return;
            }
        } else {
            if (preferedParent.code != HTMLElements.HEAD || (!fSeenBodyElement && !fDocumentFragment)) {
                int depth = getParentDepth(element.parent, element.bounds);
                if (depth == -1) {
                    // no parent found 
                    final String pname = modifyName(preferedParent.name, fNamesElems);
                    final QName qname = new QName(null, pname, pname, null);
                    if (fReportErrors) {
                        String ename = elem.rawname;
                        fErrorReporter.reportWarning("HTML2004", new Object[] { ename, pname });
                    }
                    final boolean parentCreated = forceStartElement(qname, null, synthesizedAugs());
                    if (!parentCreated) {
                        if (!isForcedCreation) {
                            notifyDiscardedStartElement(elem, attrs, augs);
                        }
                        return;
                    }
                }
            }
        }
    }
    // if block element, save immediate parent inline elements 
    int depth = 0;
    if (element.flags == 0) {
        int length = fElementStack.top;
        fInlineStack.top = 0;
        for (int i = length - 1; i >= 0; i--) {
            Info info = fElementStack.data[i];
            if (!info.element.isInline()) {
                break;
            }
            fInlineStack.push(info);
            endElement(info.qname, synthesizedAugs());
        }
        depth = fInlineStack.top;
    }
    // close previous elements 
    // all elements close a <script> 
    // in head, no element has children 
    if ((fElementStack.top > 1 && (fElementStack.peek().element.code == HTMLElements.SCRIPT)) || fElementStack.top > 2 && fElementStack.data[fElementStack.top - 2].element.code == HTMLElements.HEAD) {
        final Info info = fElementStack.pop();
        if (fDocumentHandler != null) {
            callEndElement(info.qname, synthesizedAugs());
        }
    }
    if (element.closes != null) {
        int length = fElementStack.top;
        for (int i = length - 1; i >= 0; i--) {
            Info info = fElementStack.data[i];
            // does it close the element we're looking at? 
            if (element.closes(info.element.code)) {
                if (fReportErrors) {
                    String ename = elem.rawname;
                    String iname = info.qname.rawname;
                    fErrorReporter.reportWarning("HTML2005", new Object[] { ename, iname });
                }
                for (int j = length - 1; j >= i; j--) {
                    info = fElementStack.pop();
                    if (fDocumentHandler != null) {
                        // PATCH: Marc-Andr� Morissette 
                        callEndElement(info.qname, synthesizedAugs());
                    }
                }
                length = i;
                continue;
            }
            // should we stop searching? 
            if (info.element.isBlock() || element.isParent(info.element)) {
                break;
            }
        }
    } else if (elementCode == HTMLElements.TABLE) {
        for (int i = fElementStack.top - 1; i >= 0; i--) {
            final Info info = fElementStack.data[i];
            if (!info.element.isInline()) {
                break;
            }
            endElement(info.qname, synthesizedAugs());
        }
    }
    // call handler 
    fSeenRootElement = true;
    if (element != null && element.isEmpty()) {
        if (attrs == null) {
            attrs = emptyAttributes();
        }
        if (fDocumentHandler != null) {
            fDocumentHandler.emptyElement(elem, attrs, augs);
        }
    } else {
        boolean inline = element != null && element.isInline();
        fElementStack.push(new Info(element, elem, inline ? attrs : null));
        if (attrs == null) {
            attrs = emptyAttributes();
        }
        if (fDocumentHandler != null) {
            callStartElement(elem, attrs, augs);
        }
    }
    // re-open inline elements 
    for (int i = 0; i < depth; i++) {
        Info info = fInlineStack.pop();
        forceStartElement(info.qname, info.attributes, synthesizedAugs());
    }
    if (elementCode == HTMLElements.BODY) {
        lostText_.refeed(this);
    }
}
