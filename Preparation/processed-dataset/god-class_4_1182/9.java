/**
	 * Set the SourceSinkInfo as having been created based
	 * on the results of type qualifiers computed for a called method
	 * (and not explicitly annotating the called method).
	 * 
	 * @param interproc true if the SourceSinkInfo results from
	 *                  computed type qualifiers for a called method,
	 *                  false otherwise
	 */
public void setInterproc(boolean interproc) {
    this.interproc = interproc;
}
