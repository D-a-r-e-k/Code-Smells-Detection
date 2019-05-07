//}}}  
//{{{ getIndex() method  
/**
	 * Returns the position of this buffer in the buffer list.
	 */
public int getIndex() {
    int count = 0;
    Buffer buffer = prev;
    while (true) {
        if (buffer == null)
            break;
        count++;
        buffer = buffer.prev;
    }
    return count;
}
