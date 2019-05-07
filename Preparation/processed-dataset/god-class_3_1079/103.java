/**
	 * Sets the compression level to be used if the image is written as a compressed stream.
	 * @param compressionLevel a value between 0 (best speed) and 9 (best compression)
     * @since	2.1.3
	 */
public void setCompressionLevel(int compressionLevel) {
    if (compressionLevel < PdfStream.NO_COMPRESSION || compressionLevel > PdfStream.BEST_COMPRESSION)
        this.compressionLevel = PdfStream.DEFAULT_COMPRESSION;
    else
        this.compressionLevel = compressionLevel;
}
