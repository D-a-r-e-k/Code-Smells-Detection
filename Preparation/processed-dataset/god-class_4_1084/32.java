/**
     * Recursive method to update the count in the outlines.
     */
void traverseOutlineCount(PdfOutline outline) {
    ArrayList<PdfOutline> kids = outline.getKids();
    PdfOutline parent = outline.parent();
    if (kids.isEmpty()) {
        if (parent != null) {
            parent.setCount(parent.getCount() + 1);
        }
    } else {
        for (int k = 0; k < kids.size(); ++k) {
            traverseOutlineCount(kids.get(k));
        }
        if (parent != null) {
            if (outline.isOpen()) {
                parent.setCount(outline.getCount() + parent.getCount() + 1);
            } else {
                parent.setCount(parent.getCount() + 1);
                outline.setCount(-outline.getCount());
            }
        }
    }
}
