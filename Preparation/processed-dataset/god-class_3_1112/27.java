public void prompt() {
    if (sqltoolPrompt != null)
        prompt(sqltoolPrompt);
    specialAppendState = (interactive && magicPrefix != null);
    // This tells scanner that if SQL input "looks" empty, it isn't. 
    if (interactive && magicPrefix != null) {
        psStd.print(magicPrefix);
        magicPrefix = null;
    }
}
