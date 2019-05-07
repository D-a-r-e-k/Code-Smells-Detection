/**
	 * Returns <CODE>true</CODE> if this <CODE>Image</CODE> has the
	 * requisites to be a mask.
	 *
	 * @return <CODE>true</CODE> if this <CODE>Image</CODE> can be a mask
	 */
public boolean isMaskCandidate() {
    if (type == IMGRAW) {
        if (bpc > 0xff)
            return true;
    }
    return colorspace == 1;
}
