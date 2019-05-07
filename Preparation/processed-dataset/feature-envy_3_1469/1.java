public String save() {
    myValidate();
    if (!hasActionErrors()) {
        // We ONLY modify the user currently logged in 
        User existingUser = getAuthenticatedUser();
        // We want to be VERY selective about what data gets updated 
        existingUser.setScreenName(getBean().getScreenName());
        existingUser.setFullName(getBean().getFullName());
        existingUser.setEmailAddress(getBean().getEmailAddress());
        existingUser.setLocale(getBean().getLocale());
        existingUser.setTimeZone(getBean().getTimeZone());
        // If user set both password and passwordConfirm then reset password 
        if (!StringUtils.isEmpty(getBean().getPasswordText()) && !StringUtils.isEmpty(getBean().getPasswordConfirm())) {
            try {
                existingUser.resetPassword(getBean().getPasswordText());
            } catch (WebloggerException e) {
                addMessage("yourProfile.passwordResetError");
            }
        }
        try {
            // save the updated profile 
            UserManager mgr = WebloggerFactory.getWeblogger().getUserManager();
            mgr.saveUser(existingUser);
            WebloggerFactory.getWeblogger().flush();
            // TODO: i18n 
            addMessage("profile updated.");
            return SUCCESS;
        } catch (WebloggerException ex) {
            log.error("ERROR in action", ex);
            // TODO: i18n 
            addError("unexpected error doing profile save");
        }
    }
    return INPUT;
}
