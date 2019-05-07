void method0() { 
private static final Log log = LogFactory.getLog(AttachmentWebHandler.class);
private OnlineUserManager onlineUserManager = OnlineUserManager.getInstance();
private CategoryService categoryService = MvnForumServiceFactory.getMvnForumService().getCategoryService();
private static CategoryBuilderService categoryBuilderService = MvnForumServiceFactory.getMvnForumService().getCategoryBuilderService();
private BinaryStorageService binaryStorageService = MvnCoreServiceFactory.getMvnCoreService().getBinaryStorageService();
private FileUploadParserService fileUploadParserService = MvnCoreServiceFactory.getMvnCoreService().getFileUploadParserService();
private URLResolverService urlResolverService = MvnCoreServiceFactory.getMvnCoreService().getURLResolverService();
}
