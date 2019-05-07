void method0() { 
private static final Log log = LogFactory.getLog(UserModuleProcessor.class);
private static int count;
protected final String ORIGINAL_REQUEST = "mvnforum.user.OriginalRequest";
private HttpServlet mainServlet = null;
protected ServletContext servletContext = null;
protected OnlineUserManager onlineUserManager = OnlineUserManager.getInstance();
protected UserModuleURLMapHandler urlMapHandler = new UserModuleURLMapHandler();
private ForumWebHandler forumWebHandler = new ForumWebHandler();
private ThreadWebHandler threadWebHandler = new ThreadWebHandler();
private PostWebHandler postWebHandler = new PostWebHandler();
protected AttachmentWebHandler attachmentWebHandler = new AttachmentWebHandler();
private MemberWebHandler memberWebHandler = new MemberWebHandler();
private WatchWebHandler watchWebHandler = new WatchWebHandler();
private FavoriteThreadWebHandler favoriteThreadWebHandler = new FavoriteThreadWebHandler();
private MessageWebHandler messageWebHandler = new MessageWebHandler();
private PmAttachmentWebHandler pmAttachmentWebHandler = new PmAttachmentWebHandler();
private MessageFolderWebHandler messageFolderWebHandler = new MessageFolderWebHandler();
private MailWebHandler mailWebHandler = new MailWebHandler();
}
