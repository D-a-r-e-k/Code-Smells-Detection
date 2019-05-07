/** Performs deferred initialization.  Only runs in the event thread.  Some of this code occasionally generated swing
   *  exceptions  when run in themain thread as part of MainFrame construction prior to making MainFrame visible. */
public void setUp() {
    assert EventQueue.isDispatchThread();
    /* Set up _fileOptionChooser, _browserChooser, and _dirChooser.  The line _dirChooser.setSelectedFile(...) caused
     * java.lang.ArrayIndexOutOfBoundsException within swing code in a JUnit test setUp() routine that constructed a
     * a MainFrame.
     */
    _fileOptionChooser.setDialogTitle("Select");
    _fileOptionChooser.setApproveButtonText("Select");
    _fileOptionChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
    _fileOptionChooser.setFileFilter(ClassPathFilter.ONLY);
    _jarChooser.setDialogTitle("Select");
    _jarChooser.setApproveButtonText("Select");
    _jarChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    _jarChooser.setFileFilter(ClassPathFilter.ONLY);
    _browserChooser.setDialogTitle("Select Web Browser");
    _browserChooser.setApproveButtonText("Select");
    _browserChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    _dirChooser.setSelectedFile(_getWorkDir());
    _dirChooser.setDialogTitle("Select");
    _dirChooser.setApproveButtonText("Select");
    _dirChooser.setMultiSelectionEnabled(false);
}
