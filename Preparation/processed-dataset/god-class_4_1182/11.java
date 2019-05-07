/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
public int compareTo(SourceSinkInfo o) {
    return this.location.compareTo(o.location);
}
