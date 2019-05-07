void correctAcroFieldPages(int page) {
    if (acroFields == null)
        return;
    if (page > reader.getNumberOfPages())
        return;
    HashMap<String, Item> fields = acroFields.getFields();
    for (AcroFields.Item item : fields.values()) {
        for (int k = 0; k < item.size(); ++k) {
            int p = item.getPage(k).intValue();
            if (p >= page)
                item.forcePage(k, p + 1);
        }
    }
}
