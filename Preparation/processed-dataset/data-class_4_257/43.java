/**
     * The local destination to where a local goto with the same
     * name will jump to.
     * @param name the name of this local destination
     * @param destination the <CODE>PdfDestination</CODE> with the jump coordinates
     * @return <CODE>true</CODE> if the local destination was added,
     * <CODE>false</CODE> if a local destination with the same name
     * already existed
     */
boolean localDestination(String name, PdfDestination destination) {
    Destination dest = localDestinations.get(name);
    if (dest == null)
        dest = new Destination();
    if (dest.destination != null)
        return false;
    dest.destination = destination;
    localDestinations.put(name, dest);
    if (!destination.hasPage())
        destination.addPage(writer.getCurrentPage());
    return true;
}
