/**
	 * Returns the data of all the embedded sounds in the SlideShow
	 */
public SoundData[] getSoundData() {
    return SoundData.find(_documentRecord);
}
