// charDataInCount()  
/** convert attribute type from ints to strings */
private String getAttributeTypeName(XMLAttributeDecl attrDecl) {
    switch(attrDecl.simpleType.type) {
        case XMLSimpleType.TYPE_ENTITY:
            {
                return attrDecl.simpleType.list ? XMLSymbols.fENTITIESSymbol : XMLSymbols.fENTITYSymbol;
            }
        case XMLSimpleType.TYPE_ENUMERATION:
            {
                StringBuffer buffer = new StringBuffer();
                buffer.append('(');
                for (int i = 0; i < attrDecl.simpleType.enumeration.length; i++) {
                    if (i > 0) {
                        buffer.append("|");
                    }
                    buffer.append(attrDecl.simpleType.enumeration[i]);
                }
                buffer.append(')');
                return fSymbolTable.addSymbol(buffer.toString());
            }
        case XMLSimpleType.TYPE_ID:
            {
                return XMLSymbols.fIDSymbol;
            }
        case XMLSimpleType.TYPE_IDREF:
            {
                return attrDecl.simpleType.list ? XMLSymbols.fIDREFSSymbol : XMLSymbols.fIDREFSymbol;
            }
        case XMLSimpleType.TYPE_NMTOKEN:
            {
                return attrDecl.simpleType.list ? XMLSymbols.fNMTOKENSSymbol : XMLSymbols.fNMTOKENSymbol;
            }
        case XMLSimpleType.TYPE_NOTATION:
            {
                return XMLSymbols.fNOTATIONSymbol;
            }
    }
    return XMLSymbols.fCDATASymbol;
}
