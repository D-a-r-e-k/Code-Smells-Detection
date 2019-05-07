void method0() { 
//{{{ Some constants  
/**
	 * Backed up property.
	 * @since jEdit 3.2pre2
	 */
public static final String BACKED_UP = "Buffer__backedUp";
/**
	 * Caret info properties.
	 * @since jEdit 3.2pre1
	 */
public static final String CARET = "Buffer__caret";
public static final String CARET_POSITIONED = "Buffer__caretPositioned";
/**
	 * Stores a List of {@link org.gjt.sp.jedit.textarea.Selection} instances.
	 */
public static final String SELECTION = "Buffer__selection";
/**
	 * This should be a physical line number, so that the scroll
	 * position is preserved correctly across reloads (which will
	 * affect virtual line numbers, due to fold being reset)
	 */
public static final String SCROLL_VERT = "Buffer__scrollVert";
public static final String SCROLL_HORIZ = "Buffer__scrollHoriz";
/**
	 * Should jEdit try to set the encoding based on a UTF8, UTF16 or
	 * XML signature at the beginning of the file?
	 */
public static final String ENCODING_AUTODETECT = "encodingAutodetect";
/**
	 * This property is set to 'true' if the file has a trailing newline.
	 * @since jEdit 4.0pre1
	 */
public static final String TRAILING_EOL = "trailingEOL";
/**
	 * This property is set to 'true' if the file should be GZipped.
	 * @since jEdit 4.0pre4
	 */
public static final String GZIPPED = "gzipped";
//}}}  
//{{{ checkFileStatus() method  
public static final int FILE_NOT_CHANGED = 0;
public static final int FILE_CHANGED = 1;
public static final int FILE_DELETED = 2;
//}}}  
//}}}  
//{{{ Package-private members  
/** The previous buffer in the list. */
Buffer prev;
/** The next buffer in the list. */
Buffer next;
//}}}  
//{{{ Flag values  
private static final int CLOSED = 0;
private static final int NEW_FILE = 3;
private static final int UNTITLED = 4;
private static final int AUTOSAVE_DIRTY = 5;
private static final int AUTORELOAD = 6;
private static final int AUTORELOAD_DIALOG = 7;
private static final int TEMPORARY = 10;
private static final int MARKERS_CHANGED = 12;
//}}}  
private int flags;
//}}}  
//{{{ Instance variables  
private String path;
private String symlinkPath;
private String name;
private String directory;
private File file;
private File autosaveFile;
private long modTime;
private byte[] md5hash;
private int initialLength;
private final Vector<Marker> markers;
private Socket waitSocket;
private List<BufferUndoListener> undoListeners;
}
