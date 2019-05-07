// scanPseudoAttribute(XMLAttributesImpl):boolean 
/** 
         * Scans an attribute, pseudo or real. 
         *
         * @param attributes The list of attributes.
         * @param empty      Is used for a second return value to indicate 
         *                   whether the start element tag is empty 
         *                   (e.g. "/&gt;").
         * @param endc       The end character that appears before the
         *                   closing angle bracket ('>').
         */
protected boolean scanAttribute(XMLAttributesImpl attributes, boolean[] empty, char endc) throws IOException {
    boolean skippedSpaces = skipSpaces();
    fBeginLineNumber = fCurrentEntity.getLineNumber();
    fBeginColumnNumber = fCurrentEntity.getColumnNumber();
    fBeginCharacterOffset = fCurrentEntity.getCharacterOffset();
    int c = fCurrentEntity.read();
    if (c == -1) {
        if (fReportErrors) {
            fErrorReporter.reportError("HTML1007", null);
        }
        return false;
    } else if (c == '>') {
        return false;
    } else if (c == '<') {
        fCurrentEntity.rewind();
        return false;
    }
    fCurrentEntity.rewind();
    String aname = scanName();
    if (aname == null) {
        if (fReportErrors) {
            fErrorReporter.reportError("HTML1011", null);
        }
        empty[0] = skipMarkup(false);
        return false;
    }
    if (!skippedSpaces && fReportErrors) {
        fErrorReporter.reportError("HTML1013", new Object[] { aname });
    }
    aname = modifyName(aname, fNamesAttrs);
    skipSpaces();
    c = fCurrentEntity.read();
    if (c == -1) {
        if (fReportErrors) {
            fErrorReporter.reportError("HTML1007", null);
        }
        throw new EOFException();
    }
    if (c == '/' || c == '>') {
        fQName.setValues(null, aname, aname, null);
        attributes.addAttribute(fQName, "CDATA", "");
        attributes.setSpecified(attributes.getLength() - 1, true);
        if (fAugmentations) {
            addLocationItem(attributes, attributes.getLength() - 1);
        }
        if (c == '/') {
            fCurrentEntity.rewind();
            empty[0] = skipMarkup(false);
        }
        return false;
    }
    /***
            // REVISIT: [Q] Why is this still here? -Ac
            if (c == '/' || c == '>') {
                if (c == '/') {
                    fCurrentEntity.offset--;
                    fCurrentEntity.columnNumber--;
                    empty[0] = skipMarkup(false);
                }
                fQName.setValues(null, aname, aname, null);
                attributes.addAttribute(fQName, "CDATA", "");
                attributes.setSpecified(attributes.getLength()-1, true);
                if (fAugmentations) {
                    addLocationItem(attributes, attributes.getLength() - 1);
                }
                return false;
            }
            /***/
    if (c == '=') {
        skipSpaces();
        c = fCurrentEntity.read();
        if (c == -1) {
            if (fReportErrors) {
                fErrorReporter.reportError("HTML1007", null);
            }
            throw new EOFException();
        }
        // Xiaowei/Ac: Fix for <a href=/cgi-bin/myscript>...</a> 
        if (c == '>') {
            fQName.setValues(null, aname, aname, null);
            attributes.addAttribute(fQName, "CDATA", "");
            attributes.setSpecified(attributes.getLength() - 1, true);
            if (fAugmentations) {
                addLocationItem(attributes, attributes.getLength() - 1);
            }
            return false;
        }
        fStringBuffer.clear();
        fNonNormAttr.clear();
        if (c != '\'' && c != '"') {
            fCurrentEntity.rewind();
            while (true) {
                c = fCurrentEntity.read();
                // Xiaowei/Ac: Fix for <a href=/broken/>...</a> 
                if (Character.isWhitespace((char) c) || c == '>') {
                    //fCharOffset--; 
                    fCurrentEntity.rewind();
                    break;
                }
                if (c == -1) {
                    if (fReportErrors) {
                        fErrorReporter.reportError("HTML1007", null);
                    }
                    throw new EOFException();
                }
                if (c == '&') {
                    int ce = scanEntityRef(fStringBuffer2, false);
                    if (ce != -1) {
                        fStringBuffer.append((char) ce);
                    } else {
                        fStringBuffer.append(fStringBuffer2);
                    }
                    fNonNormAttr.append(fStringBuffer2);
                } else {
                    fStringBuffer.append((char) c);
                    fNonNormAttr.append((char) c);
                }
            }
            fQName.setValues(null, aname, aname, null);
            String avalue = fStringBuffer.toString();
            attributes.addAttribute(fQName, "CDATA", avalue);
            int lastattr = attributes.getLength() - 1;
            attributes.setSpecified(lastattr, true);
            attributes.setNonNormalizedValue(lastattr, fNonNormAttr.toString());
            if (fAugmentations) {
                addLocationItem(attributes, attributes.getLength() - 1);
            }
            return true;
        }
        char quote = (char) c;
        boolean isStart = true;
        boolean prevSpace = false;
        do {
            boolean acceptSpace = !fNormalizeAttributes || (!isStart && !prevSpace);
            c = fCurrentEntity.read();
            if (c == -1) {
                if (fReportErrors) {
                    fErrorReporter.reportError("HTML1007", null);
                }
                break;
            }
            if (c == '&') {
                isStart = false;
                int ce = scanEntityRef(fStringBuffer2, false);
                if (ce != -1) {
                    fStringBuffer.append((char) ce);
                } else {
                    fStringBuffer.append(fStringBuffer2);
                }
                fNonNormAttr.append(fStringBuffer2);
            } else if (c == ' ' || c == '\t') {
                if (acceptSpace) {
                    fStringBuffer.append(fNormalizeAttributes ? ' ' : (char) c);
                }
                fNonNormAttr.append((char) c);
            } else if (c == '\r' || c == '\n') {
                if (c == '\r') {
                    int c2 = fCurrentEntity.read();
                    if (c2 != '\n') {
                        fCurrentEntity.rewind();
                    } else {
                        fNonNormAttr.append('\r');
                        c = c2;
                    }
                }
                if (acceptSpace) {
                    fStringBuffer.append(fNormalizeAttributes ? ' ' : '\n');
                }
                fCurrentEntity.incLine();
                fNonNormAttr.append((char) c);
            } else if (c != quote) {
                isStart = false;
                fStringBuffer.append((char) c);
                fNonNormAttr.append((char) c);
            }
            prevSpace = c == ' ' || c == '\t' || c == '\r' || c == '\n';
            isStart = isStart && prevSpace;
        } while (c != quote);
        if (fNormalizeAttributes && fStringBuffer.length > 0) {
            // trailing whitespace already normalized to single space 
            if (fStringBuffer.ch[fStringBuffer.length - 1] == ' ') {
                fStringBuffer.length--;
            }
        }
        fQName.setValues(null, aname, aname, null);
        String avalue = fStringBuffer.toString();
        attributes.addAttribute(fQName, "CDATA", avalue);
        int lastattr = attributes.getLength() - 1;
        attributes.setSpecified(lastattr, true);
        attributes.setNonNormalizedValue(lastattr, fNonNormAttr.toString());
        if (fAugmentations) {
            addLocationItem(attributes, attributes.getLength() - 1);
        }
    } else {
        fQName.setValues(null, aname, aname, null);
        attributes.addAttribute(fQName, "CDATA", "");
        attributes.setSpecified(attributes.getLength() - 1, true);
        fCurrentEntity.rewind();
        if (fAugmentations) {
            addLocationItem(attributes, attributes.getLength() - 1);
        }
    }
    return true;
}
