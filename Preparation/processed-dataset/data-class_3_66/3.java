/**
     * Imports a transaction for reader into session
     * 
     * @param session
     * @param reader
     * @return true if there are no more transactions in this session
     * @throws IOException
     */
private boolean importTransaction(Session session, BufferedReader reader) throws IOException {
    String format1 = reader.readLine();
    String format2 = reader.readLine();
    String format3 = reader.readLine();
    // Better: have some kind of plugin architecture for specific banks 
    if (format1 == null || format2 == null || format3 == null)
        return true;
    if (!(format1.equals("ABNANL2A") && format2.equals("940") && format3.equals("ABNANL2A"))) {
        // Unknown format; read until end of transaction or end of file 
        return ignoreTransaction(reader);
    }
    System.out.println("\nStart of statement, format=" + format1);
    // Now read all records for this statement 
    boolean finished = false;
    boolean ready = false;
    Account account = null;
    Entry entry = null;
    while (!ready) {
        String line = reader.readLine();
        if (line == null) {
            finished = true;
            ready = true;
            break;
        }
        if (line.startsWith("-")) {
            ready = true;
            break;
        }
        if (!line.startsWith(":")) {
            return ignoreTransaction(reader);
        }
        int colon = line.indexOf(":", 1);
        if (colon < 0) {
            return ignoreTransaction(reader);
        }
        String field = line.substring(1, colon);
        String value = line.substring(colon + 1);
        if (field.equals("25")) {
            account = accountForTransaction(session, value);
            System.out.println("Transaction for account: \"" + account + "\"");
        } else if (field.equals("61")) {
            System.out.println(":61: line: " + line);
            if (entry != null) {
                System.out.println("Adding entry");
                account.addEntry(entry);
                entry = null;
            }
            if (account == null) {
                System.err.println("Statement line without account");
                return ignoreTransaction(reader);
            }
            System.out.println("Creating new entry");
            entry = new Entry();
            if (value.length() < 11) {
                System.err.println("MT940 :61: line too short: " + line);
                return ignoreTransaction(reader);
            }
            int pos = 0;
            String dateString = value.substring(0, 6);
            Date date;
            try {
                date = swiftDateFormat.parse(dateString);
            } catch (ParseException e) {
                System.err.println("Error parsing value date in line: " + line);
                return ignoreTransaction(reader);
            }
            entry.setValuta(date);
            pos += 6;
            // Maybe booking date (optional in MT940) 
            if (Character.isDigit(value.charAt(pos))) {
                try {
                    String bookingDateString = value.substring(pos, pos + 4);
                    Calendar c = Calendar.getInstance();
                    c.setTime(date);
                    int month = Integer.parseInt(bookingDateString.substring(0, 2)) - 1;
                    int day = Integer.parseInt(bookingDateString.substring(2, 4));
                    int year = c.get(Calendar.YEAR);
                    if (month == 11 && c.get(Calendar.MONTH) == 0)
                        year--;
                    if (month == 0 && c.get(Calendar.MONTH) == 11)
                        year++;
                    c.set(year, month, day);
                    entry.setDate(c.getTime());
                    pos += 4;
                } catch (NumberFormatException e) {
                    System.err.println("Error parsing booking date in line: " + line);
                    return ignoreTransaction(reader);
                }
            } else {
                entry.setDate(date);
            }
            // Next: (C)redit, (R)eversal (C)redit, (D)ebit or (R)eversal (D)ebit 
            String creditDebit;
            if (Character.isDigit(value.charAt(pos + 1))) {
                creditDebit = value.substring(pos, pos + 1);
                pos++;
            } else {
                creditDebit = value.substring(pos, pos + 2);
                pos += 2;
            }
            // Next: amount, with , (comma) as decimal point 
            // following field starts with "N", so search for that 
            int bookingCodePos = value.indexOf("N", pos);
            if (pos < 0) {
                System.err.println("Error finding amount and booking code in line: " + line);
                return ignoreTransaction(reader);
            }
            String amountString = value.substring(pos, bookingCodePos);
            double amount;
            try {
                amount = swiftNumberFormat.parse(amountString).doubleValue();
            } catch (ParseException e) {
                System.err.println("Error parsing amount " + amountString + " in line: " + line);
                return ignoreTransaction(reader);
            }
            if (creditDebit.equals("D") || creditDebit.equals("RC"))
                amount = -amount;
            entry.setAmount((long) (amount * 100));
        } else if (field.equals("86")) {
            System.out.println(":86: line: " + line);
            entry.setDescription(value);
            StringBuffer buf = new StringBuffer();
            boolean busy = true;
            while (busy) {
                reader.mark(1500);
                line = reader.readLine();
                if (line.startsWith(":")) {
                    busy = false;
                    reader.reset();
                } else {
                    buf.append(" ").append(line);
                    System.out.println(":86: line: " + line);
                }
            }
            entry.setMemo(buf.toString());
        }
    }
    System.out.println("Finished parsing transation");
    if (entry != null) {
        System.out.println("Adding entry");
        account.addEntry(entry);
        entry = null;
    }
    return finished;
}
