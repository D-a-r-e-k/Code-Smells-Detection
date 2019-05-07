/**
	 * Make this <CODE>Image</CODE> a mask.
	 *
	 * @throws DocumentException
	 *             if this <CODE>Image</CODE> can not be a mask
	 */
public void makeMask() throws DocumentException {
    if (!isMaskCandidate())
        throw new DocumentException(MessageLocalization.getComposedMessage("this.image.can.not.be.an.image.mask"));
    mask = true;
}
