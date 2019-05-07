//}}}  
//{{{ fireBeginRedo() method  
protected void fireBeginRedo() {
    for (BufferUndoListener listener : undoListeners) {
        try {
            listener.beginRedo(this);
        } catch (Throwable t) {
            Log.log(Log.ERROR, this, "Exception while sending buffer begin redo event to " + listener + " :");
            Log.log(Log.ERROR, this, t);
        }
    }
}
