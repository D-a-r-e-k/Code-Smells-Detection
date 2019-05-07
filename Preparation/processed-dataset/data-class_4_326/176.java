public BufferedImage createMiniMapThumbNail() {
    MiniMap miniMap = new MiniMap(freeColClient, this);
    miniMap.setTileSize(MiniMap.MAX_TILE_SIZE);
    int width = freeColClient.getGame().getMap().getWidth() * MiniMap.MAX_TILE_SIZE + MiniMap.MAX_TILE_SIZE / 2;
    int height = freeColClient.getGame().getMap().getHeight() * MiniMap.MAX_TILE_SIZE / 4;
    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2d = image.createGraphics();
    miniMap.paintMap(g2d);
    // TODO: this can probably done more efficiently 
    // by applying a suitable AffineTransform to the 
    // Graphics2D 
    double scaledWidth = Math.min((64 * width) / height, 128);
    BufferedImage scaledImage = new BufferedImage((int) scaledWidth, 64, BufferedImage.TYPE_INT_ARGB);
    scaledImage.createGraphics().drawImage(image, 0, 0, (int) scaledWidth, 64, null);
    return scaledImage;
}
