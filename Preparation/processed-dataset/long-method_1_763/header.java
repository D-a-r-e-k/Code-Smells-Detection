void method0() { 
/** Whether the document currently has a prompt and is ready to accept input. */
private volatile boolean _hasPrompt;
/** A flag indicating that the interpreter was recently reset, and to reset the styles list 
    * the next time a style is added. Cannot reset immediately because then the styles would be lost while 
    * the interactions pane is resetting.
    */
private volatile boolean _toClear = false;
// fields for use by undo/redo functionality 
private volatile CompoundUndoManager _undoManager;
private static final int UNDO_LIMIT = 100;
private volatile boolean _isModifiedSinceSave = false;
private GlobalEventNotifier _notifier;
/** A list of styles and their locations augmenting this document.  This augmentation is NOT part of the reduced
    * model; it a separate extension that uses itself as a mutual exclusion lock.  This list holds pairs of location
    * intervals and strings (identifying styles).  In essence it maps regions to colors (??).
    * in the document and styles, which is basically a map of regions where the coloring view that is now attached to
    * the Interactions Pane.  It is not allowed to use the reduced model to determine the color settings when 
    * rendering text. (Why not? -- Corky)  We keep a list of all places where styles not considered by the reduced 
    * are being used, such as System.out, System.err, and the various return styles for Strings and other Objects.  
    * Since the LinkedList class is not thread safe,  we have to synchronized all methods that access pointers in 
    * _stylesList and the associated boolean _toClear.
    */
private List<Pair<Pair<Integer, Integer>, String>> _stylesList = new LinkedList<Pair<Pair<Integer, Integer>, String>>();
}
