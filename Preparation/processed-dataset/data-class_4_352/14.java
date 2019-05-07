/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
@Override
public String toString() {
    return type.toString() + "@" + location.toCompactString() + "[vn=" + vn.getNumber() + ",when=" + when + "]";
}
