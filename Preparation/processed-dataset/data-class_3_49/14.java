/**
     * Removes all mappings from this map.
     */
public synchronized void clear() {
    Entry[] tab = table;
    for (int i = 0; i < tab.length; ++i) {
        // must invalidate all to force concurrent get's to wait and then retry  
        for (Entry e = tab[i]; e != null; e = e.next) {
            e.value = null;
            /** OpenSymphony BEGIN */
            itemRemoved(e.key);
        }
        tab[i] = null;
    }
    // Clean out the entire disk cache  
    persistClear();
    count = 0;
    recordModification(tab);
}
