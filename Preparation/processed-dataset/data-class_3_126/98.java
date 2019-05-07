protected void requestExit() {
    _mruFileManager.save();
    setCallSystemExitOnClose(true);
    closeAfterConfirm();
}
