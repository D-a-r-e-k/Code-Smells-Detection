void method0() { 
public static final Log _log = new Log("GlobalModel.txt", false);
private static final int NO_COMMENT_OFFSET = 0;
private static final int WING_COMMENT_OFFSET = 2;
private final List<DocumentClosedListener> _closedListeners = new LinkedList<DocumentClosedListener>();
// end debug code 
/** The maximum number of undos the model can remember */
private static final int UNDO_LIMIT = 1000;
/** Specifies if tabs are removed on open and converted to spaces. */
private static boolean _tabsRemoved = true;
/** Specifies if the document has been modified since the last save.  Modified under write lock. */
private volatile boolean _isModifiedSinceSave = false;
/** This reference to the OpenDefinitionsDocument is needed so that the document iterator 
    * (the DefaultGlobalModel) can find the next ODD given a DD. */
private volatile OpenDefinitionsDocument _odd;
private volatile CompoundUndoManager _undoManager;
/** Keeps track of the listeners to this model. */
private final GlobalEventNotifier _notifier;
/** Uses an updated version of the DefaultEditorKit */
private final DefinitionsEditorKit _editor;
/* Relying on the following definition in AbstractDJDocument.  It must be placed there to be initialized before use!
   protected static final Object _wrappedPosListLock = new Object();
   */
/** List with weak references to positions. */
private volatile LinkedList<WeakReference<WrappedPosition>> _wrappedPosList;
/** Formerly used to call editToBeUndone and editToBeRedone since they are protected methods in UndoManager. */
//  private class OurUndoManager extends UndoManager { 
//    private boolean _compoundEditState = false; 
//    private OurCompoundEdit _compoundEdit; 
// 
//    public void startCompoundEdit() { 
//      if (_compoundEditState) { 
//        throw new IllegalStateException("Cannot start a compound edit while making a compound edit"); 
//      } 
//      _compoundEditState = true; 
//      _compoundEdit = new OurCompoundEdit(); 
//    } 
// 
//    public void endCompoundEdit() { 
//      if (!_compoundEditState) { 
//        throw new IllegalStateException("Cannot end a compound edit while not making a compound edit"); 
//      } 
//      _compoundEditState = false; 
//      _compoundEdit.end(); 
//      super.addEdit(_compoundEdit); 
//    } 
// 
//    public UndoableEdit getNextUndo() { 
//      return editToBeUndone(); 
//    } 
// 
//    public UndoableEdit getNextRedo() { 
//      return editToBeRedone(); 
//    } 
// 
//    public boolean addEdit(UndoableEdit e) { 
//      if (_compoundEditState) { 
//        return _compoundEdit.addEdit(e); 
//      } 
//      else { 
//        return super.addEdit(e); 
//      } 
//    } 
//  } 
// 
// 
//  public java.util.Vector getEdits() { 
//     return _undoManager._compoundEdit.getEdits(); 
//  } 
// 
//  private class OurCompoundEdit extends CompoundEdit { 
//     public java.util.Vector getEdits() { 
//        return edits; 
//     } 
//  } 
/** Formerly used to help track down memory leaks */
//  protected void finalize() throws Throwable{ 
//    System.err.println("destroying DefDocument for " + _odd); 
//    super.finalize(); 
//  } 
//   
//  private List<Pair<Option, OptionListener>> _optionListeners = new LinkedList<Option, OptionListener>>(); 
// 
//  public void clearOptionListeners() { 
//    for (Pair<Option, OptionListener> l: _optionListeners) { 
//      DrJava.getConfig().removeOptionListener( l.getFirst(), l.getSecond()); 
//    } 
//    _optionListeners.clear(); 
//  } 
//   
//  public void addOptionListener(Option op, OptionListener l) { 
//    DrJava.getConfig().addOptionListener(op, l); 
//    _optionListeners.add(new Pair<Option, OptionListener>(op, l)); 
//  } 
/** This list of listeners to notify when we are finalized. */
private List<FinalizationListener<DefinitionsDocument>> _finalizationListeners = new LinkedList<FinalizationListener<DefinitionsDocument>>();
}
