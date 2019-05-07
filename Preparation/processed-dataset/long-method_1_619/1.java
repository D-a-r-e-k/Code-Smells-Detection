public void init(IContext context) throws Exception {
    super.init(context);
    mCallbacks = new ArrayList();
    try {
        AppContext appContext = ((AppFactory) getFactory()).getAppContext();
        List list = ApplicationManager.findByClazz(appContext.getDescriptor().getClassName());
        if (list != null && list.size() > 0) {
            Application application = (Application) list.get(0);
            if (application.getDateInstalled() == null)
                application.setDateInstalled(new Date());
            application.setLastUsed(new Date());
            application.setTotal(application.getTotal() + 1);
            if (appContext.getConfiguration() instanceof AppConfiguration) {
                AppConfiguration appConfiguration = (AppConfiguration) appContext.getConfiguration();
                application.setShared(Boolean.valueOf(appConfiguration.isShared()));
            }
            ApplicationManager.updateApplication(application);
        } else {
            boolean shared = false;
            if (appContext.getConfiguration() instanceof AppConfiguration) {
                AppConfiguration appConfiguration = (AppConfiguration) appContext.getConfiguration();
                shared = appConfiguration.isShared();
            }
            Application application = new Application(appContext.getDescriptor().getClassName(), appContext.getTitle(), appContext.getDescriptor().getVersion(), 1, new Date(), null, new Date(), Boolean.valueOf(shared));
            ApplicationManager.createApplication(application);
        }
    } catch (Exception ex) {
        Tools.logException(DefaultApplication.class, ex);
    }
}
