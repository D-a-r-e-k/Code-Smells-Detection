// scanMarkupContent(XMLStringBuffer,char):boolean 
/** Scans a processing instruction. */
protected void scanPI() throws IOException {
    fCurrentEntity.debugBufferIfNeeded("(scanPI: ");
    if (fReportErrors) {
        fErrorReporter.reportWarning("HTML1008", null);
    }
    // scan processing instruction 
    String target = scanName();
    if (target != null && !target.equalsIgnoreCase("xml")) {
        while (true) {
            int c = fCurrentEntity.read();
            if (c == '\r' || c == '\n') {
                if (c == '\r') {
                    c = fCurrentEntity.read();
                    if (c != '\n') {
                        fCurrentEntity.offset--;
                        fCurrentEntity.characterOffset_--;
                    }
                }
                fCurrentEntity.incLine();
                continue;
            }
            if (c == -1) {
                break;
            }
            if (c != ' ' && c != '\t') {
                fCurrentEntity.rewind();
                break;
            }
        }
        fStringBuffer.clear();
        while (true) {
            int c = fCurrentEntity.read();
            if (c == '?' || c == '/') {
                char c0 = (char) c;
                c = fCurrentEntity.read();
                if (c == '>') {
                    break;
                } else {
                    fStringBuffer.append(c0);
                    fCurrentEntity.rewind();
                    continue;
                }
            } else if (c == '\r' || c == '\n') {
                fStringBuffer.append('\n');
                if (c == '\r') {
                    c = fCurrentEntity.read();
                    if (c != '\n') {
                        fCurrentEntity.offset--;
                        fCurrentEntity.characterOffset_--;
                    }
                }
                fCurrentEntity.incLine();
                continue;
            } else if (c == -1) {
                break;
            } else {
                fStringBuffer.append((char) c);
            }
        }
        XMLString data = fStringBuffer;
        if (fDocumentHandler != null) {
            fEndLineNumber = fCurrentEntity.getLineNumber();
            fEndColumnNumber = fCurrentEntity.getColumnNumber();
            fEndCharacterOffset = fCurrentEntity.getCharacterOffset();
            fDocumentHandler.processingInstruction(target, data, locationAugs());
        }
    } else {
        int beginLineNumber = fBeginLineNumber;
        int beginColumnNumber = fBeginColumnNumber;
        int beginCharacterOffset = fBeginCharacterOffset;
        fAttributes.removeAllAttributes();
        int aindex = 0;
        while (scanPseudoAttribute(fAttributes)) {
            // if we haven't scanned a value, remove the entry as values have special signification 
            if (fAttributes.getValue(aindex).length() == 0) {
                fAttributes.removeAttributeAt(aindex);
            } else {
                fAttributes.getName(aindex, fQName);
                fQName.rawname = fQName.rawname.toLowerCase();
                fAttributes.setName(aindex, fQName);
                aindex++;
            }
        }
        if (fDocumentHandler != null) {
            String version = fAttributes.getValue("version");
            String encoding = fAttributes.getValue("encoding");
            String standalone = fAttributes.getValue("standalone");
            // if the encoding is successfully changed, the stream will be processed again 
            // with the right encoding an we will come here again but without need to change the encoding 
            final boolean xmlDeclNow = fIgnoreSpecifiedCharset || !changeEncoding(encoding);
            if (xmlDeclNow) {
                fBeginLineNumber = beginLineNumber;
                fBeginColumnNumber = beginColumnNumber;
                fBeginCharacterOffset = beginCharacterOffset;
                fEndLineNumber = fCurrentEntity.getLineNumber();
                fEndColumnNumber = fCurrentEntity.getColumnNumber();
                fEndCharacterOffset = fCurrentEntity.getCharacterOffset();
                fDocumentHandler.xmlDecl(version, encoding, standalone, locationAugs());
            }
        }
    }
    fCurrentEntity.debugBufferIfNeeded(")scanPI: ");
}
