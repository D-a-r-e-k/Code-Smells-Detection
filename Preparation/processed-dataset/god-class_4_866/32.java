// debugging 
/** Sets the scanner. */
protected void setScanner(Scanner scanner) {
    fScanner = scanner;
    if (DEBUG_SCANNER) {
        System.out.print("$$$ setScanner(");
        System.out.print(scanner != null ? scanner.getClass().getName() : "null");
        System.out.println(");");
    }
}
