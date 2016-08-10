package com.traveloka.gradle.data.bazel
/**
 * Created by salvian on 04/08/16.
 */
public class BazelJavaLibrary {
    String name;
    List<String> deps;
    List<String> runtimeDeps;
    BazelJavaLibrary (String __name){
        name = __name
        deps = new ArrayList<>()
        runtimeDeps = new ArrayList<>()
    }
}
