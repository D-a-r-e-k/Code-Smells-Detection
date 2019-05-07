void method0() { 
//  
// Constants  
//  
/** Serialization version. */
static final long serialVersionUID = 515687835542616694L;
//  
// Data  
//  
/** Node Iterators */
protected transient List iterators;
/** Reference queue for cleared Node Iterator references */
protected transient ReferenceQueue iteratorReferenceQueue;
/** Ranges */
protected transient List ranges;
/** Reference queue for cleared Range references */
protected transient ReferenceQueue rangeReferenceQueue;
/** Table for event listeners registered to this document nodes. */
protected Hashtable eventListeners;
/** Bypass mutation events firing. */
protected boolean mutationEvents = false;
EnclosingAttr savedEnclosingAttr;
}
