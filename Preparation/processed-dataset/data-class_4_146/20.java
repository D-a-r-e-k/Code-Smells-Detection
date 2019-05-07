protected void process(InputSource is) throws SAXException, IOException, ParseException, XPathException, ClassNotFoundException {
    // load the document  
    Document document = docBuilder.parse(is);
    // 
    // Extract pre-processing commands 
    // 
    NodeList deleteJobGroupNodes = (NodeList) xpath.evaluate("/q:job-scheduling-data/q:pre-processing-commands/q:delete-jobs-in-group", document, XPathConstants.NODESET);
    log.debug("Found " + deleteJobGroupNodes.getLength() + " delete job group commands.");
    for (int i = 0; i < deleteJobGroupNodes.getLength(); i++) {
        Node node = deleteJobGroupNodes.item(i);
        String t = node.getTextContent();
        if (t == null || (t = t.trim()).length() == 0)
            continue;
        jobGroupsToDelete.add(t);
    }
    NodeList deleteTriggerGroupNodes = (NodeList) xpath.evaluate("/q:job-scheduling-data/q:pre-processing-commands/q:delete-triggers-in-group", document, XPathConstants.NODESET);
    log.debug("Found " + deleteTriggerGroupNodes.getLength() + " delete trigger group commands.");
    for (int i = 0; i < deleteTriggerGroupNodes.getLength(); i++) {
        Node node = deleteTriggerGroupNodes.item(i);
        String t = node.getTextContent();
        if (t == null || (t = t.trim()).length() == 0)
            continue;
        triggerGroupsToDelete.add(t);
    }
    NodeList deleteJobNodes = (NodeList) xpath.evaluate("/q:job-scheduling-data/q:pre-processing-commands/q:delete-job", document, XPathConstants.NODESET);
    log.debug("Found " + deleteJobNodes.getLength() + " delete job commands.");
    for (int i = 0; i < deleteJobNodes.getLength(); i++) {
        Node node = deleteJobNodes.item(i);
        String name = getTrimmedToNullString(xpath, "q:name", node);
        String group = getTrimmedToNullString(xpath, "q:group", node);
        if (name == null)
            throw new ParseException("Encountered a 'delete-job' command without a name specified.", -1);
        jobsToDelete.add(new Key(name, group));
    }
    NodeList deleteTriggerNodes = (NodeList) xpath.evaluate("/q:job-scheduling-data/q:pre-processing-commands/q:delete-trigger", document, XPathConstants.NODESET);
    log.debug("Found " + deleteTriggerNodes.getLength() + " delete trigger commands.");
    for (int i = 0; i < deleteTriggerNodes.getLength(); i++) {
        Node node = deleteTriggerNodes.item(i);
        String name = getTrimmedToNullString(xpath, "q:name", node);
        String group = getTrimmedToNullString(xpath, "q:group", node);
        if (name == null)
            throw new ParseException("Encountered a 'delete-trigger' command without a name specified.", -1);
        triggersToDelete.add(new Key(name, group));
    }
    // 
    // Extract directives 
    // 
    Boolean overWrite = getBoolean(xpath, "/q:job-scheduling-data/q:processing-directives/q:overwrite-existing-data", document);
    if (overWrite == null) {
        log.debug("Directive 'overwrite-existing-data' not specified, defaulting to " + isOverWriteExistingData());
    } else {
        log.debug("Directive 'overwrite-existing-data' specified as: " + overWrite);
        setOverWriteExistingData(overWrite);
    }
    Boolean ignoreDupes = getBoolean(xpath, "/q:job-scheduling-data/q:processing-directives/q:ignore-duplicates", document);
    if (ignoreDupes == null) {
        log.debug("Directive 'ignore-duplicates' not specified, defaulting to " + isIgnoreDuplicates());
    } else {
        log.debug("Directive 'ignore-duplicates' specified as: " + ignoreDupes);
        setIgnoreDuplicates(ignoreDupes);
    }
    // 
    // Extract Job definitions... 
    // 
    NodeList jobNodes = (NodeList) xpath.evaluate("/q:job-scheduling-data/q:schedule/q:job", document, XPathConstants.NODESET);
    log.debug("Found " + jobNodes.getLength() + " job definitions.");
    for (int i = 0; i < jobNodes.getLength(); i++) {
        Node jobDetailNode = jobNodes.item(i);
        String t = null;
        String jobName = getTrimmedToNullString(xpath, "q:name", jobDetailNode);
        String jobGroup = getTrimmedToNullString(xpath, "q:group", jobDetailNode);
        String jobDescription = getTrimmedToNullString(xpath, "q:description", jobDetailNode);
        String jobClassName = getTrimmedToNullString(xpath, "q:job-class", jobDetailNode);
        t = getTrimmedToNullString(xpath, "q:volatility", jobDetailNode);
        boolean jobVolatility = (t != null) && t.equals("true");
        t = getTrimmedToNullString(xpath, "q:durability", jobDetailNode);
        boolean jobDurability = (t != null) && t.equals("true");
        t = getTrimmedToNullString(xpath, "q:recover", jobDetailNode);
        boolean jobRecoveryRequested = (t != null) && t.equals("true");
        Class jobClass = classLoadHelper.loadClass(jobClassName);
        JobDetail jobDetail = new JobDetail(jobName, jobGroup, jobClass, jobVolatility, jobDurability, jobRecoveryRequested);
        jobDetail.setDescription(jobDescription);
        NodeList jobListenerEntries = (NodeList) xpath.evaluate("q:job-listener-ref", jobDetailNode, XPathConstants.NODESET);
        for (int j = 0; j < jobListenerEntries.getLength(); j++) {
            Node listenerRefNode = jobListenerEntries.item(j);
            String ref = listenerRefNode.getTextContent();
            if (ref != null && (ref = ref.trim()).length() == 0)
                ref = null;
            if (ref == null)
                continue;
            jobDetail.addJobListener(ref);
        }
        NodeList jobDataEntries = (NodeList) xpath.evaluate("q:job-data-map/q:entry", jobDetailNode, XPathConstants.NODESET);
        for (int k = 0; k < jobDataEntries.getLength(); k++) {
            Node entryNode = jobDataEntries.item(k);
            String key = getTrimmedToNullString(xpath, "q:key", entryNode);
            String value = getTrimmedToNullString(xpath, "q:value", entryNode);
            jobDetail.getJobDataMap().put(key, value);
        }
        if (log.isDebugEnabled())
            log.debug("Parsed job definition: " + jobDetail);
        addJobToSchedule(jobDetail);
    }
    // 
    // Extract Trigger definitions... 
    // 
    NodeList triggerEntries = (NodeList) xpath.evaluate("/q:job-scheduling-data/q:schedule/q:trigger/*", document, XPathConstants.NODESET);
    log.debug("Found " + triggerEntries.getLength() + " trigger definitions.");
    for (int j = 0; j < triggerEntries.getLength(); j++) {
        Node triggerNode = triggerEntries.item(j);
        String triggerName = getTrimmedToNullString(xpath, "q:name", triggerNode);
        String triggerGroup = getTrimmedToNullString(xpath, "q:group", triggerNode);
        String triggerDescription = getTrimmedToNullString(xpath, "q:description", triggerNode);
        String triggerMisfireInstructionConst = getTrimmedToNullString(xpath, "q:misfire-instruction", triggerNode);
        String triggerCalendarRef = getTrimmedToNullString(xpath, "q:calendar-name", triggerNode);
        String triggerJobName = getTrimmedToNullString(xpath, "q:job-name", triggerNode);
        String triggerJobGroup = getTrimmedToNullString(xpath, "q:job-group", triggerNode);
        String t = getTrimmedToNullString(xpath, "q:volatility", triggerNode);
        boolean triggerVolatility = (t != null) && t.equals("true");
        String startTimeString = getTrimmedToNullString(xpath, "q:start-time", triggerNode);
        String endTimeString = getTrimmedToNullString(xpath, "q:end-time", triggerNode);
        Date triggerStartTime = startTimeString == null || startTimeString.length() == 0 ? new Date() : dateFormat.parse(startTimeString);
        Date triggerEndTime = endTimeString == null || endTimeString.length() == 0 ? null : dateFormat.parse(endTimeString);
        Trigger trigger = null;
        if (triggerNode.getNodeName().equals("simple")) {
            String repeatCountString = getTrimmedToNullString(xpath, "q:repeat-count", triggerNode);
            String repeatIntervalString = getTrimmedToNullString(xpath, "q:repeat-interval", triggerNode);
            int repeatCount = repeatCountString == null ? SimpleTrigger.REPEAT_INDEFINITELY : Integer.parseInt(repeatCountString);
            long repeatInterval = repeatIntervalString == null ? 0 : Long.parseLong(repeatIntervalString);
            trigger = new SimpleTrigger(triggerName, triggerGroup, triggerJobName, triggerJobGroup, triggerStartTime, triggerEndTime, repeatCount, repeatInterval);
        } else if (triggerNode.getNodeName().equals("cron")) {
            String cronExpression = getTrimmedToNullString(xpath, "q:cron-expression", triggerNode);
            String timezoneString = getTrimmedToNullString(xpath, "q:time-zone", triggerNode);
            TimeZone tz = timezoneString == null ? null : TimeZone.getTimeZone(timezoneString);
            trigger = new CronTrigger(triggerName, triggerGroup, triggerJobName, triggerJobGroup, triggerStartTime, triggerEndTime, cronExpression, tz);
        } else {
            throw new ParseException("Unknown trigger type: " + triggerNode.getNodeName(), -1);
        }
        trigger.setVolatility(triggerVolatility);
        trigger.setDescription(triggerDescription);
        trigger.setCalendarName(triggerCalendarRef);
        if (triggerMisfireInstructionConst != null && triggerMisfireInstructionConst.length() != 0) {
            Class clazz = trigger.getClass();
            java.lang.reflect.Field field;
            try {
                field = clazz.getField(triggerMisfireInstructionConst);
                int misfireInst = field.getInt(trigger);
                trigger.setMisfireInstruction(misfireInst);
            } catch (Exception e) {
                throw new ParseException("Unexpected/Unhandlable Misfire Instruction encountered '" + triggerMisfireInstructionConst + "', for trigger: " + trigger.getFullName(), -1);
            }
        }
        NodeList jobDataEntries = (NodeList) xpath.evaluate("q:job-data-map/q:entry", triggerNode, XPathConstants.NODESET);
        for (int k = 0; k < jobDataEntries.getLength(); k++) {
            Node entryNode = jobDataEntries.item(k);
            String key = getTrimmedToNullString(xpath, "q:key", entryNode);
            String value = getTrimmedToNullString(xpath, "q:value", entryNode);
            trigger.getJobDataMap().put(key, value);
        }
        if (log.isDebugEnabled())
            log.debug("Parsed trigger definition: " + trigger);
        addTriggerToSchedule(trigger);
    }
}
