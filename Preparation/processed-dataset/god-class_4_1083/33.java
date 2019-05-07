void expandFields(PdfFormField field, ArrayList<PdfAnnotation> allAnnots) {
    allAnnots.add(field);
    ArrayList<PdfFormField> kids = field.getKids();
    if (kids != null) {
        for (int k = 0; k < kids.size(); ++k) expandFields(kids.get(k), allAnnots);
    }
}
