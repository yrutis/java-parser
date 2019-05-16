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

                        System.out.println(" * " + n.getName());
                        System.out.println(" Comments " + n.getAllContainedComments());

                        System.out.println(" with comments + " + n.getBody().toString());

                        JSONObject obj = new JSONObject();


                        for (Comment child : n.getAllContainedComments()) {
                            child.remove();
                        }

                        System.out.println(" without comments + " + n.getBody().toString());


                        JSONArray params = new JSONArray();
                        for (Parameter child : n.getParameters()) {
                            params.add(child.toString());
                        }

                        obj.put("path", path);
                        obj.put("parameters", params);

                        obj.put("Type", n.getType().asString());
                        obj.put("methodBody", n.getBody().toString());
                        obj.put("methodName", n.getNameAsString());
                        JSONArray methodInfo = new JSONArray();

                        mainJson.add(obj);


                    }
                }.visit(JavaParser.parse(file), null);
                System.out.println(); // empty line
            } catch (IOException e) {
                new RuntimeException(e);
            }
        }).explore(projectDir);


        try (FileWriter file = new FileWriter("C:\\Users\\yvesr\\Downloads\\analyze-java-code-examples-master\\androidTest.json")) {
            file.write(mainJson.toJSONString());
            System.out.println("Successfully Copied JSON Object to File...");
        }

    }

    public static void main(String[] args) throws IOException {
        File projectDir = new File("new_source_to_parse/android-test-master");
        List<List> fullList = new ArrayList<List>();
        ListMethodNamesAndBody(projectDir);
    }
}
