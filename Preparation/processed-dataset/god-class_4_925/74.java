private void configConsoleLoggingLevel(QuickServer qs, String temp) {
    if (temp.equals("SEVERE"))
        qs.setConsoleLoggingLevel(Level.SEVERE);
    else if (temp.equals("WARNING"))
        qs.setConsoleLoggingLevel(Level.WARNING);
    else if (temp.equals("INFO"))
        qs.setConsoleLoggingLevel(Level.INFO);
    else if (temp.equals("CONFIG"))
        qs.setConsoleLoggingLevel(Level.CONFIG);
    else if (temp.equals("FINE"))
        qs.setConsoleLoggingLevel(Level.FINE);
    else if (temp.equals("FINER"))
        qs.setConsoleLoggingLevel(Level.FINER);
    else if (temp.equals("FINEST"))
        qs.setConsoleLoggingLevel(Level.FINEST);
    else
        logger.warning("unknown level " + temp);
}
