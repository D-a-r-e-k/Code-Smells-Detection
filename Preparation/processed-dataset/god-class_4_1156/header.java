void method0() { 
private static final Logger logger = Logger.getLogger(TerrainGenerator.class.getName());
public static final int LAND_REGIONS_SCORE_VALUE = 1000;
public static final int LAND_REGION_MIN_SCORE = 5;
public static final int PACIFIC_SCORE_VALUE = 100;
public static final int LAND_REGION_MAX_SIZE = 75;
/**
     * The map options group.
     */
private final OptionGroup mapOptions;
/**
     * The pseudo-random number source to use.
     * Uses of the PRNG are usually logged in FreeCol, but the use is
     * so intense here and this code is called pre-game, so we intentionally
     * skip the logging here.
     */
private final Random random;
// Cache of land and ocean tile types. 
private ArrayList<TileType> landTileTypes = null;
private ArrayList<TileType> oceanTileTypes = null;
// Cache of geographic regions 
private ServerRegion[] geographicRegions = null;
}
