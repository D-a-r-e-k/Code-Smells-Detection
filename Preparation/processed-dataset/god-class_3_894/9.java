private void extractStatus(Entry entry, String line) {
    char c = line.charAt(1);
    if (c == 'x' || c == 'X')
        entry.setStatus(Entry.CLEARED);
    else if (c == '*')
        entry.setStatus(Entry.RECONCILING);
}
