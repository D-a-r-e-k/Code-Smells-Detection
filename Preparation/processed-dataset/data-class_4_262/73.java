//}}}  
//{{{ fireEndRedo() method  
protected void fireEndRedo() {
    for (BufferUndoListener listener : undoListeners) {
        try {
            listener.endRedo(this);
        } catch (Throwable t) {
            Log.log(Log.ERROR, this, "Exception while sending buffer end redo event to " + listener + " :");
            Log.log(Log.ERROR, this, t);
        }
    }
}
