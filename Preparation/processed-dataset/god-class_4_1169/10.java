/** Asks the main jvm for input from the console.
   * @return the console input
   */
public String getConsoleInput() {
    String s = _interactionsModel.getConsoleInput();
    // System.err.println("MainJVM.getConsoleInput() returns '" + s + "'"); 
    return s;
}
