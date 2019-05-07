// handleEndDocument()  
// handle character contents  
// returns the normalized string if possible, otherwise the original string  
XMLString handleCharacters(XMLString text) {
    if (fSkipValidationDepth >= 0)
        return text;
    fSawText = fSawText || text.length > 0;
    // Note: data in EntityRef and CDATA is normalized as well  
    // if whitespace == -1 skip normalization, because it is a complexType  
    // or a union type.  
    if (fNormalizeData && fWhiteSpace != -1 && fWhiteSpace != XSSimpleType.WS_PRESERVE) {
        // normalize data  
        normalizeWhitespace(text, fWhiteSpace == XSSimpleType.WS_COLLAPSE);
        text = fNormalizedStr;
    }
    if (fAppendBuffer)
        fBuffer.append(text.ch, text.offset, text.length);
    // When it's a complex type with element-only content, we need to  
    // find out whether the content contains any non-whitespace character.  
    if (fCurrentType != null && fCurrentType.getTypeCategory() == XSTypeDefinition.COMPLEX_TYPE) {
        XSComplexTypeDecl ctype = (XSComplexTypeDecl) fCurrentType;
        if (ctype.fContentType == XSComplexTypeDecl.CONTENTTYPE_ELEMENT) {
            // data outside of element content  
            for (int i = text.offset; i < text.offset + text.length; i++) {
                if (!XMLChar.isSpace(text.ch[i])) {
                    fSawCharacters = true;
                    break;
                }
            }
        }
    }
    return text;
}
