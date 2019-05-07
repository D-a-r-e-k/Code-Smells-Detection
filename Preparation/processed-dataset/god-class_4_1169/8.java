/*
   * === MainJVMRemoteI methods ===
   */
// TODO: export other objects, such as the interactionsModel, thus avoiding the need to delegate here? 
/** Forwards a call to System.err from InterpreterJVM to the local InteractionsModel.
    * @param s String that was printed in the other JVM
    */
public void systemErrPrint(String s) {
    debug.logStart();
    _interactionsModel.replSystemErrPrint(s);
    //    Utilities.clearEventQueue();               // wait for event queue task to complete 
    debug.logEnd();
}
