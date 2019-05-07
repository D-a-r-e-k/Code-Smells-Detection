private Entry extractCategory(Session session, Accounts accounts, Account account, Entry entry, String line) throws CanceledException {
    if (line.charAt(1) == '[') {
        // transfer 
        String accountName = line.substring(2, line.length() - 1);
        Account other = accounts.getAccount(accountName, session);
        if (account != other) {
            DoubleEntry doubleEntry = entry.toDoubleEntry();
            doubleEntry.setCategory(other);
            doubleEntry.getOther().setCategory(account);
            entry = doubleEntry;
            other.addEntry(doubleEntry.getOther());
        }
    } else {
        // assumption: a category consists at least of one char 
        // either "LCategory" or "LCategory:Subcategory" 
        int colon;
        for (colon = 1; colon < line.length(); colon++) if (line.charAt(colon) == ':')
            break;
        if (colon == line.length()) {
            // "LCategory" 
            String categoryName = line.substring(1);
            entry.setCategory(getCategory(categoryName, session));
        } else {
            // "LCategory:Subcategory 
            String categoryName = line.substring(1, colon);
            String subcategoryName = line.substring(colon + 1);
            SimpleCategory category = getCategory(categoryName, session);
            entry.setCategory(getSubcategory(subcategoryName, category, session));
        }
    }
    return entry;
}
