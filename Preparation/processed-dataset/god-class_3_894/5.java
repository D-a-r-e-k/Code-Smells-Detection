private SplittedEntry extractSplittedDescription(Entry entry, String line) {
    SplittedEntry se = toSplittedEntry(entry);
    ((Entry) se.getEntries().lastElement()).setDescription(line.substring(1));
    return se;
}
