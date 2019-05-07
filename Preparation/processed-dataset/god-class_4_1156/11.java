/**
     * Creates ocean map regions in the given Map.
     *
     * Be careful to tolerate regions pre-existing from an imported game.
     * At the moment, the ocean is divided into two by two regions.
     *
     * @param map The <code>Map</code> to work on.
     */
private void createOceanRegions(Map map) {
    Game game = map.getGame();
    ServerRegion pacific = (ServerRegion) map.getRegion("model.region.pacific");
    ServerRegion northPacific = (ServerRegion) map.getRegion("model.region.northPacific");
    ServerRegion southPacific = (ServerRegion) map.getRegion("model.region.southPacific");
    ServerRegion atlantic = (ServerRegion) map.getRegion("model.region.atlantic");
    ServerRegion northAtlantic = (ServerRegion) map.getRegion("model.region.northAtlantic");
    ServerRegion southAtlantic = (ServerRegion) map.getRegion("model.region.southAtlantic");
    int present = 0;
    if (pacific == null) {
        pacific = new ServerRegion(game, "model.region.pacific", RegionType.OCEAN, null);
        pacific.setDiscoverable(true);
        map.putRegion(pacific);
        pacific.setScoreValue(PACIFIC_SCORE_VALUE);
    }
    if (northPacific == null) {
        northPacific = new ServerRegion(game, "model.region.northPacific", RegionType.OCEAN, pacific);
        northPacific.setDiscoverable(false);
        map.putRegion(northPacific);
    } else
        present++;
    if (southPacific == null) {
        southPacific = new ServerRegion(game, "model.region.southPacific", RegionType.OCEAN, pacific);
        southPacific.setDiscoverable(false);
        map.putRegion(southPacific);
    } else
        present++;
    if (atlantic == null) {
        atlantic = new ServerRegion(game, "model.region.atlantic", RegionType.OCEAN, null);
        atlantic.setPrediscovered(true);
        atlantic.setDiscoverable(false);
        map.putRegion(atlantic);
    }
    if (northAtlantic == null) {
        northAtlantic = new ServerRegion(game, "model.region.northAtlantic", RegionType.OCEAN, atlantic);
        northAtlantic.setPrediscovered(true);
        northAtlantic.setDiscoverable(false);
        map.putRegion(northAtlantic);
    } else
        present++;
    if (southAtlantic == null) {
        southAtlantic = new ServerRegion(game, "model.region.southAtlantic", RegionType.OCEAN, atlantic);
        southAtlantic.setPrediscovered(true);
        southAtlantic.setDiscoverable(false);
        map.putRegion(southAtlantic);
    } else
        present++;
    if (present == 4) {
        // All the ocean regions were defined, no need to regenerate. 
        return;
    }
    // Fill the ocean regions by first filling the quadrants individually, 
    // then allow the quadrants to overflow into their horizontally 
    // opposite quadrant, then finally into the whole map. 
    // This correctly handles cases like: 
    // 
    //   NP NP NP NA NA NA      NP NP NP NA NA NA 
    //   NP L  L  L  L  NA      NP L  L  NA L  NA 
    //   NP L  NA NA NA NA  or  NP L  NA NA L  NA 
    //   SP L  SA SA SA SA      SP L  NA L  L  SA 
    //   SP L  L  L  L  SA      SP L  L  L  L  SA 
    //   SP SP SP SA SA SA      SP SP SP SA SA SA 
    // 
    // or multiple such incursions across the nominal quadrant divisions. 
    // 
    final int maxx = map.getWidth();
    final int midx = maxx / 2;
    final int maxy = map.getHeight();
    final int midy = maxy / 2;
    Position pNP = null, pSP = null, pNA = null, pSA = null;
    for (int y = midy - 1; y >= 0; y--) {
        if (pNP == null && !map.getTile(0, y).isLand()) {
            pNP = new Position(0, y);
        }
        if (pNA == null && !map.getTile(maxx - 1, y).isLand()) {
            pNA = new Position(maxx - 1, y);
        }
        if (pNP != null && pNA != null)
            break;
    }
    for (int y = midy; y < maxy; y++) {
        if (pSP == null && !map.getTile(0, y).isLand()) {
            pSP = new Position(0, y);
        }
        if (pSA == null && !map.getTile(maxx - 1, y).isLand()) {
            pSA = new Position(maxx - 1, y);
        }
        if (pSP != null && pSA != null)
            break;
    }
    int nNP = 0, nSP = 0, nNA = 0, nSA = 0;
    Rectangle rNP = new Rectangle(0, 0, midx, midy);
    Rectangle rSP = new Rectangle(0, midy, midx, maxy);
    Rectangle rNA = new Rectangle(midx, 0, maxx, midy);
    Rectangle rSA = new Rectangle(midx, midy, maxx, maxy);
    if (pNP != null)
        nNP += fillOcean(map, pNP, northPacific, rNP);
    if (pSP != null)
        nSP += fillOcean(map, pSP, southPacific, rSP);
    if (pNA != null)
        nNA += fillOcean(map, pNA, northAtlantic, rNA);
    if (pSA != null)
        nSA += fillOcean(map, pSA, southAtlantic, rSA);
    Rectangle rN = new Rectangle(0, 0, maxx, midy);
    Rectangle rS = new Rectangle(0, midy, maxx, maxy);
    if (pNP != null)
        nNP += fillOcean(map, pNP, northPacific, rN);
    if (pSP != null)
        nSP += fillOcean(map, pSP, southPacific, rS);
    if (pNA != null)
        nNA += fillOcean(map, pNA, northAtlantic, rN);
    if (pSA != null)
        nSA += fillOcean(map, pSA, southAtlantic, rS);
    Rectangle rAll = new Rectangle(0, 0, maxx, maxy);
    if (pNP != null)
        nNP += fillOcean(map, pNP, northPacific, rAll);
    if (pSP != null)
        nSP += fillOcean(map, pSP, southPacific, rAll);
    if (pNA != null)
        nNA += fillOcean(map, pNA, northAtlantic, rAll);
    if (pSA != null)
        nSA += fillOcean(map, pSA, southAtlantic, rAll);
    if (nNP <= 0)
        logger.warning("No North Pacific tiles found");
    if (nSP <= 0)
        logger.warning("No South Pacific tiles found");
    if (nNA <= 0)
        logger.warning("No North Atlantic tiles found");
    if (nSA <= 0)
        logger.warning("No South Atlantic tiles found");
    logger.info("Ocean regions complete: " + nNP + " North Pacific, " + nSP + " South Pacific, " + nNA + " North Atlantic, " + nSP + " South Atlantic");
}
