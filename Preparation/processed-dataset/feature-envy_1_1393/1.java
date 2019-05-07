private String _formatPackage(String sPackage_) {
    if (sPackage_.equals("")) {
        return ".";
    }
    return sPackage_.substring(0, sPackage_.length() - 1);
}
