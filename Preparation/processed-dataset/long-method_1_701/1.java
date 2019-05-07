/**
	 * Add the tray icon to the default system tray.
	 * 
	 */
public void addToSystemTray(IFrameMediator frameMediator) {
    initPopupMenu();
    this.frameMediator = frameMediator;
    activeIcon.addToTray(DEFAULT_ICON, "Columba");
    activeIcon.setPopupMenu(menu);
    ShutdownManager.getInstance().register(new Runnable() {

        public void run() {
            ColumbaTrayIcon.getInstance().removeFromSystemTray();
        }
    });
}
