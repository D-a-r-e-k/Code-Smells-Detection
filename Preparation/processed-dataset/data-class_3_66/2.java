private Account accountForTransaction(Session session, String accountNumber) {
    if (accountNumber == null) {
        return null;
    }
    accountNumber = accountNumber.trim();
    Account account = session.getAccountByNumber(accountNumber);
    System.out.println("Found account named " + account + " for number \"" + accountNumber + "\"");
    if (account == null) {
        String info = Constants.LANGUAGE.getString("QIF.chooseAccount") + " \"" + accountNumber + "\".";
        int s = accountChooser.showDialog(session.getAccounts(), info, true);
        switch(s) {
            case Constants.OK:
                account = accountChooser.getSelectedAccount();
                System.out.println("OK, using account " + account.getName());
                break;
            case Constants.NEW:
                System.out.println("NEW, creating new account");
                String name = accountNumber;
                account = session.getNewAccount(accountNumber);
                // TODO: allow editing of account here 
                // TODO: create mapping account# -> account for this import 
                account.setAccountNumber(accountNumber);
                break;
        }
    }
    return account;
}
