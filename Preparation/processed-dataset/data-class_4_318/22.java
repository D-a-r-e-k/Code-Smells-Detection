// endDocument(Augmentations)  
//  
// DOMRevalidationHandler methods  
//  
public boolean characterData(String data, Augmentations augs) {
    fSawText = fSawText || data.length() > 0;
    // REVISIT: this methods basically duplicates implementation of  
    //          handleCharacters(). We should be able to reuse some code  
    // if whitespace == -1 skip normalization, because it is a complexType  
    // or a union type.  
    if (fNormalizeData && fWhiteSpace != -1 && fWhiteSpace != XSSimpleType.WS_PRESERVE) {
        // normalize data  
        normalizeWhitespace(data, fWhiteSpace == XSSimpleType.WS_COLLAPSE);
        fBuffer.append(fNormalizedStr.ch, fNormalizedStr.offset, fNormalizedStr.length);
    } else {
        if (fAppendBuffer)
            fBuffer.append(data);
    }
    // When it's a complex type with element-only content, we need to  
    // find out whether the content contains any non-whitespace character.  
    boolean allWhiteSpace = true;
    if (fCurrentType != null && fCurrentType.getTypeCategory() == XSTypeDefinition.COMPLEX_TYPE) {
        XSComplexTypeDecl ctype = (XSComplexTypeDecl) fCurrentType;
        if (ctype.fContentType == XSComplexTypeDecl.CONTENTTYPE_ELEMENT) {
            // data outside of element content  
            for (int i = 0; i < data.length(); i++) {
                if (!XMLChar.isSpace(data.charAt(i))) {
                    allWhiteSpace = false;
                    fSawCharacters = true;
                    break;
                }
            }
        }
    }
    return allWhiteSpace;
}
