package dariusb;

import japa.parser.JavaParser;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.*;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

    private static final String DATA_CLASS = "data-class";
    private static final String GOD_CLASS = "god-class";
    private static final String FEATURE_ENVY = "feature-envy";
    private static final String LARGE_METHOD = "large-method";

    public static void main(String[] args) {
        if (args.length != 3)
            throw new IllegalArgumentException();

        String csvFile = args[0];
        String datasetPath = args[1];
        String targetPath = args[2];
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";

        int counter = 0;
        int successful = 0;
        try {

            br = new BufferedReader(new FileReader(csvFile));
            br.readLine();

            while ((line = br.readLine()) != null) {

                String[] columns = line.split(cvsSplitBy);

                String fileName = columns[0];
                String method = columns[1];
                String className = columns[2];
                String severity = columns[3];
                String smell = columns[4];

                String content = readAllText(datasetPath, fileName);

                successful += processRecord(className, method, smell, content, targetPath, counter, severity);

                counter++;
                if (counter % 10 == 0)
                    System.out.println("processed " + counter);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        System.out.println("Processed: " + successful + " / " + counter);
    }

    private static int processRecord(String className, String method, String smell, String content,
                                      String targetPath, int counter, String severity)
    {
        CompilationUnit cu = null;
        try {
            cu = JavaParser.parse(
                    new ByteArrayInputStream(content.getBytes("UTF-16")), "UTF-16");

            var members = extractMembers(cu, className);

            String header = constructArtificialHeaderMethod(members);
            List<String> smellyMethods = new LinkedList<String>();

            if (smell.equals(DATA_CLASS) || smell.equals(GOD_CLASS))
                smellyMethods = retrieveSmellyClassMethods(members);
            else {
                String smellyMethod = retrieveSmellyMethod(members, method);
                smellyMethods.add(smellyMethod);
            }

            String ultimateTarget = targetPath + "/" + smell + "_" + severity + "_" + counter;
            new File(ultimateTarget).mkdirs();

            try (PrintWriter out = new PrintWriter(ultimateTarget +"/header.java")) {
                out.println(header);
            }

            int methodCounter = 0;
            for (String smellyMethod : smellyMethods) {
                methodCounter++;
                try (PrintWriter out = new PrintWriter(ultimateTarget + "/" + methodCounter + ".java")) {
                    out.println(smellyMethod);
                }
            }

            System.out.println("Processed " + className + " " + method + " " + smell);

            return 1;
        } catch (Exception e) {
            System.out.println("Failed " + className + " " + method + " " + smell);
        }

        return 0;
    }

    private static List<String> retrieveSmellyClassMethods(List<BodyDeclaration> members) {
        var methods = members.stream()
                .filter(x -> x instanceof MethodDeclaration)
                .map(x -> x.toString())
                .collect(Collectors.toList());

        return methods;
    }

    private static String retrieveSmellyMethod(List<BodyDeclaration> members, String method) throws Exception {
        var methods = members.stream()
                .filter(x -> (x instanceof MethodDeclaration || x instanceof ConstructorDeclaration) &&
                        sub(x.toString(),method))
                .collect(Collectors.toList());

        if (methods.size() != 1)
            throw new Exception();

        return methods.get(0).toString();
    }

    private static boolean sub(String string, String substring) {
        String [] parts = string.split("\\)")[0].split("\\(");
        String [] nameParts = parts[0].split(" ");

        String name1 = nameParts[nameParts.length - 1];

        String name2 = substring.split("\\(")[0];

        if (!name1.equals(name2))
            return false;

        String params1 = "";
        if (parts.length > 1)
            params1 = parts[1];

        String params2 = "";
        String[] parts2 = substring.split("\\)")[0].split("\\(");
        if (parts2.length > 1)
            params2 = parts2[1];

        if (params1.equals(params2))
            return true;

        int len1 = 0;
        for (String param : params1.split(",")) {
            String type = param.trim().split(" ")[0];
            len1 += type.length();

            if (!params2.contains(type))
                return false;
        }

        return len1 == params2.length();
    }

    private static String constructArtificialHeaderMethod(List<BodyDeclaration> members) {
        var result = new StringBuilder();

        result.append("void method0() { " + System.lineSeparator());

        members.stream()
                .filter(x -> x instanceof FieldDeclaration)
                .forEach(x -> result.append(x.toString() + System.lineSeparator()));

        result.append("}");

        return result.toString();
    }

    private static List<BodyDeclaration> extractMembers(CompilationUnit cu, String className) throws Exception {
        TypeDeclaration type = cu.getTypes().get(0);
        var members = type.getMembers();

        if (className.contains(".")) {
            var parts = className.split("\\.");

            className = parts[parts.length - 1];

            String finalClassName = className;
            var subClass = members.stream()
                    .filter(x -> x instanceof ClassOrInterfaceDeclaration &&
                            ((ClassOrInterfaceDeclaration)x).getName().equals(finalClassName))
                    .collect(Collectors.toList());

            if (subClass.size() != 1)
                throw new Exception();

            members = ((TypeDeclaration)subClass.get(0)).getMembers();
        }

        return members;
    }

    private static String readAllText(String basePath, String fileName) {
        BufferedReader br = null;
        var sb = new StringBuilder();

        try {

            br = new BufferedReader(new FileReader(basePath + "/" + fileName));
            int c = 0;
            while ((c = br.read()) != -1) {
                sb.append((char) c);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();
    }
}
