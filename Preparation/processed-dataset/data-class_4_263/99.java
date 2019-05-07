//}}}  
//{{{ firePreContentRemoved() method  
protected void firePreContentRemoved(int startLine, int offset, int numLines, int length) {
    for (int i = 0; i < bufferListeners.size(); i++) {
        BufferListener listener = getListener(i);
        try {
            listener.preContentRemoved(this, startLine, offset, numLines, length);
        } catch (Throwable t) {
            Log.log(Log.ERROR, this, "Exception while sending buffer event to " + listener + " :");
            Log.log(Log.ERROR, this, t);
        }
    }
}
