package me.yrutis.javaparsing;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;

/**
 * @author Crunchify.com
 */

public class Trial {
            public static void main(String[] args) {
                JavaParser.getStaticConfiguration().setAttributeComments(false);
                CompilationUnit cu = JavaParser.parse("/**a*/package a.b.c; //\nclass X{}");
                System.out.println(cu);
            }
        }

