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

import java.io.*;
import java.text.*;
import java.util.*;

import javax.swing.filechooser.FileFilter;

import net.sf.jmoney.Constants;
import net.sf.jmoney.gui.AccountChooser;
import net.sf.jmoney.gui.MainFrame;
import net.sf.jmoney.model.*;

/**
 * MT940 file import and export.
 * 
 * @author Jan-Pascal van Best
 * @author Johann Gyger
 */
public class MT940 implements FileFormat {
    NumberFormat number = NumberFormat.getInstance(Locale.US);
    Calendar calendar = Calendar.getInstance();
    DateFormat swiftDateFormat = new SimpleDateFormat( "yyMMdd" );
    NumberFormat swiftNumberFormat = NumberFormat.getInstance(Locale.GERMANY);
    MainFrame mainFrame;
    AccountChooser accountChooser;

    /**
     * Create a new MT940
     */
    public MT940(MainFrame parent, AccountChooser ac) {
        mainFrame = parent;
        accountChooser = ac;
        number.setMinimumFractionDigits(2);
        number.setMaximumFractionDigits(2);
    }

    /* (non-Javadoc)
     * @see net.sf.jmoney.FileFormat#importFile(net.sf.jmoney.model.Session, java.io.File)
     */
    public void importFile(Session session, File qifFile) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(qifFile));
            String header = reader.readLine();
            
            int count=0;
            boolean finished = false;
            while ( !finished ) {
                finished = importTransaction( session, reader );
                count++;
            }
        } catch (IOException e) {
            mainFrame.fileReadError(qifFile);
        } // catch (CanceledException e) {}

    }
    
    private Account accountForTransaction( Session session, String accountNumber ) {
        if ( accountNumber==null ) {
            return null;
        }
        accountNumber = accountNumber.trim();
        Account account = session.getAccountByNumber( accountNumber );
        System.out.println( "Found account named " + account + " for number \"" + accountNumber +"\"");
        if ( account == null ) {
            String info =
            Constants.LANGUAGE.getString("QIF.chooseAccount")
                + " \""
                + accountNumber
                + "\".";

            int s = accountChooser.showDialog( 
                    session.getAccounts(),
                    info,
                    true);
            switch( s ) {
                case Constants.OK: 
                    account = accountChooser.getSelectedAccount();
                    System.out.println( "OK, using account " + account.getName() );
                    break;
                case Constants.NEW:
                    System.out.println( "NEW, creating new account" );
                    String name = accountNumber;
                    account = session.getNewAccount(accountNumber);
                    // TODO: allow editing of account here
                    // TODO: create mapping account# -> account for this import
                    account.setAccountNumber( accountNumber );
                    break;
            }
        }
        return account;
    }

    /**
     * Imports a transaction for reader into session
     * 
     * @param session
     * @param reader
     * @return true if there are no more transactions in this session
     * @throws IOException
     */
    private boolean importTransaction( Session session, BufferedReader reader ) throws IOException {
        String format1 = reader.readLine();
        String format2 = reader.readLine();
        String format3 = reader.readLine();
        
        // Better: have some kind of plugin architecture for specific banks
        
        if ( format1==null || format2==null || format3==null ) return true;
        
        if ( ! (format1.equals("ABNANL2A") && format2.equals("940") && format3.equals("ABNANL2A")) ) {
            // Unknown format; read until end of transaction or end of file
            return ignoreTransaction( reader );
        }
        
        System.out.println( "\nStart of statement, format="+format1 );
        
        // Now read all records for this statement
        boolean finished = false;
        boolean ready = false;
        Account account = null;
        Entry entry = null;
        while( ! ready ) {
            String line = reader.readLine();
            if ( line == null ) {
                finished = true;
                ready = true;
                break;
            } 
            if ( line.startsWith( "-" )) {
                ready = true;
                break;
                
            }
            if ( ! line.startsWith(":" )) {
                return ignoreTransaction( reader );
            }
            int colon = line.indexOf( ":", 1 );
            if ( colon<0 ) {
                return ignoreTransaction( reader );
            }
            String field = line.substring( 1, colon );
            String value = line.substring( colon+1 );
            
            if ( field.equals( "25" )) {
                account = accountForTransaction( session, value );
                System.out.println( "Transaction for account: \"" + account + "\"");
                     // TODO: detect DoubleEntry en SplittedEntry, if possible
            } else if ( field.equals( "61" ) ) {
                System.out.println( ":61: line: " + line );
                if ( entry != null ) {
                    System.out.println( "Adding entry" );
                    account.addEntry( entry );
                    entry = null;
                }
                if ( account == null ) {
                    System.err.println( "Statement line without account" );
                    return ignoreTransaction( reader );
                }
                System.out.println( "Creating new entry" );
                entry = new Entry();
                if ( value.length() < 11 ) {
                    System.err.println( "MT940 :61: line too short: " + line );
                    return ignoreTransaction( reader );
                }
                
                int pos = 0;
                String dateString = value.substring( 0, 6 );
                Date date;
                try {
                    date = swiftDateFormat.parse( dateString );
                } catch ( ParseException e ) {
                    System.err.println( "Error parsing value date in line: " + line );
                    return ignoreTransaction( reader );
                }
                entry.setValuta( date );
                pos += 6;
                
                // Maybe booking date (optional in MT940)
                if ( Character.isDigit(value.charAt(pos)) ) {
                    try {
                        String bookingDateString = value.substring( pos, pos+4 );
                        Calendar c = Calendar.getInstance();
                        c.setTime(date);

                        int month = Integer.parseInt( bookingDateString.substring(0,2) )-1;
                        int day = Integer.parseInt( bookingDateString.substring(2,4) );
                        int year = c.get(Calendar.YEAR);
                        if ( month == 11 && c.get(Calendar.MONTH)==0 ) year--;
                        if ( month == 0 && c.get(Calendar.MONTH)==11 ) year++;
                        c.set(year, month, day);
                        entry.setDate(c.getTime());
                        pos += 4;
                    } catch ( NumberFormatException e ) {
                        System.err.println( "Error parsing booking date in line: " + line );
                        return ignoreTransaction( reader );
                    }
                } else {
                    entry.setDate( date );
                }
                
                // Next: (C)redit, (R)eversal (C)redit, (D)ebit or (R)eversal (D)ebit
                String creditDebit;
                if ( Character.isDigit(value.charAt(pos+1)) ) {
                    creditDebit = value.substring( pos, pos+1 );
                    pos++;
                } else {
                    creditDebit = value.substring( pos, pos+2 );
                    pos += 2;
                }
                
                // Next: amount, with , (comma) as decimal point
                // following field starts with "N", so search for that
                int bookingCodePos = value.indexOf( "N", pos );
                if ( pos<0 ) {
                    System.err.println( "Error finding amount and booking code in line: " + line );
                    return ignoreTransaction( reader );
                }
                String amountString = value.substring( pos, bookingCodePos );
                double amount;
                try {
                    amount = swiftNumberFormat.parse( amountString ).doubleValue();
                } catch ( ParseException e ){
                    System.err.println( "Error parsing amount " + amountString + " in line: " + line );
                    return ignoreTransaction( reader );
                }
                if ( creditDebit.equals("D") || creditDebit.equals("RC")) amount = -amount;
                entry.setAmount( (long) (amount*100) );

/*
                // setCreation(long aCreation) {
                setCheck(String aCheck) {
                setCategory(Category aCategory) {
                // setStatus(int aStatus) {
*/
            } else if ( field.equals( "86") ) {
                System.out.println( ":86: line: " + line );
                entry.setDescription( value );
                StringBuffer buf = new StringBuffer();
                boolean busy = true;
                while( busy ) {
                    reader.mark( 1500 );
                    line = reader.readLine();
                    if ( line.startsWith(":") ) {
                        busy = false;
                        reader.reset();
                    } else {
                        buf.append( " " ).append( line );
                        System.out.println( ":86: line: " + line );
                    }
                }
                entry.setMemo( buf.toString() );
            }
        }
        System.out.println( "Finished parsing transation" );
        
        if ( entry != null ) {
            System.out.println( "Adding entry" );
            account.addEntry( entry );
            entry = null;
        }
        
        return finished;
    }

    /**
     * @param reader
     * @return true if end of file reached
     */
    private boolean ignoreTransaction( BufferedReader reader ) throws IOException {
        while( true ) {
            String line = reader.readLine();
            if ( line==null ) return true;
            if ( line.startsWith("-") ) return false;
        }
    }

    /* (non-Javadoc)
     * @see net.sf.jmoney.FileFormat#exportAccount(net.sf.jmoney.model.Session, net.sf.jmoney.model.Account, java.io.File)
     */
    public void exportAccount(Session session, Account account, File file) {
        // TODO Auto-generated method stub

    }
    
    public FileFilter fileFilter() {
        return new MT940FileFilter();
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
     * A Filter that accepts MT940 Files.
     */
    public static class MT940FileFilter extends FileFilter {
        public boolean accept(File f) {
            if (f == null)
                return false;
            if (f.isDirectory())
                return true;
            return f.getName().toLowerCase().endsWith(".sta");
        }
        public String getDescription() {
            return "MT940 file (*.sta)";
        }
    }



}
