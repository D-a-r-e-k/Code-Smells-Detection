@Override
public boolean isDataFlavorSupported(DataFlavor flavor) {
    return flavor.getPrimaryType().equals("image") && flavor.getSubType().equals("svg+xml");
}
