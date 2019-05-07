/**
	 *
	 * @return the number of rows added.
	 */
protected int exportPage(JRPrintPage page, CutsInfo xCuts, int startRow, JRPrintElementIndex frameIndex, boolean isNewSheet) throws JRException {
    try {
        JRGridLayout layout = new JRGridLayout(nature, page.getElements(), jasperPrint.getPageWidth(), jasperPrint.getPageHeight(), globalOffsetX, globalOffsetY, xCuts);
        JRExporterGridCell grid[][] = layout.getGrid();
        boolean createXCuts = (xCuts == null);
        if (createXCuts) {
            xCuts = layout.getXCuts();
        }
        CutsInfo yCuts = layout.getYCuts();
        int skippedRows = 0;
        int rowIndex = startRow;
        XmlssTableBuilder tableBuilder = frameIndex == null ? new XmlssTableBuilder(reportIndex, pageIndex, tempBodyWriter, tempStyleWriter) : new XmlssTableBuilder(frameIndex.toString(), tempBodyWriter, tempStyleWriter);
        if (isNewSheet) {
            buildColumns(xCuts, tableBuilder);
        }
        for (int y = 0; y < grid.length; y++) {
            rowIndex = y - skippedRows + startRow;
            //if number of rows is too large a new sheet is created and populated with remaining rows 
            if (maxRowsPerSheet > 0 && rowIndex >= maxRowsPerSheet) {
                tableBuilder.buildTableFooter();
                closeWorksheet();
                tempBodyWriter.write("<Worksheet ss:Name=\"" + getSheetName(currentSheetName) + "\">\n");
                tableBuilder.buildTableHeader();
                buildColumns(xCuts, tableBuilder);
                startRow = 0;
                rowIndex = 0;
                skippedRows = y;
            }
            if (yCuts.isCutNotEmpty(y) || ((!isRemoveEmptySpaceBetweenRows || yCuts.isCutSpanned(y)) && !isCollapseRowSpan)) {
                JRExporterGridCell[] gridRow = grid[y];
                int emptyCellColSpan = 0;
                int emptyCellRowSpan = 0;
                int emptyCellWidth = 0;
                int rowHeight = isCollapseRowSpan ? JRGridLayout.getMaxRowHeight(gridRow) : JRGridLayout.getRowHeight(gridRow);
                tableBuilder.buildRowHeader(rowIndex, rowHeight);
                int emptyCols = 0;
                for (int colIndex = 0; colIndex < gridRow.length; colIndex++) {
                    emptyCols += (isRemoveEmptySpaceBetweenColumns && (!(xCuts.isCutNotEmpty(colIndex) || xCuts.isCutSpanned(colIndex))) ? 1 : 0);
                    JRExporterGridCell gridCell = gridRow[colIndex];
                    if (gridCell.getWrapper() != null) {
                        if (emptyCellColSpan > 0) {
                            tableBuilder.buildEmptyCell(emptyCellColSpan, emptyCellRowSpan);
                            emptyCellColSpan = 0;
                            emptyCellWidth = 0;
                        }
                        JRPrintElement element = gridCell.getWrapper().getElement();
                        //							if (element instanceof JRPrintLine) 
                        //							{ 
                        //								//exportLine((JRPrintLine)element, gridCell, colIndex, rowIndex); 
                        //							} 
                        //							else if (element instanceof JRPrintRectangle) 
                        //							{ 
                        //								//exportRectangle((JRPrintRectangle)element, gridCell, colIndex, rowIndex); 
                        //							} 
                        //							else if (element instanceof JRPrintEllipse) 
                        //							{ 
                        //								//exportRectangle((JRPrintEllipse)element, gridCell, colIndex, rowIndex); 
                        //							} 
                        //							else if (element instanceof JRPrintImage) 
                        //							{ 
                        //								//exportImage((JRPrintImage) element, gridCell, colIndex, rowIndex, emptyCols); 
                        //							} 
                        if (element instanceof JRPrintText) {
                            exportText(tableBuilder, (JRPrintText) element, gridCell);
                        }
                        //							else if (element instanceof JRPrintFrame) 
                        //							{ 
                        //								//exportFrame(tableBuilder, (JRPrintFrame)element, gridCell); 
                        //							} 
                        colIndex += gridCell.getColSpan() - 1;
                    } else {
                        emptyCellColSpan++;
                        emptyCellWidth += gridCell.getWidth();
                    }
                }
                if (emptyCellColSpan > 0) {
                    tableBuilder.buildEmptyCell(emptyCellColSpan, emptyCellRowSpan);
                    emptyCellColSpan = 0;
                    emptyCellWidth = 0;
                }
                tableBuilder.buildRowFooter();
                //increment row index to return proper value 
                ++rowIndex;
            } else {
                skippedRows++;
            }
        }
        //			if (createXCuts && isRemoveEmptySpaceBetweenColumns) 
        //			{ 
        //				//FIXME: to remove empty columns when isRemoveEmptySpaceBetweenColumns 
        ////				removeEmptyColumns(xCuts); 
        //			} 
        if (progressMonitor != null) {
            progressMonitor.afterPageExport();
        }
        // Return the number of rows added 
        return rowIndex;
    } catch (IOException e) {
        throw new JRException(e);
    }
}
