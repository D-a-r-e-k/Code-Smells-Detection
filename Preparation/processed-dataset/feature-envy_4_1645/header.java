void method0() { 
private int minColumns;
private POIFSFileSystem fs;
private PrintStream output;
private int lastRowNumber;
private int lastColumnNumber;
/** Should we output the formula, or the value it has? */
private boolean outputFormulaValues = true;
/** For parsing Formulas */
private SheetRecordCollectingListener workbookBuildingListener;
private HSSFWorkbook stubWorkbook;
// Records we pick up as we process 
private SSTRecord sstRecord;
private FormatTrackingHSSFListener formatListener;
/** So we known which sheet we're on */
private int sheetIndex = -1;
private BoundSheetRecord[] orderedBSRs;
private ArrayList boundSheetRecords = new ArrayList();
// For handling formulas with string results 
private int nextRow;
private int nextColumn;
private boolean outputNextStringRecord;
}
