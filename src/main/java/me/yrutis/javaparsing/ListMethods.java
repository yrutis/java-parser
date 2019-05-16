package me.yrutis.javaparsing;

import java.io.*;
import java.util.*;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.google.common.base.Strings;
import me.yrutis.support.DirExplorer;

import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class ListMethods {

    @SuppressWarnings("unchecked")
    public static void ListMethodNamesAndBody(File projectDir) throws IOException {

        JSONArray mainJson = new JSONArray();


        new DirExplorer((level, path, file) -> path.endsWith(".java"), (level, path, file) -> {
            System.out.println(path);
            System.out.println(Strings.repeat("=", path.length()));

            try {
                new VoidVisitorAdapter<Object>() {
                    @Override
                    public void visit(MethodDeclaration n, Object arg) {

                        super.visit(n, arg);
                        //List<String> method = new ArrayList<>();

                        System.out.println(" * " + n.getName());
                        System.out.println(" Comments " + n.getAllContainedComments());


                        //System.out.println(" * " + method);
                        List<String> methodCleaned = new ArrayList<>();


                        //System.out.println(" * " + methodCleaned);
                        System.out.println(" with comments + " + n.getBody().toString());

                        JSONObject obj = new JSONObject();
                        //obj.put("methodBody", n.getBody());


                        for (Comment child : n.getAllContainedComments()) {
                            child.remove();
                        }


                        /*
                        if(n.getBody().isPresent()) {
                            n.getBody().get().getStatements().forEach(statement -> {
                                method.addAll(Arrays.asList(statement.toString().split("(\\.|\\(|\\)|\\s|;|\\[|\\]|\\{|\\})")));
                            });
                        }


                        for (int i = 0; i < method.size(); i++) {
                            if (method.get(i) != null && !method.get(i).equals(" ") && !method.get(i).equals("")) {
                                methodCleaned.add(method.get(i));
                            }
                        }
                        */


                        JSONArray params = new JSONArray();
                        for (Parameter child : n.getParameters()) {
                            params.add(child.toString());
                        }

                        //methodDeclaration.getParameters().stream().map(p -> p.getName()).collect(Collectors.toList()),
                        obj.put("path", path);
                        obj.put("parameters", params);

                        obj.put("Type", n.getType().asString());
                        //obj.put("methodBodySplit", methodCleaned);
                        obj.put("methodBody", n.getBody().toString());
                        obj.put("methodName", n.getNameAsString());
                        JSONArray methodInfo = new JSONArray();
                        //methodInfo.add(obj);

                        //System.out.println("\nJSON Object: " + methodInfo);
                        mainJson.add(obj);


                    }
                }.visit(JavaParser.parse(file), null);
                System.out.println(); // empty line
            } catch (IOException e) {
                new RuntimeException(e);
            }
        }).explore(projectDir);


        //System.out.println("\nJSON Object: " + mainJson);
        try (FileWriter file = new FileWriter("C:\\Users\\yvesr\\Downloads\\analyze-java-code-examples-master\\zxing.json")) {
            file.write(mainJson.toJSONString());
            System.out.println("Successfully Copied JSON Object to File...");
            //System.out.println("\nJSON Object: " + mainJson);
        }

    }

    public static void main(String[] args) throws IOException {
        File projectDir = new File("source_to_parse_train/zxing");
        List<List> fullList = new ArrayList<List>();
        ListMethodNamesAndBody(projectDir);

    }
}
