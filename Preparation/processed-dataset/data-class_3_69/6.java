private SplittedEntry extractSplittedAmount(Entry entry, String line, short factor) throws CanceledException {
    SplittedEntry se = toSplittedEntry(entry);
    Entry e = (Entry) se.getEntries().lastElement();
    extractAmount(e, line, factor);
    if (e instanceof DoubleEntry)
        ((DoubleEntry) e).getOther().setAmount(-e.getAmount());
    return se;
}
