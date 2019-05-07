public void init(IContext context) throws Exception {
    super.init(context);
    mMenuBackground = getSkinImage("menu", "background");
    mInfoBackground = getSkinImage("info", "background");
    mPlayerBackground = getSkinImage("player", "background");
    mLyricsBackground = getSkinImage("lyrics", "background");
    mImagesBackground = getSkinImage("images", "background");
    mFolderIcon = getSkinImage("menu", "folder");
    mItemIcon = getSkinImage("menu", "item");
    PodcastingConfiguration podcastingConfiguration = (PodcastingConfiguration) ((PodcastingFactory) getFactory()).getAppContext().getConfiguration();
    push(new PodcastingMenuScreen(this), TRANSITION_NONE);
    initialize();
}
