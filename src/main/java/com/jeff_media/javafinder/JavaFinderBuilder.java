package com.jeff_media.javafinder;

import java.io.File;
import java.util.*;

/**
 * Builder for {@link JavaFinder}
 */
public class JavaFinderBuilder {

    private final Set<File> searchDirectories = new HashSet<>();
    private boolean checkDefaultLocations = true;

    /**
     * Adds one or more search directories. Non-existing directories will be silently ignored.
     * @param directories directories to add
     * @return this builder
     */
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
