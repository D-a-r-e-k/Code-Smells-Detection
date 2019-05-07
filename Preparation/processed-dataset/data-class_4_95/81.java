private static void handleOptions(String args[], QuickServer quickserver) {
    if (args.length < 3)
        return;
    if (args[2].equals("-fullXML2File") && args.length >= 4) {
        File file = new File(args[3]);
        logger.info("Writing full xml configuration to file: " + file.getAbsolutePath());
        try {
            TextFile.write(file, quickserver.getConfig().toXML(null));
        } catch (Exception e) {
            logger.warning("Error writing full xml configuration: " + e);
        }
    }
}
