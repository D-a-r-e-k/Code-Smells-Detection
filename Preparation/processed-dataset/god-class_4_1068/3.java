private void updateOptions() {
    myDetailsOptions = new GPOption[myLabelOptions.length];
    System.arraycopy(myLabelOptions, 0, myDetailsOptions, 0, myLabelOptions.length);
    myOptionGroups[0] = new ChartOptionGroup("ganttChartDetails", myDetailsOptions, myModel.getOptionEventDispatcher());
}
