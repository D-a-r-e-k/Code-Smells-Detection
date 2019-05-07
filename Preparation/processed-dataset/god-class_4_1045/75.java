public void removeIndexes(TargetInstance ti) {
    List<HarvestResult> results = ti.getHarvestResults();
    if (results != null) {
        Iterator<HarvestResult> it = results.iterator();
        while (it.hasNext()) {
            HarvestResult hr = it.next();
            if (hr.getState() != HarvestResult.STATE_REJECTED) {
                //Rejected HRs have already had their indexes removed  
                //The endorsing process should mean there is only one none rejected HR  
                removeIndexes(hr);
            }
        }
    }
}
