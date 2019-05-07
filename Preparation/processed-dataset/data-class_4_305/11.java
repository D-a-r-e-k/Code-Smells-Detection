// scanComment()  
/**
     * Scans an attribute value and normalizes whitespace converting all
     * whitespace characters to space characters.
     * 
     * [10] AttValue ::= '"' ([^<&"] | Reference)* '"' | "'" ([^<&'] | Reference)* "'"
     *
     * @param value The XMLString to fill in with the value.
     * @param nonNormalizedValue The XMLString to fill in with the 
     *                           non-normalized value.
     * @param atName The name of the attribute being parsed (for error msgs).
     * @param checkEntities true if undeclared entities should be reported as VC violation,  
     *                      false if undeclared entities should be reported as WFC violation.
     * @param eleName The name of element to which this attribute belongs.
     *
     * @return true if the non-normalized and normalized value are the same
     * 
     * <strong>Note:</strong> This method uses fStringBuffer2, anything in it
     * at the time of calling is lost.
     **/
protected boolean scanAttributeValue(XMLString value, XMLString nonNormalizedValue, String atName, boolean checkEntities, String eleName) throws IOException, XNIException {
    // quote  
    int quote = fEntityScanner.peekChar();
    if (quote != '\'' && quote != '"') {
        reportFatalError("OpenQuoteExpected", new Object[] { eleName, atName });
    }
    fEntityScanner.scanChar();
    int entityDepth = fEntityDepth;
    int c = fEntityScanner.scanLiteral(quote, value);
    if (DEBUG_ATTR_NORMALIZATION) {
        System.out.println("** scanLiteral -> \"" + value.toString() + "\"");
    }
    int fromIndex = 0;
    if (c == quote && (fromIndex = isUnchangedByNormalization(value)) == -1) {
        /** Both the non-normalized and normalized attribute values are equal. **/
        nonNormalizedValue.setValues(value);
        int cquote = fEntityScanner.scanChar();
        if (cquote != quote) {
            reportFatalError("CloseQuoteExpected", new Object[] { eleName, atName });
        }
        return true;
    }
    fStringBuffer2.clear();
    fStringBuffer2.append(value);
    normalizeWhitespace(value, fromIndex);
    if (DEBUG_ATTR_NORMALIZATION) {
        System.out.println("** normalizeWhitespace -> \"" + value.toString() + "\"");
    }
    if (c != quote) {
        fScanningAttribute = true;
        fStringBuffer.clear();
        do {
            fStringBuffer.append(value);
            if (DEBUG_ATTR_NORMALIZATION) {
                System.out.println("** value2: \"" + fStringBuffer.toString() + "\"");
            }
            if (c == '&') {
                fEntityScanner.skipChar('&');
                if (entityDepth == fEntityDepth) {
                    fStringBuffer2.append('&');
                }
                if (fEntityScanner.skipChar('#')) {
                    if (entityDepth == fEntityDepth) {
                        fStringBuffer2.append('#');
                    }
                    int ch = scanCharReferenceValue(fStringBuffer, fStringBuffer2);
                    if (ch != -1) {
                        if (DEBUG_ATTR_NORMALIZATION) {
                            System.out.println("** value3: \"" + fStringBuffer.toString() + "\"");
                        }
                    }
                } else {
                    String entityName = fEntityScanner.scanName();
                    if (entityName == null) {
                        reportFatalError("NameRequiredInReference", null);
                    } else if (entityDepth == fEntityDepth) {
                        fStringBuffer2.append(entityName);
                    }
                    if (!fEntityScanner.skipChar(';')) {
                        reportFatalError("SemicolonRequiredInReference", new Object[] { entityName });
                    } else if (entityDepth == fEntityDepth) {
                        fStringBuffer2.append(';');
                    }
                    if (entityName == fAmpSymbol) {
                        fStringBuffer.append('&');
                        if (DEBUG_ATTR_NORMALIZATION) {
                            System.out.println("** value5: \"" + fStringBuffer.toString() + "\"");
                        }
                    } else if (entityName == fAposSymbol) {
                        fStringBuffer.append('\'');
                        if (DEBUG_ATTR_NORMALIZATION) {
                            System.out.println("** value7: \"" + fStringBuffer.toString() + "\"");
                        }
                    } else if (entityName == fLtSymbol) {
                        fStringBuffer.append('<');
                        if (DEBUG_ATTR_NORMALIZATION) {
                            System.out.println("** value9: \"" + fStringBuffer.toString() + "\"");
                        }
                    } else if (entityName == fGtSymbol) {
                        fStringBuffer.append('>');
                        if (DEBUG_ATTR_NORMALIZATION) {
                            System.out.println("** valueB: \"" + fStringBuffer.toString() + "\"");
                        }
                    } else if (entityName == fQuotSymbol) {
                        fStringBuffer.append('"');
                        if (DEBUG_ATTR_NORMALIZATION) {
                            System.out.println("** valueD: \"" + fStringBuffer.toString() + "\"");
                        }
                    } else {
                        if (fEntityManager.isExternalEntity(entityName)) {
                            reportFatalError("ReferenceToExternalEntity", new Object[] { entityName });
                        } else {
                            if (!fEntityManager.isDeclaredEntity(entityName)) {
                                //WFC & VC: Entity Declared  
                                if (checkEntities) {
                                    if (fValidation) {
                                        fErrorReporter.reportError(XMLMessageFormatter.XML_DOMAIN, "EntityNotDeclared", new Object[] { entityName }, XMLErrorReporter.SEVERITY_ERROR);
                                    }
                                } else {
                                    reportFatalError("EntityNotDeclared", new Object[] { entityName });
                                }
                            }
                            fEntityManager.startEntity(entityName, true);
                        }
                    }
                }
            } else if (c == '<') {
                reportFatalError("LessthanInAttValue", new Object[] { eleName, atName });
                fEntityScanner.scanChar();
                if (entityDepth == fEntityDepth) {
                    fStringBuffer2.append((char) c);
                }
            } else if (c == '%' || c == ']') {
                fEntityScanner.scanChar();
                fStringBuffer.append((char) c);
                if (entityDepth == fEntityDepth) {
                    fStringBuffer2.append((char) c);
                }
                if (DEBUG_ATTR_NORMALIZATION) {
                    System.out.println("** valueF: \"" + fStringBuffer.toString() + "\"");
                }
            } else if (c == '\n' || c == '\r') {
                fEntityScanner.scanChar();
                fStringBuffer.append(' ');
                if (entityDepth == fEntityDepth) {
                    fStringBuffer2.append('\n');
                }
            } else if (c != -1 && XMLChar.isHighSurrogate(c)) {
                fStringBuffer3.clear();
                if (scanSurrogates(fStringBuffer3)) {
                    fStringBuffer.append(fStringBuffer3);
                    if (entityDepth == fEntityDepth) {
                        fStringBuffer2.append(fStringBuffer3);
                    }
                    if (DEBUG_ATTR_NORMALIZATION) {
                        System.out.println("** valueI: \"" + fStringBuffer.toString() + "\"");
                    }
                }
            } else if (c != -1 && isInvalidLiteral(c)) {
                reportFatalError("InvalidCharInAttValue", new Object[] { eleName, atName, Integer.toString(c, 16) });
                fEntityScanner.scanChar();
                if (entityDepth == fEntityDepth) {
                    fStringBuffer2.append((char) c);
                }
            }
            c = fEntityScanner.scanLiteral(quote, value);
            if (entityDepth == fEntityDepth) {
                fStringBuffer2.append(value);
            }
            normalizeWhitespace(value);
        } while (c != quote || entityDepth != fEntityDepth);
        fStringBuffer.append(value);
        if (DEBUG_ATTR_NORMALIZATION) {
            System.out.println("** valueN: \"" + fStringBuffer.toString() + "\"");
        }
        value.setValues(fStringBuffer);
        fScanningAttribute = false;
    }
    nonNormalizedValue.setValues(fStringBuffer2);
    // quote  
    int cquote = fEntityScanner.scanChar();
    if (cquote != quote) {
        reportFatalError("CloseQuoteExpected", new Object[] { eleName, atName });
    }
    return nonNormalizedValue.equals(value.ch, value.offset, value.length);
}
