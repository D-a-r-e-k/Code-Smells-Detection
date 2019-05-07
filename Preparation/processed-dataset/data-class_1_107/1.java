/**
    * Starts up the thread that does the actual work being monitored.
    *
    * @param worker thread the dialog is attached to
    */
public void startThread(final Thread worker) {
    this.worker = worker;
    initComponents();
    totalProgressBar.setIndeterminate(true);
    pack();
    setLocationRelativeTo(getParent());
    worker.start();
    //      //create a waiter thread: waits until task is finished, then closes dialogue.  
    //      new Thread("ProgressDialog.startThread") {  
    //         public void run() {  
    //            try {  
    //               worker.join();  
    //            } catch (InterruptedException e) {  
    //               //no action  
    //            }  
    //            setVisible(false);  
    //            dispose();  
    //            JagGenerator.logToConsole("2222222222222222222222222");  
    //  
    //         }  
    //      }.start();  
    setVisible(true);
    JagGenerator.logToConsole("33333333333333333333333");
}
