//}}}  
//{{{ writeUnlock() method  
/**
	 * Attempting to obtain read lock will block between calls to
	 * {@link #writeLock()} and {@link #writeUnlock()}.
	 */
public void writeUnlock() {
    lock.writeLock().unlock();
}
