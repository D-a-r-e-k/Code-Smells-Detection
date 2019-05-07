/**
     * Returns the inital size of the pool.
     * @see #setInitSize
	 * @since 1.4.6
     */
public int getInitSize() {
    if (maxIdle != -1 && initSize > maxIdle)
        return maxIdle;
    else
        return initSize;
}
