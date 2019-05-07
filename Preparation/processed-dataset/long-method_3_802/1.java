/**
	 *
	 */
protected void exportReportToStream(Writer writer) throws JRException, IOException {
    tempBodyWriter = new StringWriter();
    tempStyleWriter = new StringWriter();
    styleCache = new XmlssStyleCache(tempStyleWriter, fontMap);
    sheetNamesMap = new HashMap();
    sheetNamesMap.put("Page", Integer.valueOf(0));
    // in order to skip first sheet name that would have no index 
    for (reportIndex = 0; reportIndex < jasperPrintList.size(); reportIndex++) {
        setJasperPrint((JasperPrint) jasperPrintList.get(reportIndex));
        defaultFont = new JRBasePrintText(jasperPrint.getDefaultStyleProvider());
        List pages = jasperPrint.getPages();
        if (pages != null && pages.size() > 0) {
            if (isModeBatch) {
                startPageIndex = 0;
                endPageIndex = pages.size() - 1;
            }
            if (isOnePagePerSheet) {
                for (int pageIndex = startPageIndex; pageIndex <= endPageIndex; pageIndex++) {
                    if (Thread.interrupted()) {
                        throw new JRException("Current thread interrupted.");
                    }
                    JRPrintPage page = (JRPrintPage) pages.get(pageIndex);
                    if (sheetNames != null && sheetIndex < sheetNames.length) {
                        tempBodyWriter.write("<Worksheet ss:Name=\"" + getSheetName(sheetNames[sheetIndex]) + "\">\n");
                    } else {
                        tempBodyWriter.write("<Worksheet ss:Name=\"" + getSheetName("Page") + "\">\n");
                    }
                    // we need to count all sheets generated for all exported documents 
                    sheetIndex++;
                    tempBodyWriter.write("<Table>\n");
                    exportPage(page, null, 0, null, true);
                    tempBodyWriter.write("</Table>\n");
                    closeWorksheet();
                }
            } else {
                // Create the sheet before looping. 
                if (sheetNames != null && sheetIndex < sheetNames.length) {
                    tempBodyWriter.write("<Worksheet ss:Name=\"" + getSheetName(sheetNames[sheetIndex]) + "\">\n");
                } else {
                    tempBodyWriter.write("<Worksheet ss:Name=\"" + getSheetName(jasperPrint.getName()) + "\">\n");
                }
                tempBodyWriter.write("<Table>\n");
                // we need to count all sheets generated for all exported documents 
                sheetIndex++;
                /*
					 * Make a pass and calculate the X cuts for all pages on this sheet.
					 * The Y cuts can be calculated as each page is exported.
					 */
                CutsInfo xCuts = JRGridLayout.calculateXCuts(nature, pages, startPageIndex, endPageIndex, jasperPrint.getPageWidth(), globalOffsetX);
                //clear the filter's internal cache that might have built up 
                if (filter instanceof ResetableExporterFilter) {
                    ((ResetableExporterFilter) filter).reset();
                }
                int startRow = 0;
                for (int pageIndex = startPageIndex; pageIndex <= endPageIndex; pageIndex++) {
                    if (Thread.interrupted()) {
                        throw new JRException("Current thread interrupted.");
                    }
                    JRPrintPage page = (JRPrintPage) pages.get(pageIndex);
                    startRow = exportPage(page, xCuts, startRow, null, pageIndex == startPageIndex);
                }
                //					if (isRemoveEmptySpaceBetweenColumns) 
                //					{ 
                //						//FIXME: to remove empty columns when isRemoveEmptySpaceBetweenColumns 
                //						//removeEmptyColumns(xCuts); 
                //					} 
                tempBodyWriter.write("</Table>\n");
                closeWorksheet();
            }
        }
    }
    tempBodyWriter.flush();
    tempStyleWriter.flush();
    tempBodyWriter.close();
    tempStyleWriter.close();
    /*   */
    XmlssContentBuilder xmlssContentBuilder = new XmlssContentBuilder(writer, tempStyleWriter, tempBodyWriter);
    xmlssContentBuilder.build();
}
