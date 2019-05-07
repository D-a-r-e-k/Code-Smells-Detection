/**
     * Removes any usage rights that this PDF may have. Only Adobe can grant usage rights
     * and any PDF modification with iText will invalidate them. Invalidated usage rights may
     * confuse Acrobat and it's advisable to remove them altogether.
     */
public void removeUsageRights() {
    PdfDictionary perms = catalog.getAsDict(PdfName.PERMS);
    if (perms == null)
        return;
    perms.remove(PdfName.UR);
    perms.remove(PdfName.UR3);
    if (perms.size() == 0)
        catalog.remove(PdfName.PERMS);
}
