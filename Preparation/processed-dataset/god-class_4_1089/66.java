//}}}  
//{{{ addBufferUndoListener() method  
/**
	 * Adds a buffer undo listener.
	 * @param listener The listener
	 * @since jEdit 4.3pre18
	 */
public void addBufferUndoListener(BufferUndoListener listener) {
    undoListeners.add(listener);
}
