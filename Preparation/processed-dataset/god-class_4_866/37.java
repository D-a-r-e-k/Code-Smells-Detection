// scanName():String 
/** Scans an entity reference. */
protected int scanEntityRef(final XMLStringBuffer str, final boolean content) throws IOException {
    str.clear();
    str.append('&');
    boolean endsWithSemicolon = false;
    while (true) {
        int c = fCurrentEntity.read();
        if (c == ';') {
            str.append(';');
            endsWithSemicolon = true;
            break;
        } else if (c == -1) {
            break;
        } else if (!ENTITY_CHARS.get(c) && c != '#') {
            fCurrentEntity.rewind();
            break;
        }
        str.append((char) c);
    }
    if (!endsWithSemicolon) {
        if (fReportErrors) {
            fErrorReporter.reportWarning("HTML1004", null);
        }
    }
    if (str.length == 1) {
        if (content && fDocumentHandler != null && fElementCount >= fElementDepth) {
            fEndLineNumber = fCurrentEntity.getLineNumber();
            fEndColumnNumber = fCurrentEntity.getColumnNumber();
            fEndCharacterOffset = fCurrentEntity.getCharacterOffset();
            fDocumentHandler.characters(str, locationAugs());
        }
        return -1;
    }
    final String name;
    if (endsWithSemicolon)
        name = str.toString().substring(1, str.length - 1);
    else
        name = str.toString().substring(1);
    if (name.startsWith("#")) {
        int value = -1;
        try {
            if (name.startsWith("#x") || name.startsWith("#X")) {
                value = Integer.parseInt(name.substring(2), 16);
            } else {
                value = Integer.parseInt(name.substring(1));
            }
            /* PATCH: Asgeir Asgeirsson */
            if (fFixWindowsCharRefs && fIso8859Encoding) {
                value = fixWindowsCharacter(value);
            }
            if (content && fDocumentHandler != null && fElementCount >= fElementDepth) {
                fEndLineNumber = fCurrentEntity.getLineNumber();
                fEndColumnNumber = fCurrentEntity.getColumnNumber();
                fEndCharacterOffset = fCurrentEntity.getCharacterOffset();
                if (fNotifyCharRefs) {
                    XMLResourceIdentifier id = resourceId();
                    String encoding = null;
                    fDocumentHandler.startGeneralEntity(name, id, encoding, locationAugs());
                }
                str.clear();
                str.append((char) value);
                fDocumentHandler.characters(str, locationAugs());
                if (fNotifyCharRefs) {
                    fDocumentHandler.endGeneralEntity(name, locationAugs());
                }
            }
        } catch (NumberFormatException e) {
            if (fReportErrors) {
                fErrorReporter.reportError("HTML1005", new Object[] { name });
            }
            if (content && fDocumentHandler != null && fElementCount >= fElementDepth) {
                fEndLineNumber = fCurrentEntity.getLineNumber();
                fEndColumnNumber = fCurrentEntity.getColumnNumber();
                fEndCharacterOffset = fCurrentEntity.getCharacterOffset();
                fDocumentHandler.characters(str, locationAugs());
            }
        }
        return value;
    }
    int c = HTMLEntities.get(name);
    // in attributes, some incomplete entities should be recognized, not all 
    // TODO: investigate to find which ones (there are differences between browsers) 
    // in a first time, consider only those that behave the same in FF and IE  
    final boolean invalidEntityInAttribute = !content && !endsWithSemicolon && c > 256;
    if (c == -1 || invalidEntityInAttribute) {
        if (fReportErrors) {
            fErrorReporter.reportWarning("HTML1006", new Object[] { name });
        }
        if (content && fDocumentHandler != null && fElementCount >= fElementDepth) {
            fEndLineNumber = fCurrentEntity.getLineNumber();
            fEndColumnNumber = fCurrentEntity.getColumnNumber();
            fEndCharacterOffset = fCurrentEntity.getCharacterOffset();
            fDocumentHandler.characters(str, locationAugs());
        }
        return -1;
    }
    if (content && fDocumentHandler != null && fElementCount >= fElementDepth) {
        fEndLineNumber = fCurrentEntity.getLineNumber();
        fEndColumnNumber = fCurrentEntity.getColumnNumber();
        fEndCharacterOffset = fCurrentEntity.getCharacterOffset();
        boolean notify = fNotifyHtmlBuiltinRefs || (fNotifyXmlBuiltinRefs && builtinXmlRef(name));
        if (notify) {
            XMLResourceIdentifier id = resourceId();
            String encoding = null;
            fDocumentHandler.startGeneralEntity(name, id, encoding, locationAugs());
        }
        str.clear();
        str.append((char) c);
        fDocumentHandler.characters(str, locationAugs());
        if (notify) {
            fDocumentHandler.endGeneralEntity(name, locationAugs());
        }
    }
    return c;
}
