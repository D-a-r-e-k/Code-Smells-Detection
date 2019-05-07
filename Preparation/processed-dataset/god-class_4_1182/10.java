/**
	 * Return whether or not the SourceSinkInfo was created
	 * based on the results of type qualifiers computed for a called method
	 * (and not explicitly annotating the called method).
	 * 
	 * @return true if the SourceSinkInfo results from
	 *         computed type qualifiers for a called method,
	 *         false otherwise
	 */
public boolean getInterproc() {
    return interproc;
}
