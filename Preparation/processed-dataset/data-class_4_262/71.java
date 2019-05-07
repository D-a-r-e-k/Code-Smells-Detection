//}}}  
//{{{ fireEndUndo() method  
protected void fireEndUndo() {
    for (BufferUndoListener listener : undoListeners) {
        try {
            listener.endUndo(this);
        } catch (Throwable t) {
            Log.log(Log.ERROR, this, "Exception while sending buffer undo event to " + listener + " :");
            Log.log(Log.ERROR, this, t);
        }
    }
}
