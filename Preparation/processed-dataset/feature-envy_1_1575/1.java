public static void main(String[] args) {
    dbdump db = new dbdump();
    db.setUsage(usage);
    db.getCommandLine().add(argDeclFormat);
    // add any new args  
    db.init(args);
    // do any additional test here  
    // Action!  
    db.exec();
}
