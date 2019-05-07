/**
	 * Open connection to SMTP server and login if needed.
	 * 
	 * @return true if connection was successful, false otherwise
	 */
private void ensureAuthenticated() throws IOException, SMTPException, CommandCancelledException {
    String username;
    char[] password = new char[0];
    boolean savePassword;
    // Init Values 
    // user's email address 
    fromAddress = identity.getAddress().getMailAddress();
    // Sent Folder 
    SpecialFoldersItem specialFoldersItem = accountItem.getSpecialFoldersItem();
    Integer i = new Integer(specialFoldersItem.get("sent"));
    int sentFolder = i.intValue();
    usingSSL = smtpItem.getBoolean("enable_ssl");
    int authMethod = getLoginMethod();
    boolean authenticated = (authMethod == AuthenticationManager.NONE);
    if (authMethod == AuthenticationManager.POP_BEFORE_SMTP) {
        // no esmtp - use POP3-before-SMTP instead 
        try {
            pop3Authentification();
        } catch (POP3Exception e) {
            throw new SMTPException(e);
        }
        authenticated = true;
    }
    ensureConnected();
    if (!authenticated) {
        username = smtpItem.get("user");
        password = Blowfish.decrypt(smtpItem.getRoot().getAttribute("password", ""));
        savePassword = smtpItem.getBoolean("save_password");
        if (username.length() == 0) {
            // there seems to be no username set in the smtp-options 
            //  -> use username from pop3 or imap options 
            if (accountItem.isPopAccount()) {
                PopItem pop3Item = accountItem.getPopItem();
                username = pop3Item.get("user");
            } else {
                ImapItem imapItem = accountItem.getImapItem();
                username = imapItem.get("user");
            }
        }
        PasswordDialog passDialog = new PasswordDialog();
        // ask password from user 
        if (password.length == 0) {
            passDialog.showDialog(MessageFormat.format(MailResourceLoader.getString("dialog", "password", "enter_password"), new Object[] { username, smtpItem.get("host") }), new String(password), savePassword);
            if (passDialog.success()) {
                password = passDialog.getPassword();
                savePassword = passDialog.getSave();
            } else {
                throw new CommandCancelledException();
            }
        }
        // try to authenticate 
        while (!authenticated) {
            try {
                try {
                    protocol.auth(AuthenticationManager.getSaslName(authMethod), username, password);
                    authenticated = true;
                } catch (AuthenticationException e) {
                    // If the cause is a SMTPExcpetion then only password 
                    // wrong 
                    // else bogus authentication mechanism 
                    if (e.getCause() instanceof SMTPException) {
                        int errorCode = ((SMTPException) e.getCause()).getCode();
                        // Authentication is not supported 
                        if (errorCode == 504) {
                            //TODO: Add dialog to inform user that the smtp server 
                            // does not support authentication 
                            JOptionPane.showMessageDialog(FrameManager.getInstance().getActiveFrame(), new MultiLineLabel(MailResourceLoader.getString("dialog", "error", "authentication_not_supported")), MailResourceLoader.getString("dialog", "error", "authentication_process_error"), JOptionPane.INFORMATION_MESSAGE);
                            //Turn off authentication for the future 
                            smtpItem.setString("login_method", Integer.toString(AuthenticationManager.NONE));
                            return;
                        }
                    } else {
                        throw (SMTPException) e.getCause();
                    }
                    // Some error in the client/server communication 
                    //  --> fall back to default login process 
                    int result = JOptionPane.showConfirmDialog(FrameManager.getInstance().getActiveFrame(), new MultiLineLabel(e.getMessage() + "\n" + MailResourceLoader.getString("dialog", "error", "authentication_fallback_to_default")), MailResourceLoader.getString("dialog", "error", "authentication_process_error"), JOptionPane.OK_CANCEL_OPTION);
                    if (result == JOptionPane.OK_OPTION) {
                        authMethod = AuthenticationManager.SASL_PLAIN;
                        smtpItem.setString("login_method", Integer.toString(authMethod));
                    } else {
                        throw new CommandCancelledException();
                    }
                }
            } catch (SMTPException e) {
                passDialog.showDialog(MessageFormat.format(MailResourceLoader.getString("dialog", "password", "enter_password"), new Object[] { username, smtpItem.get("host") }), new String(password), savePassword);
                if (!passDialog.success()) {
                    throw new CommandCancelledException();
                } else {
                    password = passDialog.getPassword();
                    savePassword = passDialog.getSave();
                }
            }
        }
        // authentication was successful 
        // -> save name/password 
        smtpItem.setString("user", username);
        smtpItem.setBoolean("save_password", savePassword);
        if (savePassword) {
            smtpItem.setString("password", Blowfish.encrypt(password));
        }
    }
}
