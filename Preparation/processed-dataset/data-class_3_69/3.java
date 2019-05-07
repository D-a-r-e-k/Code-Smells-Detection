/**
     * Imports an account from a QIF-file.
     */
private void importAccount(Session session, Account account, BufferedReader buffer) throws IOException, CanceledException {
    session.modified();
    String line;
    Accounts accounts = new Accounts();
    while (true) {
        Entry entry = new Entry();
        while (true) {
            line = buffer.readLine();
            if (line == null || line.equals("^"))
                break;
            char firstChar = line.charAt(0);
            switch(firstChar) {
                case 'D':
                    entry.setDate(parseDate(line));
                    break;
                case 'T':
                    extractAmount(entry, line, account.getCurrency().getScaleFactor());
                    break;
                case 'C':
                    extractStatus(entry, line);
                    break;
                case 'N':
                    entry.setCheck(line.substring(1));
                    break;
                case 'P':
                    entry.setDescription(line.substring(1));
                    break;
                case 'L':
                    entry = extractCategory(session, accounts, account, entry, line);
                    break;
                case 'M':
                    entry.setMemo(line.substring(1));
                    break;
                case 'S':
                    entry = extractSplittedCategory(session, accounts, account, entry, line);
                    break;
                case 'E':
                    entry = extractSplittedDescription(entry, line);
                    break;
                case '$':
                    entry = extractSplittedAmount(entry, line, account.getCurrency().getScaleFactor());
                    break;
                default:
                    break;
            }
        }
        if (line == null)
            break;
        account.addEntry(entry);
        removeSimilarTransfer(account, entry);
    }
    account.setEntries(account.getEntries());
}
