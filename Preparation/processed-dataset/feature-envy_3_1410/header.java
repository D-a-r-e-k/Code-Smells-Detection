void method0() { 
/** Are we initialized? */
private boolean m_initialized = false;
/** The page counters. */
private Map<String, Counter> m_counters = null;
/** The page counters in storage format. */
private Properties m_storage = null;
/** Are all changes stored? */
private boolean m_dirty = false;
/** The page count storage background thread. */
private Thread m_pageCountSaveThread = null;
/** The work directory. */
private String m_workDir = null;
/** Comparator for descending sort on page count. */
private final Comparator<Object> m_compareCountDescending = new Comparator<Object>() {

    public int compare(Object o1, Object o2) {
        final int v1 = getCount(o1);
        final int v2 = getCount(o2);
        return (v1 == v2) ? ((String) o1).compareTo((String) o2) : (v1 < v2) ? 1 : -1;
    }
};
}
