void method0() { 
// http://<your tivoip>/TiVoConnect?Command=QueryContainer&Container=%2F 
// http://<your tivoip>/TiVoConnect?Command=QueryContainer&Container=GalleonRecordings&Recurse=Yes&SortOrder=!CaptureDate&ItemCount=8&Filter=x-tivo-container%2Ftivo-videos,x-tivo-container%2Ffolder,video%2Fx-tivo-mpeg,video%2F* 
private static Logger log = Logger.getLogger(VideoServer.class.getName());
private String mHost = "Galleon";
private SimpleDateFormat mFileDateFormat;
private SimpleDateFormat mTimeDateFormat;
private SimpleDateFormat mDurationFormat;
private GregorianCalendar mCalendar;
private List mPublished = new ArrayList();
}
