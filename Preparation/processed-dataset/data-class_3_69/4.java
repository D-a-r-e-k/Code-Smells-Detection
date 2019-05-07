private SplittedEntry extractSplittedCategory(Session session, Accounts accounts, Account account, Entry entry, String line) throws CanceledException {
    if (entry instanceof DoubleEntry) {
        DoubleEntry de = (DoubleEntry) entry;
        Account other = (Account) de.getCategory();
        other.getEntries().removeElement(de.getOther());
    }
    SplittedEntry se = entry.toSplittedEntry();
    se.setCategory((Category) session.getCategories().getSplitNode().getUserObject());
    se.addEntry(extractCategory(session, accounts, account, new Entry(), line));
    return se;
}
