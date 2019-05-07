//}}}  
//{{{ writeLock() method  
/**
	 * Attempting to obtain read lock will block between calls to
	 * {@link #writeLock()} and {@link #writeUnlock()}.
	 */
public void writeLock() {
    lock.writeLock().lock();
}
