/**
     * Saves a LastSale record.
     *
     * @param sale The <code>LastSale</code> to save.
     */
public void saveSale(LastSale sale) {
    if (lastSales == null)
        lastSales = new HashMap<String, LastSale>();
    lastSales.put(sale.getId(), sale);
}
