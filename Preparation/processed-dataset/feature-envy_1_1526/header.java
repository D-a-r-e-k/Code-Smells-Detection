void method0() { 
private static final Logger LOG = Logger.getLogger("org.columba.core.trayicon");
private IFrameMediator frameMediator;
/**
	 * Default icon for the TrayIcon.
	 */
public static final Icon DEFAULT_ICON = ImageLoader.getImageIcon("trayicon.png");
private static ColumbaTrayIcon instance = new ColumbaTrayIcon();
private JPopupMenu menu;
private TrayIconInterface activeIcon;
}
