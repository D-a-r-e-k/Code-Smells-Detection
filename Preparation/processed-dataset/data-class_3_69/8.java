private void extractAmount(Entry entry, String line, short factor) {
    Number n = number.parse(line, new ParsePosition(1));
    entry.setAmount(n == null ? 0 : Math.round(n.doubleValue() * factor));
}
