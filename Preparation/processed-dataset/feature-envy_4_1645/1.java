/**
	 * Main HSSFListener method, processes events, and outputs the
	 *  CSV as the file is processed.
	 */
public void processRecord(Record record) {
    int thisRow = -1;
    int thisColumn = -1;
    String thisStr = null;
    switch(record.getSid()) {
        case BoundSheetRecord.sid:
            boundSheetRecords.add(record);
            break;
        case BOFRecord.sid:
            BOFRecord br = (BOFRecord) record;
            if (br.getType() == BOFRecord.TYPE_WORKSHEET) {
                // Create sub workbook if required 
                if (workbookBuildingListener != null && stubWorkbook == null) {
                    stubWorkbook = workbookBuildingListener.getStubHSSFWorkbook();
                }
                // Output the worksheet name 
                // Works by ordering the BSRs by the location of 
                //  their BOFRecords, and then knowing that we 
                //  process BOFRecords in byte offset order 
                sheetIndex++;
                if (orderedBSRs == null) {
                    orderedBSRs = BoundSheetRecord.orderByBofPosition(boundSheetRecords);
                }
                output.println();
                output.println(orderedBSRs[sheetIndex].getSheetname() + " [" + (sheetIndex + 1) + "]:");
            }
            break;
        case SSTRecord.sid:
            sstRecord = (SSTRecord) record;
            break;
        case BlankRecord.sid:
            BlankRecord brec = (BlankRecord) record;
            thisRow = brec.getRow();
            thisColumn = brec.getColumn();
            thisStr = "";
            break;
        case BoolErrRecord.sid:
            BoolErrRecord berec = (BoolErrRecord) record;
            thisRow = berec.getRow();
            thisColumn = berec.getColumn();
            thisStr = "";
            break;
        case FormulaRecord.sid:
            FormulaRecord frec = (FormulaRecord) record;
            thisRow = frec.getRow();
            thisColumn = frec.getColumn();
            if (outputFormulaValues) {
                if (Double.isNaN(frec.getValue())) {
                    // Formula result is a string 
                    // This is stored in the next record 
                    outputNextStringRecord = true;
                    nextRow = frec.getRow();
                    nextColumn = frec.getColumn();
                } else {
                    thisStr = formatListener.formatNumberDateCell(frec);
                }
            } else {
                thisStr = '"' + HSSFFormulaParser.toFormulaString(stubWorkbook, frec.getParsedExpression()) + '"';
            }
            break;
        case StringRecord.sid:
            if (outputNextStringRecord) {
                // String for formula 
                StringRecord srec = (StringRecord) record;
                thisStr = srec.getString();
                thisRow = nextRow;
                thisColumn = nextColumn;
                outputNextStringRecord = false;
            }
            break;
        case LabelRecord.sid:
            LabelRecord lrec = (LabelRecord) record;
            thisRow = lrec.getRow();
            thisColumn = lrec.getColumn();
            thisStr = '"' + lrec.getValue() + '"';
            break;
        case LabelSSTRecord.sid:
            LabelSSTRecord lsrec = (LabelSSTRecord) record;
            thisRow = lsrec.getRow();
            thisColumn = lsrec.getColumn();
            if (sstRecord == null) {
                thisStr = '"' + "(No SST Record, can't identify string)" + '"';
            } else {
                thisStr = '"' + sstRecord.getString(lsrec.getSSTIndex()).toString() + '"';
            }
            break;
        case NoteRecord.sid:
            NoteRecord nrec = (NoteRecord) record;
            thisRow = nrec.getRow();
            thisColumn = nrec.getColumn();
            // TODO: Find object to match nrec.getShapeId() 
            thisStr = '"' + "(TODO)" + '"';
            break;
        case NumberRecord.sid:
            NumberRecord numrec = (NumberRecord) record;
            thisRow = numrec.getRow();
            thisColumn = numrec.getColumn();
            // Format 
            thisStr = formatListener.formatNumberDateCell(numrec);
            break;
        case RKRecord.sid:
            RKRecord rkrec = (RKRecord) record;
            thisRow = rkrec.getRow();
            thisColumn = rkrec.getColumn();
            thisStr = '"' + "(TODO)" + '"';
            break;
        default:
            break;
    }
    // Handle new row 
    if (thisRow != -1 && thisRow != lastRowNumber) {
        lastColumnNumber = -1;
    }
    // Handle missing column 
    if (record instanceof MissingCellDummyRecord) {
        MissingCellDummyRecord mc = (MissingCellDummyRecord) record;
        thisRow = mc.getRow();
        thisColumn = mc.getColumn();
        thisStr = "";
    }
    // If we got something to print out, do so 
    if (thisStr != null) {
        if (thisColumn > 0) {
            output.print(',');
        }
        output.print(thisStr);
    }
    // Update column and row count 
    if (thisRow > -1)
        lastRowNumber = thisRow;
    if (thisColumn > -1)
        lastColumnNumber = thisColumn;
    // Handle end of row 
    if (record instanceof LastCellOfRowDummyRecord) {
        // Print out any missing commas if needed 
        if (minColumns > 0) {
            // Columns are 0 based 
            if (lastColumnNumber == -1) {
                lastColumnNumber = 0;
            }
            for (int i = lastColumnNumber; i < (minColumns); i++) {
                output.print(',');
            }
        }
        // We're onto a new row 
        lastColumnNumber = -1;
        // End the row 
        output.println();
    }
}
