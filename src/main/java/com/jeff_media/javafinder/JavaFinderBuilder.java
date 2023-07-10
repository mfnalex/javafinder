package com.jeff_media.javafinder;

import java.io.File;
import java.util.*;

public class JavaFinderBuilder {

    private final Set<File> searchDirectories = new HashSet<>();
    private boolean checkDefaultLocations = true;

    public JavaFinderBuilder addSearchDirectories(File... directories) {
        return addSearchDirectories(Arrays.asList(directories));
    }

    public JavaFinderBuilder addSearchDirectories(Collection<File> directories) {
        searchDirectories.addAll(directories);
        return this;
    }

    public JavaFinderBuilder checkDefaultLocations(boolean checkDefaultLocations) {
        this.checkDefaultLocations = checkDefaultLocations;
        return this;
    }

    public boolean isCheckDefaultLocations() {
        return checkDefaultLocations;
    }

    public Collection<File> getSearchDirectories() {
        return Collections.unmodifiableCollection(searchDirectories);
    }

    public JavaFinderBuilder() {

    }

    public JavaFinder build() {
        if(checkDefaultLocations) {
            searchDirectories.addAll(JavaFinder.getDefaultJavaLocations());
        }
        return new JavaFinder(searchDirectories);
    }

}
