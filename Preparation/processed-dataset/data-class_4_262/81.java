/** Update the buffer's members with the current hash and length,
	 *  for later comparison.
	 */
private void updateHash() {
    initialLength = getLength();
    md5hash = calculateHash();
}
