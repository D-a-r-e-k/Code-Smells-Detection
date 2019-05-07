/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
@Override
public boolean equals(Object obj) {
    if (obj == null || obj.getClass() != this.getClass()) {
        return false;
    }
    SourceSinkInfo other = (SourceSinkInfo) obj;
    return this.type.equals(other.type) && this.vn.equals(other.vn) && this.location.equals(other.location);
}
