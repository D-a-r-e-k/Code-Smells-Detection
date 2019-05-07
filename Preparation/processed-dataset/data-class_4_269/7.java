@Override
public boolean isDataFlavorSupported(DataFlavor flavor) {
    return flavor.getPrimaryType().equals("application") && flavor.getSubType().equals("vnd.oasis.opendocument.graphics");
}
