/**
     * Gets a tile type fitted to the regional requirements.
     *
     * TODO: Can be used for mountains and rivers too.
     *
     * @param game The <code>Game</code> to find the type in.
     * @param candidates A list of <code>TileType</code>s to use for
     *     calculations.
     * @param latitude The tile latitude.
     * @return A suitable <code>TileType</code>.
     */
private TileType getRandomTileType(Game game, List<TileType> candidates, int latitude) {
    // decode options 
    final int forestChance = mapOptions.getInteger("model.option.forestNumber");
    final int temperaturePreference = mapOptions.getInteger("model.option.temperature");
    // temperature calculation 
    int poleTemperature = -20;
    int equatorTemperature = 40;
    switch(temperaturePreference) {
        case MapGeneratorOptions.TEMPERATURE_COLD:
            poleTemperature = -20;
            equatorTemperature = 25;
            break;
        case MapGeneratorOptions.TEMPERATURE_CHILLY:
            poleTemperature = -20;
            equatorTemperature = 30;
            break;
        case MapGeneratorOptions.TEMPERATURE_TEMPERATE:
            poleTemperature = -10;
            equatorTemperature = 35;
            break;
        case MapGeneratorOptions.TEMPERATURE_WARM:
            poleTemperature = -5;
            equatorTemperature = 40;
            break;
        case MapGeneratorOptions.TEMPERATURE_HOT:
            poleTemperature = 0;
            equatorTemperature = 40;
            break;
    }
    final Specification spec = game.getSpecification();
    int temperatureRange = equatorTemperature - poleTemperature;
    int localeTemperature = poleTemperature + (90 - Math.abs(latitude)) * temperatureRange / 90;
    int temperatureDeviation = 7;
    // +/- 7 degrees randomization 
    localeTemperature += random.nextInt(temperatureDeviation * 2) - temperatureDeviation;
    localeTemperature = limitToRange(localeTemperature, -20, 40);
    // humidity calculation 
    int localeHumidity = spec.getRangeOption(MapGeneratorOptions.HUMIDITY).getValue();
    int humidityDeviation = 20;
    // +/- 20% randomization 
    localeHumidity += random.nextInt(humidityDeviation * 2) - humidityDeviation;
    localeHumidity = limitToRange(localeHumidity, 0, 100);
    List<TileType> candidateTileTypes = new ArrayList<TileType>(candidates);
    // Filter the candidates by temperature. 
    int i = 0;
    while (i < candidateTileTypes.size()) {
        TileType type = candidateTileTypes.get(i);
        if (!type.withinRange(TileType.RangeType.TEMPERATURE, localeTemperature)) {
            candidateTileTypes.remove(i);
            continue;
        }
        i++;
    }
    // Need to continue? 
    switch(candidateTileTypes.size()) {
        case 0:
            throw new RuntimeException("No TileType for" + " temperature==" + localeTemperature);
        case 1:
            return candidateTileTypes.get(0);
        default:
            break;
    }
    // Filter the candidates by humidity. 
    i = 0;
    while (i < candidateTileTypes.size()) {
        TileType type = candidateTileTypes.get(i);
        if (!type.withinRange(TileType.RangeType.HUMIDITY, localeHumidity)) {
            candidateTileTypes.remove(i);
            continue;
        }
        i++;
    }
    // Need to continue? 
    switch(candidateTileTypes.size()) {
        case 0:
            throw new RuntimeException("No TileType for" + " humidity==" + localeHumidity);
        case 1:
            return candidateTileTypes.get(0);
        default:
            break;
    }
    // Filter the candidates by forest presence. 
    boolean forested = random.nextInt(100) < forestChance;
    i = 0;
    while (i < candidateTileTypes.size()) {
        TileType type = candidateTileTypes.get(i);
        if (type.isForested() != forested) {
            candidateTileTypes.remove(i);
            continue;
        }
        i++;
    }
    // Done 
    switch(i = candidateTileTypes.size()) {
        case 0:
            throw new RuntimeException("No TileType for" + " forested==" + forested);
        case 1:
            return candidateTileTypes.get(0);
        default:
            return candidateTileTypes.get(random.nextInt(i));
    }
}
