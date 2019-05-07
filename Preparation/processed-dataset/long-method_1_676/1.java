public static void main(String[] args) throws RuleSetNotFoundException, IOException, PMDException {
    String srcDir = findOptionalStringValue(args, "--source-directory", "/usr/local/java/src/java/lang/");
    List<File> files = new FileFinder().findFilesFrom(srcDir, new SourceFileOrDirectoryFilter(new SourceFileSelector()), true);
    SourceType jdk = SourceType.JAVA_14;
    String targetjdk = findOptionalStringValue(args, "--targetjdk", "1.4");
    if (targetjdk.equals("1.3")) {
        jdk = SourceType.JAVA_13;
    } else if (targetjdk.equals("1.5")) {
        jdk = SourceType.JAVA_15;
    } else if (targetjdk.equals("1.6")) {
        jdk = SourceType.JAVA_16;
    } else if (targetjdk.equals("1.7")) {
        jdk = SourceType.JAVA_17;
    }
    boolean debug = findBooleanSwitch(args, "--debug");
    boolean parseOnly = findBooleanSwitch(args, "--parse-only");
    if (debug)
        System.out.println("Using JDK " + jdk.getId());
    if (parseOnly) {
        parseStress(jdk, files);
    } else {
        String ruleset = findOptionalStringValue(args, "--ruleset", "");
        if (debug)
            System.out.println("Checking directory " + srcDir);
        Set<Result> results = new TreeSet<Result>();
        RuleSetFactory factory = new RuleSetFactory();
        if (ruleset.length() > 0) {
            SimpleRuleSetNameMapper mapper = new SimpleRuleSetNameMapper(ruleset);
            stress(jdk, factory.createSingleRuleSet(mapper.getRuleSets()), files, results, debug);
        } else {
            Iterator<RuleSet> i = factory.getRegisteredRuleSets();
            while (i.hasNext()) {
                stress(jdk, i.next(), files, results, debug);
            }
        }
        System.out.println("=========================================================");
        System.out.println("Rule\t\t\t\t\t\tTime in ms");
        System.out.println("=========================================================");
        for (Result result : results) {
            StringBuffer out = new StringBuffer(result.rule.getName());
            while (out.length() < 48) {
                out.append(' ');
            }
            out.append(result.time);
            System.out.println(out.toString());
        }
    }
    System.out.println("=========================================================");
}
