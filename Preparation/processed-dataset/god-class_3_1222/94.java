/**
   * A test method for this class. Just extracts the first command line argument
   * as a classifier class name and calls evaluateModel.
   * 
   * @param args an array of command line arguments, the first of which must be
   *          the class name of a classifier.
   */
public static void main(String[] args) {
    try {
        if (args.length == 0) {
            throw new Exception("The first argument must be the class name" + " of a classifier");
        }
        String classifier = args[0];
        args[0] = "";
        System.out.println(evaluateModel(classifier, args));
    } catch (Exception ex) {
        ex.printStackTrace();
        System.err.println(ex.getMessage());
    }
}
