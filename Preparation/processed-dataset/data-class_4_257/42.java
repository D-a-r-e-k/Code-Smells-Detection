PdfAction getLocalGotoAction(String name) {
    PdfAction action;
    Destination dest = localDestinations.get(name);
    if (dest == null)
        dest = new Destination();
    if (dest.action == null) {
        if (dest.reference == null) {
            dest.reference = writer.getPdfIndirectReference();
        }
        action = new PdfAction(dest.reference);
        dest.action = action;
        localDestinations.put(name, dest);
    } else {
        action = dest.action;
    }
    return action;
}
