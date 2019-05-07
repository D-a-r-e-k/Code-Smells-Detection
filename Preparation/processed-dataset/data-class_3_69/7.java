private SplittedEntry toSplittedEntry(Entry entry) {
    SplittedEntry se;
    if (entry instanceof SplittedEntry) {
        se = (SplittedEntry) entry;
    } else {
        se = entry.toSplittedEntry();
        se.addEntry(new Entry());
    }
    return se;
}
