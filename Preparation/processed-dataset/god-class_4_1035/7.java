public static List getListing(ChannelIF channel) {
    ArrayList listing = new ArrayList();
    if (channel != null) {
        try {
            if (channel.getItems().size() > 0) {
                int count = 0;
                Iterator chs = channel.getItems().iterator();
                while (chs.hasNext()) {
                    ItemIF item = (ItemIF) chs.next();
                    if (item.getEnclosure() != null) {
                        if ((item.getEnclosure().getLocation() != null) || (item.getEnclosure().getType() != null && item.getEnclosure().getType().equals("audio/mpeg")))
                            listing.add(item);
                    }
                }
            }
        } catch (Exception ex) {
            Tools.logException(Videocasting.class, ex);
        }
    }
    return listing;
}
