private Map<String, List<Trigger>> buildTriggersByFQJobNameMap(List<Trigger> triggers) {
    Map<String, List<Trigger>> triggersByFQJobName = new HashMap<String, List<Trigger>>();
    for (Trigger trigger : triggers) {
        List<Trigger> triggersOfJob = triggersByFQJobName.get(trigger.getFullJobName());
        if (triggersOfJob == null) {
            triggersOfJob = new LinkedList<Trigger>();
            triggersByFQJobName.put(trigger.getFullJobName(), triggersOfJob);
        }
        triggersOfJob.add(trigger);
    }
    return triggersByFQJobName;
}
