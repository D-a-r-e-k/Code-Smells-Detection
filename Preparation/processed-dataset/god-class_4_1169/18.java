//  /** Notifies the main jvm that an assignment has been made in the given debug interpreter. 
//   * Does not notify on declarations. 
//   * 
//   * This method is not currently necessary, since we don't copy back values in a debug interpreter until the thread 
//   * has resumed. 
//   * 
//   * @param name the name of the debug interpreter 
//   */ 
//   public void notifyDebugInterpreterAssignment(String name) { 
//   } 
/*
   * === Local getters and setters ===
   */
/** Provides an object to listen to interactions-related events. */
public void setInteractionsModel(InteractionsModelCallback model) {
    _interactionsModel = model;
}
