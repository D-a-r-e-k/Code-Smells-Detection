void method0() { 
/**
   * The file format version for the segments_N codec header
   */
public static final int VERSION_40 = 0;
/** Used for the segments.gen file only!
   * Whenever you add a new format, make it 1 smaller (negative version logic)! */
public static final int FORMAT_SEGMENTS_GEN_CURRENT = -2;
/** Used to name new segments. */
public int counter;
/** Counts how often the index has been changed.  */
public long version;
private long generation;
// generation of the "segments_N" for the next commit 
private long lastGeneration;
// generation of the "segments_N" file we last successfully read 
// or wrote; this is normally the same as generation except if 
// there was an IOException that had interrupted a commit 
/** Opaque Map&lt;String, String&gt; that user can specify during IndexWriter.commit */
public Map<String, String> userData = Collections.<String, String>emptyMap();
private List<SegmentInfoPerCommit> segments = new ArrayList<SegmentInfoPerCommit>();
/**
   * If non-null, information about loading segments_N files
   * will be printed here.  @see #setInfoStream.
   */
private static PrintStream infoStream = null;
// Only non-null after prepareCommit has been called and 
// before finishCommit is called 
ChecksumIndexOutput pendingSegnOutput;
private static final String SEGMENT_INFO_UPGRADE_CODEC = "SegmentInfo3xUpgrade";
private static final int SEGMENT_INFO_UPGRADE_VERSION = 0;
/* Advanced configuration of retry logic in loading
     segments_N file */
private static int defaultGenLookaheadCount = 10;
}
