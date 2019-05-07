private void triggerAutoQA(ArcHarvestResult ahr) {
    TargetInstance ti = ahr.getTargetInstance();
    if (autoQAUrl != null && autoQAUrl.length() > 0 && ti.isUseAQA()) {
        GetMethod getMethod = new GetMethod(autoQAUrl);
        String primarySeed = "";
        //Might be nice to use the SeedHistory here, but as this won't necessarily be turned on, we can't  
        //use it reliably  
        Set<Seed> seeds = ti.getTarget().getSeeds();
        Iterator<Seed> it = seeds.iterator();
        while (it.hasNext()) {
            Seed seed = it.next();
            if (seed.isPrimary()) {
                primarySeed = seed.getSeed();
                break;
            }
        }
        NameValuePair[] params = { new NameValuePair("targetInstanceId", ti.getOid().toString()), new NameValuePair("harvestNumber", Integer.toString(ahr.getHarvestNumber())), new NameValuePair("primarySeed", primarySeed) };
        getMethod.setQueryString(params);
        HttpClient client = new HttpClient();
        try {
            int result = client.executeMethod(getMethod);
            if (result != HttpURLConnection.HTTP_OK) {
                log.error("Unable to initiate Auto QA. Response at " + autoQAUrl + " is " + result);
            }
        } catch (Exception e) {
            log.error("Unable to initiate Auto QA.", e);
        }
    }
}
