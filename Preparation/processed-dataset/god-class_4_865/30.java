// ignorableWhitespace(XMLString,Augmentations) 
/** End element. */
public void endElement(final QName element, final Augmentations augs) throws XNIException {
    final boolean forcedEndElement = forcedEndElement_;
    // is there anything to do? 
    if (fSeenRootElementEnd) {
        notifyDiscardedEndElement(element, augs);
        return;
    }
    // get element information 
    HTMLElements.Element elem = getElement(element);
    // if we consider outside content, just buffer </body> and </html> to consider them at the very end 
    if (!fIgnoreOutsideContent && (elem.code == HTMLElements.BODY || elem.code == HTMLElements.HTML)) {
        endElementsBuffer_.add(new ElementEntry(element, augs));
        return;
    }
    // check for end of document 
    if (elem.code == HTMLElements.HTML) {
        fSeenRootElementEnd = true;
    } else if (elem.code == HTMLElements.FORM) {
        fOpenedForm = false;
    } else if (elem.code == HTMLElements.HEAD && !forcedEndElement) {
        // consume </head> first when <body> is reached to retrieve content lost between </head> and <body> 
        endElementsBuffer_.add(new ElementEntry(element, augs));
        return;
    }
    // empty element 
    int depth = getElementDepth(elem);
    if (depth == -1) {
        if (elem.code == HTMLElements.P) {
            forceStartElement(element, emptyAttributes(), synthesizedAugs());
            endElement(element, augs);
        } else if (!elem.isEmpty()) {
            notifyDiscardedEndElement(element, augs);
        }
        return;
    }
    // find unbalanced inline elements 
    if (depth > 1 && elem.isInline()) {
        final int size = fElementStack.top;
        fInlineStack.top = 0;
        for (int i = 0; i < depth - 1; i++) {
            final Info info = fElementStack.data[size - i - 1];
            final HTMLElements.Element pelem = info.element;
            if (pelem.isInline() || pelem.code == HTMLElements.FONT) {
                // TODO: investigate if only FONT 
                // NOTE: I don't have to make a copy of the info because 
                //       it will just be popped off of the element stack 
                //       as soon as we close it, anyway. 
                fInlineStack.push(info);
            }
        }
    }
    // close children up to appropriate element 
    for (int i = 0; i < depth; i++) {
        Info info = fElementStack.pop();
        if (fReportErrors && i < depth - 1) {
            String ename = modifyName(element.rawname, fNamesElems);
            String iname = info.qname.rawname;
            fErrorReporter.reportWarning("HTML2007", new Object[] { ename, iname });
        }
        if (fDocumentHandler != null) {
            // PATCH: Marc-Andr� Morissette 
            callEndElement(info.qname, i < depth - 1 ? synthesizedAugs() : augs);
        }
    }
    // re-open inline elements 
    if (depth > 1) {
        int size = fInlineStack.top;
        for (int i = 0; i < size; i++) {
            Info info = (Info) fInlineStack.pop();
            XMLAttributes attributes = info.attributes;
            if (fReportErrors) {
                String iname = info.qname.rawname;
                fErrorReporter.reportWarning("HTML2008", new Object[] { iname });
            }
            forceStartElement(info.qname, attributes, synthesizedAugs());
        }
    }
}
