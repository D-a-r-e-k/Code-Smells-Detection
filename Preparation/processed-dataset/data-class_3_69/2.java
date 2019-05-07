/**
     * Imports a QIF-file.
     */
public void importFile(Session session, File qifFile) {
    try {
        BufferedReader buffer = new BufferedReader(new FileReader(qifFile));
        String header = buffer.readLine();
        // import transactions of a non-investment account 
        if (header.startsWith("!Type:Bank") || header.startsWith("!Type:Cash") || header.startsWith("!Type:Bar") || // MS M*ney97 german edition 
        header.startsWith("!Type:CCard") || header.startsWith("!Type:Oth A") || header.startsWith("!Type:Oth L")) {
            String info = Constants.LANGUAGE.getString("QIF.chooseAccount") + " \"" + qifFile.getName() + "\".";
            int s = accountChooser.showDialog(session.getAccounts(), info, true);
            if (s == Constants.OK) {
                // an existing account has been selected 
                importAccount(session, accountChooser.getSelectedAccount(), buffer);
            } else if (s == Constants.NEW) {
                // create new account to import transactions 
                String name = qifFile.getName();
                if (name.endsWith(".qif"))
                    name = name.substring(0, name.length() - 4);
                importAccount(session, getNewAccount(session, name), buffer);
            }
        } else if (header.equals("!Type:Invst")) {
            System.err.println("QIF: Import of investment accounts is not suported.");
        } else if (header.equals("!Account")) {
            System.err.println("QIF: Import of account lists is not supported.");
        } else if (header.equals("!Type:Cat")) {
            System.err.println("QIF: Import of category lists is not supported.");
        } else if (header.equals("!Type:Class")) {
            System.err.println("QIF: Import of class lists is not supported.");
        } else if (header.equals("!Type:Memorized")) {
            System.err.println("QIF: Import of memorized transaction lists is not supported.");
        }
    } catch (IOException e) {
        mainFrame.fileReadError(qifFile);
    } catch (CanceledException e) {
    }
}
