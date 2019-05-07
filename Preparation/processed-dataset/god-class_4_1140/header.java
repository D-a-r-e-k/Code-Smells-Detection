void method0() { 
//  
// Data  
//  
/**
    * The column number where the error occured, 
    * or -1 if there is no column number available.
    */
public int fColumnNumber = -1;
/**
    * The line number where the error occured, 
    * or -1 if there is no line number available.
    */
public int fLineNumber = -1;
/** related data node*/
public Node fRelatedNode = null;
/**
    * The URI where the error occured, 
    * or null if there is no URI available.
    */
public String fUri = null;
/**
    * The byte offset into the input source this locator is pointing to or -1 
    * if there is no byte offset available
    */
public int fByteOffset = -1;
/**
    * The UTF-16, as defined in [Unicode] and Amendment 1 of [ISO/IEC 10646], 
    * offset into the input source this locator is pointing to or -1 if there 
    * is no UTF-16 offset available.
    */
public int fUtf16Offset = -1;
}
