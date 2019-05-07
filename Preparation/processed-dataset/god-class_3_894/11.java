public void exportAccount(Session session, Account account, File file) {
    try {
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        Vector entries = account.getEntries();
        // write header 
        writeln(writer, "!Type:Bank");
        // write first entry (containing the start balance) 
        if (entries.size() > 0) {
            Entry entry = (Entry) entries.elementAt(0);
            String dateString = formatDate(entry.getDate());
            if (dateString != null)
                writeln(writer, dateString);
        }
        writeln(writer, "T" + formatAmount(account.getStartBalance(), account));
        writeln(writer, "CX");
        writeln(writer, "POpening Balance");
        writeln(writer, "L[" + account.getName() + "]");
        writeln(writer, "^");
        // write entries 
        for (int i = 0; i < entries.size(); i++) {
            Entry entry = (Entry) entries.elementAt(i);
            // date 
            String dateString = formatDate(entry.getDate());
            if (dateString != null)
                writeln(writer, dateString);
            // memo 
            if (entry.getMemo() != null)
                writeln(writer, "M" + entry.getMemo());
            // status 
            if (entry.getStatus() == Entry.RECONCILING)
                writeln(writer, "C*");
            else if (entry.getStatus() == Entry.CLEARED)
                writeln(writer, "CX");
            // amount 
            writeln(writer, "T" + formatAmount(entry.getAmount(), account));
            // check 
            if (entry.getCheck() != null)
                writeln(writer, "N" + entry.getCheck());
            // description 
            if (entry.getDescription() != null)
                writeln(writer, "P" + entry.getDescription());
            // category 
            Category category = entry.getCategory();
            if (category != null) {
                if (category instanceof Account)
                    writeln(writer, "L[" + category.getCategoryName() + "]");
                else {
                    writeln(writer, "L" + category.getFullCategoryName());
                }
            }
            // end of entry 
            writeln(writer, "^");
        }
        writer.close();
    } catch (IOException e) {
        mainFrame.fileWriteError(file);
    }
}
