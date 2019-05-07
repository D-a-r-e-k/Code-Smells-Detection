void method0() { 
static final boolean JJK_DEBUG = false;
static final boolean JJK_NEWCODE = true;
/** Manefest constant
   */
static final String NAMESPACE_DECL_NS = "http://www.w3.org/XML/1998/namespace";
/** The current position in the DOM tree. Last node examined for
   * possible copying to DTM. */
private transient Node m_pos;
/** The current position in the DTM tree. Who children get appended to. */
private int m_last_parent = 0;
/** The current position in the DTM tree. Who children reference as their 
   * previous sib. */
private int m_last_kid = NULL;
/** The top of the subtree.
   * %REVIEW%: 'may not be the same as m_context if "//foo" pattern.'
   * */
private transient Node m_root;
/** True iff the first element has been processed. This is used to control
      synthesis of the implied xml: namespace declaration node. */
boolean m_processedFirstElement = false;
/** true if ALL the nodes in the m_root subtree have been processed;
   * false if our incremental build has not yet finished scanning the
   * DOM tree.  */
private transient boolean m_nodesAreProcessed;
/** The node objects.  The instance part of the handle indexes
   * directly into this vector.  Each DTM node may actually be
   * composed of several DOM nodes (for example, if logically-adjacent
   * Text/CDATASection nodes in the DOM have been coalesced into a
   * single DTM Text node); this table points only to the first in
   * that sequence. */
protected Vector m_nodes = new Vector();
TreeWalker m_walker = new TreeWalker(null);
}
