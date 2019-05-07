/**
     * Performs the subsequent ReTrace operations.
     */
public void execute() throws IOException {
    // Read the mapping file. 
    MappingReader mappingReader = new MappingReader(mappingFile);
    mappingReader.pump(this);
    StringBuffer expressionBuffer = new StringBuffer(regularExpression.length() + 32);
    char[] expressionTypes = new char[32];
    int expressionTypeCount = 0;
    int index = 0;
    while (true) {
        int nextIndex = regularExpression.indexOf('%', index);
        if (nextIndex < 0 || nextIndex == regularExpression.length() - 1 || expressionTypeCount == expressionTypes.length) {
            break;
        }
        expressionBuffer.append(regularExpression.substring(index, nextIndex));
        expressionBuffer.append('(');
        char expressionType = regularExpression.charAt(nextIndex + 1);
        switch(expressionType) {
            case 'c':
                expressionBuffer.append(REGEX_CLASS);
                break;
            case 'C':
                expressionBuffer.append(REGEX_CLASS_SLASH);
                break;
            case 'l':
                expressionBuffer.append(REGEX_LINE_NUMBER);
                break;
            case 't':
                expressionBuffer.append(REGEX_TYPE);
                break;
            case 'f':
                expressionBuffer.append(REGEX_MEMBER);
                break;
            case 'm':
                expressionBuffer.append(REGEX_MEMBER);
                break;
            case 'a':
                expressionBuffer.append(REGEX_ARGUMENTS);
                break;
        }
        expressionBuffer.append(')');
        expressionTypes[expressionTypeCount++] = expressionType;
        index = nextIndex + 2;
    }
    expressionBuffer.append(regularExpression.substring(index));
    Pattern pattern = Pattern.compile(expressionBuffer.toString());
    // Read the stack trace file. 
    LineNumberReader reader = new LineNumberReader(stackTraceFile == null ? (Reader) new InputStreamReader(System.in) : (Reader) new BufferedReader(new FileReader(stackTraceFile)));
    try {
        StringBuffer outLine = new StringBuffer(256);
        List extraOutLines = new ArrayList();
        String className = null;
        // Read the line in the stack trace. 
        while (true) {
            String line = reader.readLine();
            if (line == null) {
                break;
            }
            Matcher matcher = pattern.matcher(line);
            if (matcher.matches()) {
                int lineNumber = 0;
                String type = null;
                String arguments = null;
                // Figure out a class name, line number, type, and 
                // arguments beforehand. 
                for (int expressionTypeIndex = 0; expressionTypeIndex < expressionTypeCount; expressionTypeIndex++) {
                    int startIndex = matcher.start(expressionTypeIndex + 1);
                    if (startIndex >= 0) {
                        String match = matcher.group(expressionTypeIndex + 1);
                        char expressionType = expressionTypes[expressionTypeIndex];
                        switch(expressionType) {
                            case 'c':
                                className = originalClassName(match);
                                break;
                            case 'C':
                                className = originalClassName(ClassUtil.externalClassName(match));
                                break;
                            case 'l':
                                lineNumber = Integer.parseInt(match);
                                break;
                            case 't':
                                type = originalType(match);
                                break;
                            case 'a':
                                arguments = originalArguments(match);
                                break;
                        }
                    }
                }
                // Actually construct the output line. 
                int lineIndex = 0;
                outLine.setLength(0);
                extraOutLines.clear();
                for (int expressionTypeIndex = 0; expressionTypeIndex < expressionTypeCount; expressionTypeIndex++) {
                    int startIndex = matcher.start(expressionTypeIndex + 1);
                    if (startIndex >= 0) {
                        int endIndex = matcher.end(expressionTypeIndex + 1);
                        String match = matcher.group(expressionTypeIndex + 1);
                        // Copy a literal piece of input line. 
                        outLine.append(line.substring(lineIndex, startIndex));
                        char expressionType = expressionTypes[expressionTypeIndex];
                        switch(expressionType) {
                            case 'c':
                                className = originalClassName(match);
                                outLine.append(className);
                                break;
                            case 'C':
                                className = originalClassName(ClassUtil.externalClassName(match));
                                outLine.append(ClassUtil.internalClassName(className));
                                break;
                            case 'l':
                                lineNumber = Integer.parseInt(match);
                                outLine.append(match);
                                break;
                            case 't':
                                type = originalType(match);
                                outLine.append(type);
                                break;
                            case 'f':
                                originalFieldName(className, match, type, outLine, extraOutLines);
                                break;
                            case 'm':
                                originalMethodName(className, match, lineNumber, type, arguments, outLine, extraOutLines);
                                break;
                            case 'a':
                                arguments = originalArguments(match);
                                outLine.append(arguments);
                                break;
                        }
                        // Skip the original element whose processed version 
                        // has just been appended. 
                        lineIndex = endIndex;
                    }
                }
                // Copy the last literal piece of input line. 
                outLine.append(line.substring(lineIndex));
                // Print out the main line. 
                System.out.println(outLine);
                // Print out any additional lines. 
                for (int extraLineIndex = 0; extraLineIndex < extraOutLines.size(); extraLineIndex++) {
                    System.out.println(extraOutLines.get(extraLineIndex));
                }
            } else {
                // Print out the original line. 
                System.out.println(line);
            }
        }
    } catch (IOException ex) {
        throw new IOException("Can't read stack trace (" + ex.getMessage() + ")");
    } finally {
        if (stackTraceFile != null) {
            try {
                reader.close();
            } catch (IOException ex) {
            }
        }
    }
}
