private static void exception(Scanner sc, String msg) throws ParsingException {
    throw new GrammerException(sc.getOffset(), sc.getLineNumberStart(), sc.getColumnNumberStart(), msg);
}
