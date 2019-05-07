//}}}  
//{{{ readUnlock() method  
/**
	 * The buffer is guaranteed not to change between calls to
	 * {@link #readLock()} and {@link #readUnlock()}.
	 */
public void readUnlock() {
    lock.readLock().unlock();
}
