/**
	 * Sets the animation dictionary describing the preferred method
	 * that conforming readers should use to drive keyframe animations
	 * present in this artwork.
	 * @param	animation	a RichMediaAnimation dictionary
	 */
public void setAnimation(RichMediaAnimation animation) {
    put(PdfName.ANIMATION, animation);
}
