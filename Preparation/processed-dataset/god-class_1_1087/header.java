void method0() { 
//{{{ Capabilities  
/**
	 * Read capability.
	 * @since jEdit 2.6pre2
	 */
public static final int READ_CAP = 1 << 0;
/**
	 * Write capability.
	 * @since jEdit 2.6pre2
	 */
public static final int WRITE_CAP = 1 << 1;
/**
	 * Browse capability
	 * @since jEdit 4.3pre11
	 *
	 * This was the official API for adding items to a file
	 * system browser's <b>Plugins</b> menu in jEdit 4.1 and earlier. In
	 * jEdit 4.2, there is a different way of doing this, you must provide
	 * a <code>browser.actions.xml</code> file in your plugin JAR, and
	 * define <code>plugin.<i>class</i>.browser-menu-item</code>
	 * or <code>plugin.<i>class</i>.browser-menu</code> properties.
	 * See {@link org.gjt.sp.jedit.EditPlugin} for details.
	 */
public static final int BROWSE_CAP = 1 << 2;
/**
	 * Delete file capability.
	 * @since jEdit 2.6pre2
	 */
public static final int DELETE_CAP = 1 << 3;
/**
	 * Rename file capability.
	 * @since jEdit 2.6pre2
	 */
public static final int RENAME_CAP = 1 << 4;
/**
	 * Make directory capability.
	 * @since jEdit 2.6pre2
	 */
public static final int MKDIR_CAP = 1 << 5;
/**
	 * Low latency capability. If this is not set, then a confirm dialog
	 * will be shown before doing a directory search in this VFS.
	 * @since jEdit 4.1pre1
	 */
public static final int LOW_LATENCY_CAP = 1 << 6;
/**
	 * Case insensitive file system capability.
	 * @since jEdit 4.1pre1
	 */
public static final int CASE_INSENSITIVE_CAP = 1 << 7;
//}}}  
//{{{ Extended attributes  
/**
	 * File type.
	 * @since jEdit 4.2pre1
	 */
public static final String EA_TYPE = "type";
/**
	 * File status (read only, read write, etc).
	 * @since jEdit 4.2pre1
	 */
public static final String EA_STATUS = "status";
/**
	 * File size.
	 * @since jEdit 4.2pre1
	 */
public static final String EA_SIZE = "size";
/**
	 * File last modified date.
	 * @since jEdit 4.2pre1
	 */
public static final String EA_MODIFIED = "modified";
//}}}  
public static int IOBUFSIZE = 32678;
//}}}  
//{{{ Private members  
private String name;
private int caps;
private String[] extAttrs;
private static List<ColorEntry> colors;
private static final Object lock = new Object();
}
