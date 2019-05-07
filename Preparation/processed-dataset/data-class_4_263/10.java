//}}}  
//}}}  
//{{{ Thread safety  
//{{{ readLock() method  
/**
	 * The buffer is guaranteed not to change between calls to
	 * {@link #readLock()} and {@link #readUnlock()}.
	 */
public void readLock() {
    lock.readLock().lock();
}
