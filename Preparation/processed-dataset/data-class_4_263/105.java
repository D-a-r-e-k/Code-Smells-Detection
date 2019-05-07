//}}}  
//{{{ fireFoldHandlerChanged() method  
protected void fireFoldHandlerChanged() {
    for (int i = 0; i < bufferListeners.size(); i++) {
        BufferListener listener = getListener(i);
        try {
            listener.foldHandlerChanged(this);
        } catch (Throwable t) {
            Log.log(Log.ERROR, this, "Exception while sending buffer event to " + listener + " :");
            Log.log(Log.ERROR, this, t);
        }
    }
}
