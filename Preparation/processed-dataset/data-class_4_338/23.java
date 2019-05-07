//  public void hide() { 
//   System.err.println("*** Called hide ***"); 
//   if (_open) 
//   _frame.uninstallFindReplaceDialog(this); 
//   //super.hide(); 
//   } 
//  private ContinueCommand CONFIRM_CONTINUE = new ContinueCommand() { 
//    public boolean shouldContinue() { 
//      String text = "The search has reached the end of the document.\n" + 
//        "Continue searching from the start?"; 
//      int rc = JOptionPane.showConfirmDialog(FindReplacePanel.this, 
//                                             text, 
//                                             "Continue search?", 
//                                             JOptionPane.YES_NO_OPTION); 
//       
//      switch (rc) { 
//        case JOptionPane.YES_OPTION: 
//          return true; 
//        case JOptionPane.NO_OPTION: 
//          return false; 
//        default: 
//          throw new RuntimeException("Invalid rc: " + rc); 
//      } 
//       
//    } 
//  }; 
/** We lost ownership of what we put in the clipboard. */
public void lostOwnership(Clipboard clipboard, Transferable contents) {
}
