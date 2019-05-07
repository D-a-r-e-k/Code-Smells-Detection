private void importMindmanagerFile(File file) {
    // from e455. Retrieving a Compressed File from a ZIP File 
    // http://javaalmanac.com/egs/java.util.zip/GetZip.html 
    try {
        // Open the ZIP file 
        ZipInputStream in = new ZipInputStream(new FileInputStream(file));
        while (in.available() != 0) {
            ZipEntry entry = in.getNextEntry();
            if (!entry.getName().equals("Document.xml")) {
                continue;
            }
            // now apply the transformation: 
            // search for xslt file: 
            String xsltFileName = "accessories/mindmanager2mm.xsl";
            URL xsltUrl = getResource(xsltFileName);
            if (xsltUrl == null) {
                logger.severe("Can't find " + xsltFileName + " as resource.");
                throw new IllegalArgumentException("Can't find " + xsltFileName + " as resource.");
            }
            InputStream xsltFile = xsltUrl.openStream();
            String xml = transForm(new StreamSource(in), xsltFile);
            if (xml != null) {
                // now start a new map with this string: 
                File tempFile = File.createTempFile(file.getName(), freemind.main.FreeMindCommon.FREEMIND_FILE_EXTENSION, file.getParentFile());
                FileWriter fw = new FileWriter(tempFile);
                fw.write(xml);
                fw.close();
                getController().load(tempFile);
            }
            break;
        }
    } catch (IOException e) {
        freemind.main.Resources.getInstance().logException(e);
    } catch (XMLParseException e) {
        freemind.main.Resources.getInstance().logException(e);
    }
}
