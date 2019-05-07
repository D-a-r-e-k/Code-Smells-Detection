/******************************************************************************/
/**
 * Returns the temperature of a cell contained in a given list.
 * 
 * @param index Identifies the cell. This is the index of the cell in 
 * a given list of CellViews
 * @param list List containing only CellViews
 * @see #getAttribute(int,String,ArrayList)
 */
private double getTemperature(int index, ArrayList list) {
    Double temperature = (Double) getAttribute(index, KEY_TEMPERATURE, list);
    return temperature.doubleValue();
}
