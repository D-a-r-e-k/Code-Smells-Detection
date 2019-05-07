//}}}  
//{{{ removeBufferUndoListener() method  
/**
	 * Removes a buffer undo listener.
	 * @param listener The listener
	 * @since jEdit 4.3pre18
	 */
public void removeBufferUndoListener(BufferUndoListener listener) {
    undoListeners.remove(listener);
}
