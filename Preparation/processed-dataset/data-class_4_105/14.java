/**
    *
    * @param args
    */
public static void main(String[] args) {
    if (args.length == 1) {
        new UML2JagGenerator().generateXML(args[0], ".");
    } else {
        System.out.println("Pass an xmi file as argument!");
    }
}
