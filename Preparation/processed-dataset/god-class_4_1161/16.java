/**
     * Drops some goods from the goods list, and cancels any transport.
     *
     * @param ag The <code>AIGoods</code> to drop.
     */
private void dropGoods(AIGoods ag) {
    if (ag.getTransport() != null && ag.getTransport().getMission() instanceof TransportMission) {
        ((TransportMission) ag.getTransport().getMission()).removeTransportable((Transportable) ag, "No longer required by " + colony.getName());
    }
    removeAIGoods(ag);
    ag.dispose();
}
