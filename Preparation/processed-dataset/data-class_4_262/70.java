//}}}  
//}}}  
//{{{ Protected members  
//{{{ fireBeginUndo() method  
protected void fireBeginUndo() {
    for (BufferUndoListener listener : undoListeners) {
        try {
            listener.beginUndo(this);
        } catch (Throwable t) {
            Log.log(Log.ERROR, this, "Exception while sending buffer undo event to " + listener + " :");
            Log.log(Log.ERROR, this, t);
        }
    }
}
