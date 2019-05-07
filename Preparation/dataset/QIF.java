/*
 *
 *  JMoney - A Personal Finance Manager
 *  Copyright (c) 2002 Johann Gyger <johann.gyger@switzerland.org>
 *
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *
 */

package net.sf.jmoney.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.filechooser.FileFilter;

import net.sf.jmoney.Constants;
import net.sf.jmoney.gui.AccountChooser;
import net.sf.jmoney.gui.MainFrame;
import net.sf.jmoney.model.Account;
import net.sf.jmoney.model.Category;
import net.sf.jmoney.model.CategoryNode;
import net.sf.jmoney.model.DoubleEntry;
import net.sf.jmoney.model.Entry;
import net.sf.jmoney.model.Session;
import net.sf.jmoney.model.SimpleCategory;
import net.sf.jmoney.model.SplittedEntry;

/**
 * Quicken interchange format file import and export.
 */
public class QIF implements FileFormat {
    static NumberFormat number = NumberFormat.getInstance(Locale.US);
    static Calendar calendar = Calendar.getInstance(Locale.US);
    MainFrame mainFrame;
    AccountChooser accountChooser;

    /**
     * Creates a new QIF.
     */
    public QIF(MainFrame parent, AccountChooser ac) {
        mainFrame = parent;
        accountChooser = ac;
        number.setMinimumFractionDigits(2);
        number.setMaximumFractionDigits(2);
    }
    
    /**
     * Create a QIF file filter
     * @return A FileFilter for QIF files
     */
    public FileFilter fileFilter() {
        return new QifFileFilter();
    }

    /**
     * Imports a QIF-file.
     */
    public void importFile(Session session, File qifFile) {
        try {
            BufferedReader buffer = new BufferedReader(new FileReader(qifFile));
            String header = buffer.readLine();

            // import transactions of a non-investment account
            if (header.startsWith("!Type:Bank")
                || header.startsWith("!Type:Cash")
                || header.startsWith("!Type:Bar")
                || // MS M*ney97 german edition
            header.startsWith("!Type:CCard")
                || header.startsWith("!Type:Oth A")
                || header.startsWith("!Type:Oth L")) {

                String info =
                    Constants.LANGUAGE.getString("QIF.chooseAccount")
                        + " \""
                        + qifFile.getName()
                        + "\".";
                int s =
                    accountChooser.showDialog(
                        session.getAccounts(),
                        info,
                        true);
                if (s == Constants.OK) {
                    // an existing account has been selected
                    importAccount(
                        session,
                        accountChooser.getSelectedAccount(),
                        buffer);
                } else if (s == Constants.NEW) {
                    // create new account to import transactions
                    String name = qifFile.getName();
                    if (name.endsWith(".qif"))
                        name = name.substring(0, name.length() - 4);
                    importAccount(
                        session,
                        getNewAccount(session, name),
                        buffer);
                }
            }

            // import transactions of a investment account
            else if (header.equals("!Type:Invst")) {
                System.err.println(
                    "QIF: Import of investment accounts is not suported.");
            }

            // import account list
            else if (header.equals("!Account")) {
                System.err.println(
                    "QIF: Import of account lists is not supported.");
            }

            // import category list
            else if (header.equals("!Type:Cat")) {
                System.err.println(
                    "QIF: Import of category lists is not supported.");
            }

            // import class list
            else if (header.equals("!Type:Class")) {
                System.err.println(
                    "QIF: Import of class lists is not supported.");
            }

            // import memorized transaction list
            else if (header.equals("!Type:Memorized")) {
                System.err.println(
                    "QIF: Import of memorized transaction lists is not supported.");
            }
        } catch (IOException e) {
            mainFrame.fileReadError(qifFile);
        } catch (CanceledException e) {}
    }

