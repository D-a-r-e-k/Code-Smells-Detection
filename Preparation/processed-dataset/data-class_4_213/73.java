public void notifyAQAComplete(String aqaId) {
    log.debug("Received notifyAQAComplete for job(" + aqaId + ").");
    try {
        String[] ids = aqaId.split("-");
        long tiOid = Long.parseLong(ids[0]);
        TargetInstance ti = targetInstanceDao.load(tiOid);
        int harvestNumber = Integer.parseInt(ids[1]);
        HarvestResult result = ti.getHarvestResult(harvestNumber);
        // Send a message.  
        inTrayManager.generateNotification(ti.getOwner().getOid(), MessageType.CATEGORY_MISC, MessageType.NOTIFICATION_AQA_COMPLETE, result);
    } catch (Exception e) {
        log.error("Unable to notify AQA Id: " + aqaId, e);
    }
}
