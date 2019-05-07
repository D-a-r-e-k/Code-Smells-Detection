/** Forwards a call to System.out from InterpreterJVM to the local InteractionsModel.
    * @param s String that was printed in the other JVM
    */
public void systemOutPrint(String s) {
    debug.logStart();
    _interactionsModel.replSystemOutPrint(s);
    //    Utilities.clearEventQueue();                // wait for event queue task to complete 
    debug.logEnd();
}
