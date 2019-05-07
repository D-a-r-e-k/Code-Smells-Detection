/**
     * Create a QIF file filter
     * @return A FileFilter for QIF files
     */
public FileFilter fileFilter() {
    return new QifFileFilter();
}
