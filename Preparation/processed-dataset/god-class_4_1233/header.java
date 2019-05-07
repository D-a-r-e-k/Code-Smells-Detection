void method0() { 
// What we're based on 
private HSLFSlideShow _hslfSlideShow;
// Low level contents, as taken from HSLFSlideShow 
private Record[] _records;
// Pointers to the most recent versions of the core records 
// (Document, Notes, Slide etc) 
private Record[] _mostRecentCoreRecords;
// Lookup between the PersitPtr "sheet" IDs, and the position 
// in the mostRecentCoreRecords array 
private Hashtable _sheetIdToCoreRecordsLookup;
// Records that are interesting 
private Document _documentRecord;
// Friendly objects for people to deal with 
private SlideMaster[] _masters;
private TitleMaster[] _titleMasters;
private Slide[] _slides;
private Notes[] _notes;
private FontCollection _fonts;
// For logging 
private POILogger logger = POILogFactory.getLogger(this.getClass());
}
