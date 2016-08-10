package com.traveloka.gradle.data.bazel

import java.util.regex.Matcher

/**
 * Created by salvian on 04/08/16.
 */
import java.util.regex.Pattern

//TODO use real parser
public class BazelBuildFile {
    List<BazelJavaLibrary> javaLibraries = new ArrayList<>();

    public BazelBuildFile(String path) {
        File buildFile = new File(path + "/BUILD")
        if (buildFile.exists()) {
            javaLibraries = loadJavaLibries(buildFile)
        }
    }

    private static List<BazelJavaLibrary> loadJavaLibries(File buildFile) {
        List<BazelJavaLibrary> javaLibraries = new ArrayList<>();
        List<String> javaLib = new ArrayList<>()
        Stack<Character> brace = new Stack<>()
        String line
        def isJavaLib = false

        String __temp = ""
        // Get the Java_library
        buildFile.eachLine {
            line = "$it".trim()

            if (line.startsWith("java_library")) {
                isJavaLib = true
            }

            if (isJavaLib) {
                int len = line.length()
                for (int i = 0; i < len; i++) {
                    char ch = line.charAt(i)
                    __temp += ch
                    if (ch == '(') {
                        brace.push(ch)
                    } else if (ch == ')') {
                        brace.pop()

                        if (brace.isEmpty()) {
                            javaLib.add(__temp)
                            __temp = ""
                            isJavaLib = false
                            break
                        }
                    }
                }
            }
        }
        for (String lib : javaLib) {
            String lib_name
            lib = lib.replaceAll("#[^\n]+","")
            Pattern pattern = Pattern.compile("\\bname = \"([^\"]+)\\\"")

            Matcher matcher = pattern.matcher(lib)
            if (matcher.find()) {
                lib_name = matcher.group(1)
                BazelJavaLibrary bazelJavaLibrary = new BazelJavaLibrary(lib_name)

                // Get 'deps'
                pattern = Pattern.compile("\\bdeps = \\[([^]]+)\\]",Pattern.MULTILINE)
                matcher = pattern.matcher(lib)
                String depString
                if (matcher.find() && (depString = matcher.group(1).trim()).length() > 0) {
                    String[] listDeps = depString.split(',')
                    for (String depsName : listDeps) {
                        if (depsName.length()>0) {
                            depsName = depsName.substring(depsName.indexOf("\"") + 1, depsName.lastIndexOf("\""))
                            bazelJavaLibrary.deps.add(depsName)
                        }
                    }
                }

                // Get the 'runtime_deps'
                pattern = Pattern.compile("\\bruntime_deps = \\[([^]]+)\\]",Pattern.MULTILINE)
                matcher = pattern.matcher(lib)

                if (matcher.find()) {
                    String[] listDeps = matcher.group(1).split(",")

                    for (String runtimeDepsName : listDeps) {
                        if (!runtimeDepsName.startsWith("#")) {
                            runtimeDepsName = runtimeDepsName.substring(runtimeDepsName.indexOf("\"") + 1, runtimeDepsName.lastIndexOf("\""))
                            bazelJavaLibrary.runtimeDeps.add(runtimeDepsName)
                        }
                    }
                }
                javaLibraries.add(bazelJavaLibrary)
            }
        }

        return javaLibraries
    }
}
