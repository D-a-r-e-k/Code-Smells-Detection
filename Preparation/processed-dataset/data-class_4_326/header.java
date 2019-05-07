void method0() { 
private static final Logger logger = Logger.getLogger(GUI.class.getName());
public static final String COPYRIGHT = "Copyright (C) 2002-2012   The FreeCol Team";
public static final String LICENSE = "http://www.gnu.org/licenses/gpl.html";
public static final String REVISION = "$Revision: 10427 $";
/**
     * The space not being used in windowed mode.
     */
private static final int DEFAULT_WINDOW_SPACE = 50;
public static final int MOVE_UNITS_MODE = 0;
public static final int VIEW_TERRAIN_MODE = 1;
private FreeColClient freeColClient;
private GraphicsDevice gd;
private FreeColFrame frame;
private Canvas canvas;
private MapViewer mapViewer;
private MapControls mapControls;
/**
     * This is the MapViewer instance used to paint the colony tiles in the
     * ColonyPanel and other panels.  It should not be scaled along
     * with the default MapViewer.
     */
private MapViewer colonyTileGUI;
private ImageLibrary imageLibrary;
private SoundPlayer soundPlayer;
private boolean windowed;
private Rectangle windowBounds;
private JWindow splash;
}
