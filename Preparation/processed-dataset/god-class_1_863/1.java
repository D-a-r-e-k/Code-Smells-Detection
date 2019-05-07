// <init>() 
// 
// XMLDocumentHandler methods 
// 
/** Doctype declaration. */
public void doctypeDecl(String root, String pubid, String sysid, Augmentations augs) throws XNIException {
    // NOTE: Xerces HTML DOM implementation (up to and including 
    //       2.5.0) throws a heirarchy request error exception  
    //       when a doctype node is appended to the tree. So,  
    //       don't insert this node into the tree for those  
    //       versions... -Ac 
    String VERSION = XercesBridge.getInstance().getVersion();
    boolean okay = true;
    if (VERSION.startsWith("Xerces-J 2.")) {
        okay = getParserSubVersion() > 5;
    } else if (VERSION.startsWith("XML4J")) {
        okay = false;
    }
    // if okay, insert doctype; otherwise, don't risk it 
    if (okay) {
        super.doctypeDecl(root, pubid, sysid, augs);
    }
}
