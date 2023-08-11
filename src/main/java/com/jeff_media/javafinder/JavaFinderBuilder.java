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

    /**
     * Adds one or more search directories. Non-existing directories will be silently ignored.
     * @param directories directories to add
     * @return this builder
     */
    public JavaFinderBuilder addSearchDirectories(Collection<File> directories) {
        searchDirectories.addAll(directories);
        return this;
    }

    /**
     * Whether to check the default locations for Java installations. Default is true.
     * @param checkDefaultLocations whether to check the default locations for Java installations
     * @return this builder
     * @see JavaFinder#getDefaultJavaLocations()
     * @see #isCheckDefaultLocations()
     */
    public JavaFinderBuilder checkDefaultLocations(boolean checkDefaultLocations) {
        this.checkDefaultLocations = checkDefaultLocations;
        return this;
    }

    /**
     * Whether to check the default locations for Java installations. Default is true.
     * @return whether to check the default locations for Java installations
     * @see #checkDefaultLocations(boolean)
     */
    public boolean isCheckDefaultLocations() {
        return checkDefaultLocations;
    }

    /**
     * Returns the manually added search directories (excluding the default locations, if {@link #checkDefaultLocations(boolean)} is set to true).
     * @return the manually added search directories
     */
    public Collection<File> getSearchDirectories() {
        return Collections.unmodifiableCollection(searchDirectories);
    }

    /**
     * Creates a new JavaFinderBuilder
     */
    public JavaFinderBuilder() {

    }

    /**
     * Creates a new {@link JavaFinder} with the current settings.
     */
    public JavaFinder build() {
        if(checkDefaultLocations) {
            searchDirectories.addAll(JavaFinder.getDefaultJavaLocations());
        }
        return new JavaFinder(searchDirectories);
    }

}
