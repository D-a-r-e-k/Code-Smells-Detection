protected int goComposite(boolean simulate) throws DocumentException {
    if (!rectangularMode)
        throw new DocumentException(MessageLocalization.getComposedMessage("irregular.columns.are.not.supported.in.composite.mode"));
    linesWritten = 0;
    descender = 0;
    boolean firstPass = adjustFirstLine;
    main_loop: while (true) {
        if (compositeElements.isEmpty())
            return NO_MORE_TEXT;
        Element element = compositeElements.getFirst();
        if (element.type() == Element.PARAGRAPH) {
            Paragraph para = (Paragraph) element;
            int status = 0;
            for (int keep = 0; keep < 2; ++keep) {
                float lastY = yLine;
                boolean createHere = false;
                if (compositeColumn == null) {
                    compositeColumn = new ColumnText(canvas);
                    compositeColumn.setAlignment(para.getAlignment());
                    compositeColumn.setIndent(para.getIndentationLeft() + para.getFirstLineIndent());
                    compositeColumn.setExtraParagraphSpace(para.getExtraParagraphSpace());
                    compositeColumn.setFollowingIndent(para.getIndentationLeft());
                    compositeColumn.setRightIndent(para.getIndentationRight());
                    compositeColumn.setLeading(para.getLeading(), para.getMultipliedLeading());
                    compositeColumn.setRunDirection(runDirection);
                    compositeColumn.setArabicOptions(arabicOptions);
                    compositeColumn.setSpaceCharRatio(spaceCharRatio);
                    compositeColumn.addText(para);
                    if (!firstPass) {
                        yLine -= para.getSpacingBefore();
                    }
                    createHere = true;
                }
                compositeColumn.setUseAscender(firstPass ? useAscender : false);
                compositeColumn.leftX = leftX;
                compositeColumn.rightX = rightX;
                compositeColumn.yLine = yLine;
                compositeColumn.rectangularWidth = rectangularWidth;
                compositeColumn.rectangularMode = rectangularMode;
                compositeColumn.minY = minY;
                compositeColumn.maxY = maxY;
                boolean keepCandidate = para.getKeepTogether() && createHere && !firstPass;
                status = compositeColumn.go(simulate || keepCandidate && keep == 0);
                lastX = compositeColumn.getLastX();
                updateFilledWidth(compositeColumn.filledWidth);
                if ((status & NO_MORE_TEXT) == 0 && keepCandidate) {
                    compositeColumn = null;
                    yLine = lastY;
                    return NO_MORE_COLUMN;
                }
                if (simulate || !keepCandidate)
                    break;
                if (keep == 0) {
                    compositeColumn = null;
                    yLine = lastY;
                }
            }
            firstPass = false;
            yLine = compositeColumn.yLine;
            linesWritten += compositeColumn.linesWritten;
            descender = compositeColumn.descender;
            if ((status & NO_MORE_TEXT) != 0) {
                compositeColumn = null;
                compositeElements.removeFirst();
                yLine -= para.getSpacingAfter();
            }
            if ((status & NO_MORE_COLUMN) != 0) {
                return NO_MORE_COLUMN;
            }
        } else if (element.type() == Element.LIST) {
            com.itextpdf.text.List list = (com.itextpdf.text.List) element;
            ArrayList<Element> items = list.getItems();
            ListItem item = null;
            float listIndentation = list.getIndentationLeft();
            int count = 0;
            Stack<Object[]> stack = new Stack<Object[]>();
            for (int k = 0; k < items.size(); ++k) {
                Object obj = items.get(k);
                if (obj instanceof ListItem) {
                    if (count == listIdx) {
                        item = (ListItem) obj;
                        break;
                    } else
                        ++count;
                } else if (obj instanceof com.itextpdf.text.List) {
                    stack.push(new Object[] { list, new Integer(k), new Float(listIndentation) });
                    list = (com.itextpdf.text.List) obj;
                    items = list.getItems();
                    listIndentation += list.getIndentationLeft();
                    k = -1;
                    continue;
                }
                if (k == items.size() - 1) {
                    if (!stack.isEmpty()) {
                        Object objs[] = stack.pop();
                        list = (com.itextpdf.text.List) objs[0];
                        items = list.getItems();
                        k = ((Integer) objs[1]).intValue();
                        listIndentation = ((Float) objs[2]).floatValue();
                    }
                }
            }
            int status = 0;
            for (int keep = 0; keep < 2; ++keep) {
                float lastY = yLine;
                boolean createHere = false;
                if (compositeColumn == null) {
                    if (item == null) {
                        listIdx = 0;
                        compositeElements.removeFirst();
                        continue main_loop;
                    }
                    compositeColumn = new ColumnText(canvas);
                    compositeColumn.setUseAscender(firstPass ? useAscender : false);
                    compositeColumn.setAlignment(item.getAlignment());
                    compositeColumn.setIndent(item.getIndentationLeft() + listIndentation + item.getFirstLineIndent());
                    compositeColumn.setExtraParagraphSpace(item.getExtraParagraphSpace());
                    compositeColumn.setFollowingIndent(compositeColumn.getIndent());
                    compositeColumn.setRightIndent(item.getIndentationRight() + list.getIndentationRight());
                    compositeColumn.setLeading(item.getLeading(), item.getMultipliedLeading());
                    compositeColumn.setRunDirection(runDirection);
                    compositeColumn.setArabicOptions(arabicOptions);
                    compositeColumn.setSpaceCharRatio(spaceCharRatio);
                    compositeColumn.addText(item);
                    if (!firstPass) {
                        yLine -= item.getSpacingBefore();
                    }
                    createHere = true;
                }
                compositeColumn.leftX = leftX;
                compositeColumn.rightX = rightX;
                compositeColumn.yLine = yLine;
                compositeColumn.rectangularWidth = rectangularWidth;
                compositeColumn.rectangularMode = rectangularMode;
                compositeColumn.minY = minY;
                compositeColumn.maxY = maxY;
                boolean keepCandidate = item.getKeepTogether() && createHere && !firstPass;
                status = compositeColumn.go(simulate || keepCandidate && keep == 0);
                lastX = compositeColumn.getLastX();
                updateFilledWidth(compositeColumn.filledWidth);
                if ((status & NO_MORE_TEXT) == 0 && keepCandidate) {
                    compositeColumn = null;
                    yLine = lastY;
                    return NO_MORE_COLUMN;
                }
                if (simulate || !keepCandidate)
                    break;
                if (keep == 0) {
                    compositeColumn = null;
                    yLine = lastY;
                }
            }
            firstPass = false;
            yLine = compositeColumn.yLine;
            linesWritten += compositeColumn.linesWritten;
            descender = compositeColumn.descender;
            if (!Float.isNaN(compositeColumn.firstLineY) && !compositeColumn.firstLineYDone) {
                if (!simulate)
                    showTextAligned(canvas, Element.ALIGN_LEFT, new Phrase(item.getListSymbol()), compositeColumn.leftX + listIndentation, compositeColumn.firstLineY, 0);
                compositeColumn.firstLineYDone = true;
            }
            if ((status & NO_MORE_TEXT) != 0) {
                compositeColumn = null;
                ++listIdx;
                yLine -= item.getSpacingAfter();
            }
            if ((status & NO_MORE_COLUMN) != 0)
                return NO_MORE_COLUMN;
        } else if (element.type() == Element.PTABLE) {
            // don't write anything in the current column if there's no more space available 
            if (yLine < minY || yLine > maxY)
                return NO_MORE_COLUMN;
            // get the PdfPTable element 
            PdfPTable table = (PdfPTable) element;
            // we ignore tables without a body 
            if (table.size() <= table.getHeaderRows()) {
                compositeElements.removeFirst();
                continue;
            }
            // offsets 
            float yTemp = yLine;
            if (!firstPass && listIdx == 0)
                yTemp -= table.spacingBefore();
            float yLineWrite = yTemp;
            // don't write anything in the current column if there's no more space available 
            if (yTemp < minY || yTemp > maxY)
                return NO_MORE_COLUMN;
            // coordinates 
            currentLeading = 0;
            float x1 = leftX;
            float tableWidth;
            if (table.isLockedWidth()) {
                tableWidth = table.getTotalWidth();
                updateFilledWidth(tableWidth);
            } else {
                tableWidth = rectangularWidth * table.getWidthPercentage() / 100f;
                table.setTotalWidth(tableWidth);
            }
            // how many header rows are real header rows; how many are footer rows? 
            int headerRows = table.getHeaderRows();
            int footerRows = table.getFooterRows();
            if (footerRows > headerRows)
                footerRows = headerRows;
            int realHeaderRows = headerRows - footerRows;
            float headerHeight = table.getHeaderHeight();
            float footerHeight = table.getFooterHeight();
            // make sure the header and footer fit on the page 
            boolean skipHeader = !firstPass && table.isSkipFirstHeader() && listIdx <= headerRows;
            if (!skipHeader) {
                yTemp -= headerHeight;
                if (yTemp < minY || yTemp > maxY) {
                    if (firstPass) {
                        compositeElements.removeFirst();
                        continue;
                    }
                    return NO_MORE_COLUMN;
                }
            }
            // how many real rows (not header or footer rows) fit on a page? 
            int k;
            if (listIdx < headerRows)
                listIdx = headerRows;
            if (!table.isComplete())
                yTemp -= footerHeight;
            for (k = listIdx; k < table.size(); ++k) {
                float rowHeight = table.getRowHeight(k);
                if (yTemp - rowHeight < minY)
                    break;
                yTemp -= rowHeight;
            }
            if (!table.isComplete())
                yTemp += footerHeight;
            // either k is the first row that doesn't fit on the page (break); 
            if (k < table.size()) {
                if (table.isSplitRows() && (!table.isSplitLate() || k == listIdx && firstPass)) {
                    if (!splittedRow) {
                        splittedRow = true;
                        table = new PdfPTable(table);
                        compositeElements.set(0, table);
                        ArrayList<PdfPRow> rows = table.getRows();
                        for (int i = headerRows; i < listIdx; ++i) rows.set(i, null);
                    }
                    float h = yTemp - minY;
                    PdfPRow newRow = table.getRow(k).splitRow(table, k, h);
                    if (newRow == null) {
                        if (k == listIdx)
                            return NO_MORE_COLUMN;
                    } else {
                        yTemp = minY;
                        table.getRows().add(++k, newRow);
                    }
                } else if (!table.isSplitRows() && k == listIdx && firstPass) {
                    compositeElements.removeFirst();
                    splittedRow = false;
                    continue;
                } else if (k == listIdx && !firstPass && (!table.isSplitRows() || table.isSplitLate()) && (table.getFooterRows() == 0 || table.isComplete()))
                    return NO_MORE_COLUMN;
            }
            // or k is the number of rows in the table (for loop was done). 
            firstPass = false;
            // we draw the table (for real now) 
            if (!simulate) {
                // set the alignment 
                switch(table.getHorizontalAlignment()) {
                    case Element.ALIGN_LEFT:
                        break;
                    case Element.ALIGN_RIGHT:
                        x1 += rectangularWidth - tableWidth;
                        break;
                    default:
                        x1 += (rectangularWidth - tableWidth) / 2f;
                }
                // copy the rows that fit on the page in a new table nt 
                PdfPTable nt = PdfPTable.shallowCopy(table);
                ArrayList<PdfPRow> sub = nt.getRows();
                // first we add the real header rows (if necessary) 
                if (!skipHeader && realHeaderRows > 0) {
                    sub.addAll(table.getRows(0, realHeaderRows));
                } else
                    nt.setHeaderRows(footerRows);
                // then we add the real content 
                sub.addAll(table.getRows(listIdx, k));
                // if k < table.size(), we must indicate that the new table is complete; 
                // otherwise no footers will be added (because iText thinks the table continues on the same page) 
                boolean showFooter = !table.isSkipLastFooter();
                boolean newPageFollows = false;
                if (k < table.size()) {
                    nt.setComplete(true);
                    showFooter = true;
                    newPageFollows = true;
                }
                // we add the footer rows if necessary (not for incomplete tables) 
                for (int j = 0; j < footerRows && nt.isComplete() && showFooter; ++j) sub.add(table.getRow(j + realHeaderRows));
                // we need a correction if the last row needs to be extended 
                float rowHeight = 0;
                int index = sub.size() - 1;
                if (showFooter)
                    index -= footerRows;
                PdfPRow last = sub.get(index);
                if (table.isExtendLastRow(newPageFollows)) {
                    rowHeight = last.getMaxHeights();
                    last.setMaxHeights(yTemp - minY + rowHeight);
                    yTemp = minY;
                }
                // now we render the rows of the new table 
                if (canvases != null)
                    nt.writeSelectedRows(0, -1, x1, yLineWrite, canvases);
                else
                    nt.writeSelectedRows(0, -1, x1, yLineWrite, canvas);
                if (table.isExtendLastRow(newPageFollows)) {
                    last.setMaxHeights(rowHeight);
                }
            } else if (table.isExtendLastRow() && minY > PdfPRow.BOTTOM_LIMIT)
                yTemp = minY;
            yLine = yTemp;
            if (!(skipHeader || table.isComplete()))
                yLine += footerHeight;
            if (k >= table.size()) {
                yLine -= table.spacingAfter();
                compositeElements.removeFirst();
                splittedRow = false;
                listIdx = 0;
            } else {
                if (splittedRow) {
                    ArrayList<PdfPRow> rows = table.getRows();
                    for (int i = listIdx; i < k; ++i) rows.set(i, null);
                }
                listIdx = k;
                return NO_MORE_COLUMN;
            }
        } else if (element.type() == Element.YMARK) {
            if (!simulate) {
                DrawInterface zh = (DrawInterface) element;
                zh.draw(canvas, leftX, minY, rightX, maxY, yLine);
            }
            compositeElements.removeFirst();
        } else
            compositeElements.removeFirst();
    }
}
