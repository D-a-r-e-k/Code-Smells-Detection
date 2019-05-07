void applyRotation(PdfDictionary pageN, ByteBuffer out) {
    if (!rotateContents)
        return;
    Rectangle page = reader.getPageSizeWithRotation(pageN);
    int rotation = page.getRotation();
    switch(rotation) {
        case 90:
            out.append(PdfContents.ROTATE90);
            out.append(page.getTop());
            out.append(' ').append('0').append(PdfContents.ROTATEFINAL);
            break;
        case 180:
            out.append(PdfContents.ROTATE180);
            out.append(page.getRight());
            out.append(' ');
            out.append(page.getTop());
            out.append(PdfContents.ROTATEFINAL);
            break;
        case 270:
            out.append(PdfContents.ROTATE270);
            out.append('0').append(' ');
            out.append(page.getRight());
            out.append(PdfContents.ROTATEFINAL);
            break;
    }
}
