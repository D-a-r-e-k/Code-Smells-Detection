/**
     * Sets the dataset used by the plot and sends a {@link PlotChangeEvent}
     * to all registered listeners.
     *
     * @param dataset  the dataset (<code>null</code> permitted).
     *
     * @see #getDataset()
     */
public void setDataset(CategoryDataset dataset) {
    // if there is an existing dataset, remove the plot from the list of  
    // change listeners...  
    if (this.dataset != null) {
        this.dataset.removeChangeListener(this);
    }
    // set the new dataset, and register the chart as a change listener...  
    this.dataset = dataset;
    if (dataset != null) {
        setDatasetGroup(dataset.getGroup());
        dataset.addChangeListener(this);
    }
    // send a dataset change event to self to trigger plot change event  
    datasetChanged(new DatasetChangeEvent(this, dataset));
}
