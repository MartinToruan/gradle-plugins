package com.traveloka.gradle.converter

/**
 * Created by salvian on 04/08/16.
 */
interface DependencyConverter {
    def convertCompileDeps(List<String> strings)
    def convertRuntimeDeps(List<String> strings)
    def convertTestCompileDeps(List<String> strings)
    def convertTestRuntimeDeps(List<String> strings)
}