    /**
     * Imports an account from a QIF-file.
     */
    private void importAccount(
        Session session,
        Account account,
        BufferedReader buffer)
        throws IOException, CanceledException {
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
                switch (firstChar) {
                    case 'D' :
                        entry.setDate(parseDate(line));
                        break;
                    case 'T' :
                        extractAmount(
                            entry,
                            line,
                            account.getCurrency().getScaleFactor());
                        break;
                    case 'C' :
                        extractStatus(entry, line);
                        break;
                    case 'N' :
                        entry.setCheck(line.substring(1));
                        break;
                    case 'P' :
                        entry.setDescription(line.substring(1));
                        break;
                    case 'L' :
                        entry =
                            extractCategory(
                                session,
                                accounts,
                                account,
                                entry,
                                line);
                        break;
                    case 'M' :
                        entry.setMemo(line.substring(1));
                        break;
                    case 'S' :
                        entry =
                            extractSplittedCategory(
                                session,
                                accounts,
                                account,
                                entry,
                                line);
                        break;
                    case 'E' :
                        entry = extractSplittedDescription(entry, line);
                        break;
                    case '$' :
                        entry =
                            extractSplittedAmount(
                                entry,
                                line,
                                account.getCurrency().getScaleFactor());
                        break;
                    default :
                        break;
                }
            }
            if (line == null)
                break;
            account.addEntry(entry);
            removeSimilarTransfer(account, entry);
        }
        account.setEntries(account.getEntries()); // notify listeners
    }

    private SplittedEntry extractSplittedCategory(
        Session session,
        Accounts accounts,
        Account account,
        Entry entry,
        String line)
        throws CanceledException {
        if (entry instanceof DoubleEntry) {
            DoubleEntry de = (DoubleEntry) entry;
            Account other = (Account) de.getCategory();
            other.getEntries().removeElement(de.getOther());
        }
        SplittedEntry se = entry.toSplittedEntry();
        se.setCategory(
            (Category) session.getCategories().getSplitNode().getUserObject());
        se.addEntry(
            extractCategory(session, accounts, account, new Entry(), line));
        return se;
    }

    private SplittedEntry extractSplittedDescription(
        Entry entry,
        String line) {
        SplittedEntry se = toSplittedEntry(entry);
        ((Entry) se.getEntries().lastElement()).setDescription(
            line.substring(1));
        return se;
    }

    private SplittedEntry extractSplittedAmount(
        Entry entry,
        String line,
        short factor)
        throws CanceledException {
        SplittedEntry se = toSplittedEntry(entry);
        Entry e = (Entry) se.getEntries().lastElement();
        extractAmount(e, line, factor);
        if (e instanceof DoubleEntry)
             ((DoubleEntry) e).getOther().setAmount(-e.getAmount());
        return se;
    }

    private SplittedEntry toSplittedEntry(Entry entry) {
        SplittedEntry se;
        if (entry instanceof SplittedEntry) {
            se = (SplittedEntry) entry;
        } else {
            se = entry.toSplittedEntry();
            se.addEntry(new Entry());
        }
        return se;
    }

    private void extractAmount(Entry entry, String line, short factor) {
        Number n = number.parse(line, new ParsePosition(1));
        entry.setAmount(n == null ? 0 : Math.round(n.doubleValue() * factor));
    }

    private void extractStatus(Entry entry, String line) {
        char c = line.charAt(1);

        if (c == 'x' || c == 'X')
            entry.setStatus(Entry.CLEARED);
        else if (c == '*')
            entry.setStatus(Entry.RECONCILING);
    }

    private Entry extractCategory(
        Session session,
        Accounts accounts,
        Account account,
        Entry entry,
        String line)
        throws CanceledException {
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
            for (colon = 1; colon < line.length(); colon++)
                if (line.charAt(colon) == ':')
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
                entry.setCategory(
                    getSubcategory(subcategoryName, category, session));
            }
        }
        return entry;
    }

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
            writeln(
                writer,
                "T" + formatAmount(account.getStartBalance(), account));
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
                        writeln(
                            writer,
                            "L[" + category.getCategoryName() + "]");
                    else {
                        writeln(writer, "L" + category.getFullCategoryName());
                    }
                    // TODO: Split Entries
                }
                // end of entry
                writeln(writer, "^");
            }
            writer.close();
        } catch (IOException e) {
            mainFrame.fileWriteError(file);
        }
    }

    private String formatAmount(long amount, Account account) {
        return number.format(
            ((double) amount) / account.getCurrency().getScaleFactor());
    }

    /**
     * Parses the date string and returns a date object:
     *   11/2/98 ->> 11/2/1998
     *   3/15'00 ->> 3/15/2000
     */
    private Date parseDate(String line) {
        try {
            StringTokenizer st = new StringTokenizer(line, "D/\'");
            int month = Integer.parseInt(st.nextToken().trim());
            int day = Integer.parseInt(st.nextToken().trim());
            int year = Integer.parseInt(st.nextToken().trim());
            if (year < 100) {
                if (line.indexOf("'") < 0)
                    year = year + 1900;
                else
                    year = year + 2000;
            }
            calendar.clear();
            calendar.set(year, month - 1, day);
            return calendar.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String formatDate(Date date) {
        if (date == null)
            return null;
        calendar.setTime(date);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int year = calendar.get(Calendar.YEAR);
        if ((year >= 1900) && (year < 2000))
            return "D" + month + "/" + day + "/" + (year - 1900);
        if ((year >= 2000) && (year < 2010))
            return "D" + month + "/" + day + "\'0" + (year - 2000);
        else if ((year >= 2010) && (year < 2100))
            return "D" + month + "/" + day + "\'" + (year - 2000);
        else
            return null;
    }

    private Account getNewAccount(Session session, String accountName) {
        Account account = session.getNewAccount(accountName);
        return account;
    }

    /**
     * Returns the category with the specified name. If it doesn't exist a new
     * category will be created.
     */
    private SimpleCategory getCategory(String categoryName, Session session) {
        SimpleCategory category =
            searchCategory(categoryName, session.getCategories().getRootNode());
        if (category == null) {
            category = new SimpleCategory(categoryName);
            session.getCategories().insertNodeInto(
                category.getCategoryNode(),
                session.getCategories().getRootNode(),
                0);
        }
        return category;
    }

    /**
     * Returns the subcategory with the specified name. If it doesn't exist a new
     * subcategory will be created.
     */
    private SimpleCategory getSubcategory(
        String name,
        SimpleCategory category,
        Session session) {
        SimpleCategory subcategory =
            searchCategory(name, category.getCategoryNode());
        if (subcategory == null) {
            subcategory = new SimpleCategory(name);
            session.getCategories().insertNodeInto(
                subcategory.getCategoryNode(),
                category.getCategoryNode(),
                0);
        }
        return subcategory;
    }

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
                        if ((newDe2 != oldE2)
                            && (oldE2 instanceof DoubleEntry)
                            && (newDe2.getAmount() == oldE2.getAmount())
                            && equals(newDe2.getDate(), oldE2.getDate())
                            && equals(
                                newDe2.getCategory(),
                                oldE2.getCategory())) {
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
                if ((newDe != e)
                    && (e instanceof DoubleEntry)
                    && equals(newDe.getCategory(), e.getCategory())
                    && (newDe.getAmount() == e.getAmount())
                    && equals(newDe.getDate(), e.getDate())
                    && equals(newDe.getCheck(), e.getCheck())
                    && equals(newDe.getDescription(), e.getDescription())
                    && equals(newDe.getMemo(), e.getMemo()))
                    newDe.remove();
            }
        }
    }

    private boolean equals(Object obj1, Object obj2) {
        if (obj1 == null && obj2 == null)
            return true;
        if (obj1 == null || obj2 == null)
            return false;
        return obj1.equals(obj2);
    }

    /**
     * Searches a category and returns null if it doesn't exist.
     */
    private SimpleCategory searchCategory(String name, CategoryNode root) {
        for (Enumeration e = root.children(); e.hasMoreElements();) {
            CategoryNode node = (CategoryNode) e.nextElement();
            Object obj = node.getUserObject();
            if (obj instanceof SimpleCategory) {
                SimpleCategory category = (SimpleCategory) obj;
                if (category.getCategoryName().equals(name))
                    return category;
            }
        }
        return null;
    }

    /**
     * Writes a line and jumps to a new one.
     */
    private void writeln(BufferedWriter writer, String line)
        throws IOException {
        writer.write(line);
        writer.newLine();
    }

    /**
     * Auxiliary class with the function of a table with the columns "names"
     * and "accounts".
     * Provides a method "getAccount()" which returns the corresponding account to
     * a given account name. If there is no entry in the table the user will be
     * asked to choose an account.
     */
    class Accounts {
        Vector names = new Vector(10);
        Vector accounts = new Vector(10);
        Account getAccount(String accountName, Session session)
            throws CanceledException {
            Account account;
            int index;
            // search account
            for (index = 0; index < names.size(); index++) {
                String name = (String) names.get(index);
                if (name.equals(accountName))
                    break;
            }
            if (index == names.size()) {
                // account doesn't exist -> ask user
                String info =
                    Constants.LANGUAGE.getString("QIF.chooseTransferAccount")
                        + " \""
                        + accountName
                        + "\".";
                int status =
                    accountChooser.showDialog(
                        session.getAccounts(),
                        info,
                        true);
                if (status == Constants.OK)
                    account = accountChooser.getSelectedAccount();
                else if (status == Constants.NEW)
                    account = getNewAccount(session, accountName);
                else
                    throw new CanceledException("QIF import canceled.");
                names.add(accountName);
                accounts.add(account);
            } else {
                account = (Account) accounts.get(index);
            }
            return account;
        }
    }

    /**
     * Exception to cancel an import/export operation.
     */
    public class CanceledException extends Exception {
        public CanceledException() {
            super();
        }
        public CanceledException(String s) {
            super(s);
        }
    }

    /**
     * A Filter that accepts QIF Files.
     */
    public static class QifFileFilter extends FileFilter {
        public boolean accept(File f) {
            if (f == null)
                return false;
            if (f.isDirectory())
                return true;
            return f.getName().toLowerCase().endsWith(".qif");
        }
        public String getDescription() {
            return "Quicken Interchange Format (*.qif)";
        }
    }

}
