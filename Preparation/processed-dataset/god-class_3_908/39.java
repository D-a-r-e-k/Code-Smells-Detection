//}}} 
//{{{ printUsage() 
private static void printUsage() {
    System.out.println("jsXe " + getVersion());
    System.out.println("The Java Simple XML Editor");
    System.out.println();
    System.out.println("Copyright 2004 Ian Lewis");
    System.out.println("This is free software; see the source for copying conditions. There is NO");
    System.out.println("warranty; not even for MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.");
    System.out.println();
    System.out.println("Usage: jsXe [<options>] [<files>]");
    // System.out.println("  -v, --view <viewname>     open the files using the view specified"); 
    // System.out.println("                            valid views are:"); 
    // System.out.println("                                            tree"); 
    // System.out.println("                                            source"); 
    System.out.println("      --debug               print debug information");
    System.out.println("  -h, --help                display this help and exit");
    System.out.println("  -V, --version             print version information and exit");
    System.out.println();
    System.out.println("Report bugs to <ian_lewis@users.sourceforge.net>");
}
