// returns a non-null ZipEntry object if current language pack 
// contains requested file 
public static ZipEntry languagePackContains(String fileName) {
    for (int i = 0; i < languageEntries.size(); i++) {
        ZipEntry entry = (ZipEntry) languageEntries.get(i);
        if (entry.getName().equalsIgnoreCase(fileName))
            return entry;
    }
    return null;
}
