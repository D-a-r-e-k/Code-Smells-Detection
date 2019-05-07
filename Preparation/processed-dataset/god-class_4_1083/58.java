void setThumbnail(Image image, int page) throws PdfException, DocumentException {
    PdfIndirectReference thumb = getImageReference(addDirectImageSimple(image));
    reader.resetReleasePage();
    PdfDictionary dic = reader.getPageN(page);
    dic.put(PdfName.THUMB, thumb);
    reader.resetReleasePage();
}
