/**
	 * If this dictionary refers to an intermediate target, you can
	 * add the next target in the sequence.
	 * @param nested	the next target in the sequence
	 */
public void setAdditionalPath(PdfTargetDictionary nested) {
    put(PdfName.T, nested);
}
