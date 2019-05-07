/**
	 * Sets the explicit masking.
	 *
	 * @param mask
	 *            the mask to be applied
	 * @throws DocumentException
	 *             on error
	 */
public void setImageMask(Image mask) throws DocumentException {
    if (this.mask)
        throw new DocumentException(MessageLocalization.getComposedMessage("an.image.mask.cannot.contain.another.image.mask"));
    if (!mask.mask)
        throw new DocumentException(MessageLocalization.getComposedMessage("the.image.mask.is.not.a.mask.did.you.do.makemask"));
    imageMask = mask;
    smask = mask.bpc > 1 && mask.bpc <= 8;
}
