public void doTable(Parse table) {
    summary.put(countsKey, counts());
    SortedSet keys = new TreeSet(summary.keySet());
    table.parts.more = rows(keys.iterator());
}
