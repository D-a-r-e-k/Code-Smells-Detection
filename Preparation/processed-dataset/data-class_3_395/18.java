/**
   * Evaluates a classifier with the options given in an array of strings.
   * <p/>
   * 
   * Valid options are:
   * <p/>
   * 
   * -t name of training file <br/>
   * Name of the file with the training data. (required)
   * <p/>
   * 
   * -T name of test file <br/>
   * Name of the file with the test data. If missing a cross-validation is
   * performed.
   * <p/>
   * 
   * -c class index <br/>
   * Index of the class attribute (1, 2, ...; default: last).
   * <p/>
   * 
   * -x number of folds <br/>
   * The number of folds for the cross-validation (default: 10).
   * <p/>
   * 
   * -no-cv <br/>
   * No cross validation. If no test file is provided, no evaluation is done.
   * <p/>
   * 
   * -split-percentage percentage <br/>
   * Sets the percentage for the train/test set split, e.g., 66.
   * <p/>
   * 
   * -preserve-order <br/>
   * Preserves the order in the percentage split instead of randomizing the data
   * first with the seed value ('-s').
   * <p/>
   * 
   * -s seed <br/>
   * Random number seed for the cross-validation and percentage split (default:
   * 1).
   * <p/>
   * 
   * -m file with cost matrix <br/>
   * The name of a file containing a cost matrix.
   * <p/>
   * 
   * -l filename <br/>
   * Loads classifier from the given file. In case the filename ends with
   * ".xml",a PMML file is loaded or, if that fails, options are loaded from
   * XML.
   * <p/>
   * 
   * -d filename <br/>
   * Saves classifier built from the training data into the given file. In case
   * the filename ends with ".xml" the options are saved XML, not the model.
   * <p/>
   * 
   * -v <br/>
   * Outputs no statistics for the training data.
   * <p/>
   * 
   * -o <br/>
   * Outputs statistics only, not the classifier.
   * <p/>
   * 
   * -i <br/>
   * Outputs detailed information-retrieval statistics per class.
   * <p/>
   * 
   * -k <br/>
   * Outputs information-theoretic statistics.
   * <p/>
   * 
   * -classifications
   * "weka.classifiers.evaluation.output.prediction.AbstractOutput + options" <br/>
   * Uses the specified class for generating the classification output. E.g.:
   * weka.classifiers.evaluation.output.prediction.PlainText or :
   * weka.classifiers.evaluation.output.prediction.CSV
   * 
   * -p range <br/>
   * Outputs predictions for test instances (or the train instances if no test
   * instances provided and -no-cv is used), along with the attributes in the
   * specified range (and nothing else). Use '-p 0' if no attributes are
   * desired.
   * <p/>
   * Deprecated: use "-classifications ..." instead.
   * <p/>
   * 
   * -distribution <br/>
   * Outputs the distribution instead of only the prediction in conjunction with
   * the '-p' option (only nominal classes).
   * <p/>
   * Deprecated: use "-classifications ..." instead.
   * <p/>
   * 
   * -no-predictions <br/>
   * Turns off the collection of predictions in order to conserve memory.
   * <p/>
   * 
   * -r <br/>
   * Outputs cumulative margin distribution (and nothing else).
   * <p/>
   * 
   * -g <br/>
   * Only for classifiers that implement "Graphable." Outputs the graph
   * representation of the classifier (and nothing else).
   * <p/>
   * 
   * -xml filename | xml-string <br/>
   * Retrieves the options from the XML-data instead of the command line.
   * <p/>
   * 
   * @param classifier machine learning classifier
   * @param options the array of string containing the options
   * @throws Exception if model could not be evaluated successfully
   * @return a string describing the results
   */
public static String evaluateModel(Classifier classifier, String[] options) throws Exception {
    return weka.classifiers.evaluation.Evaluation.evaluateModel(classifier, options);
}
