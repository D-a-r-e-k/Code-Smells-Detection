void method0() { 
/** Event type indicating a change of the options */
public static final int PROPERTIES_CHANGED = 0;
/** Event type indicating a change of the colorizing syntax mode */
public static final int SYNTAX_MODE_CHANGED = 1;
// text area specific 
/** Event type indicating a change in a text area */
public static final int CHANGED_UPDATE = 2;
/** Event type indicating an insertion in a text area */
public static final int INSERT_UPDATE = 3;
/** Event type indicating a removing in a text area */
public static final int REMOVE_UPDATE = 4;
/** Event type indicating a file was opened */
public static final int FILE_OPENED = 10;
/** Event type indicating a file was cleared (new) */
public static final int FILE_CLEARED = 11;
/** Event type indicating batch mode is on */
public static final int BATCH_MODE_SET = 20;
/** Event type indicating batch mode is off */
public static final int BATCH_MODE_UNSET = 21;
/** Event type indicating current selected text area has gained focus. UNUSED */
public static final int TEXT_AREA_FOCUS_GAINED = 76;
/** Event type indicating current selected text area has changed */
public static final int TEXT_AREA_SELECTED = 77;
/** Event type indicating a new text area is available */
public static final int TEXT_AREA_OPENED = 78;
/** Event type indicating a text area was closed */
public static final int TEXT_AREA_CLOSED = 79;
/** Event type indicating a new window was opened */
public static final int OPENING_WINDOW = 98;
/** Event type indicating a window was closed */
public static final int CLOSING_WINDOW = 99;
/**
   * Event type indicating the last Jext window is being killed, and maybe Jext itself. In the background
   * mode, in fact, when the last window of Jext is closed, you'll get this event and you should, i.e.,
   * save properties; but when a new window is created, the start method won't be called, so it won't
   * be able to reinitialize freed resources.
   * If Jext instead is completely closed, Plugin.stop() is called. But it is not assured that the stop method will
   * be called, for instance if the user shutdowns the PC while Jext is still in the bg(and the user won't close Jext
   * when it's in the BG). So don't rely on stop().
   */
public static final int KILLING_JEXT = 101;
// private 
private int event;
private JextFrame parent;
private JextTextArea textArea;
}
