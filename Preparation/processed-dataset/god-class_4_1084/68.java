void setThumbnail(Image image) throws PdfException, DocumentException {
    thumb = writer.getImageReference(writer.addDirectImageSimple(image));
}
