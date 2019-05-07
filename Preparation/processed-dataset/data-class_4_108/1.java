/**
     * Runs the JaGGenerator.
     *
     * @param args the command line arguments
     */
public static void main(String args[]) {
    // Initialize commons logging..  
    log.info("Starting jag...");
    jagGenerator = new JagGenerator();
    jagGenerator.setVisible(true);
}
