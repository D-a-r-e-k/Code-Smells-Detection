/**
     * Check if the transfer already exists.
     */
private void removeSimilarTransfer(Account a1, Entry newEntry) {
    if (newEntry instanceof SplittedEntry) {
        SplittedEntry se = (SplittedEntry) newEntry;
        for (int i = 0; i < se.getEntries().size(); i++) {
            Entry subEntry = (Entry) se.getEntries().elementAt(i);
            if (subEntry instanceof DoubleEntry) {
                DoubleEntry newDe1 = (DoubleEntry) subEntry;
                DoubleEntry newDe2 = newDe1.getOther();
                Account a2 = (Account) newDe1.getCategory();
                for (int j = 0; j < a2.getEntries().size(); j++) {
                    Entry oldE2 = (Entry) a2.getEntries().elementAt(j);
                    if ((newDe2 != oldE2) && (oldE2 instanceof DoubleEntry) && (newDe2.getAmount() == oldE2.getAmount()) && equals(newDe2.getDate(), oldE2.getDate()) && equals(newDe2.getCategory(), oldE2.getCategory())) {
                        DoubleEntry oldDe2 = (DoubleEntry) oldE2;
                        a1.getEntries().removeElement(oldDe2.getOther());
                        a2.getEntries().removeElement(oldDe2);
                    }
                }
            }
        }
    } else if (newEntry instanceof DoubleEntry) {
        DoubleEntry newDe = (DoubleEntry) newEntry;
        for (int i = 0; i < a1.getEntries().size(); i++) {
            Entry e = (Entry) a1.getEntries().elementAt(i);
            if ((newDe != e) && (e instanceof DoubleEntry) && equals(newDe.getCategory(), e.getCategory()) && (newDe.getAmount() == e.getAmount()) && equals(newDe.getDate(), e.getDate()) && equals(newDe.getCheck(), e.getCheck()) && equals(newDe.getDescription(), e.getDescription()) && equals(newDe.getMemo(), e.getMemo()))
                newDe.remove();
        }
    }
}
