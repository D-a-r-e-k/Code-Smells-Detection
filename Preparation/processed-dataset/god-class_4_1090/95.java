//{{{ Event firing methods  
//{{{ fireFoldLevelChanged() method  
protected void fireFoldLevelChanged(int start, int end) {
    for (int i = 0; i < bufferListeners.size(); i++) {
        BufferListener listener = getListener(i);
        try {
            listener.foldLevelChanged(this, start, end);
        } catch (Throwable t) {
            Log.log(Log.ERROR, this, "Exception while sending buffer event to " + listener + " :");
            Log.log(Log.ERROR, this, t);
        }
    }
}
