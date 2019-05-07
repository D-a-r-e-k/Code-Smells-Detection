/**
    *
    * @param args
    */
public static void main(String[] args) {
    if (args.length == 1) {
        (new Jag2UMLGenerator()).generateXMI(args[0]);
    } else {
        log("Pass a JAG xml file as argument!");
    }
}
