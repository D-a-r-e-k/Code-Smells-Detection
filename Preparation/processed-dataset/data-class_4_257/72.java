/**
	 * Method added by Pelikan Stephan
	 */
public void clearTextWrap() {
    float tmpHeight = imageEnd - currentHeight;
    if (line != null) {
        tmpHeight += line.height();
    }
    if (imageEnd > -1 && tmpHeight > 0) {
        carriageReturn();
        currentHeight += tmpHeight;
    }
}
