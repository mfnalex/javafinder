package com.jeff_media.javafinder;

/*-
 * #%L
 * JavaFinder
 * %%
 * Copyright (C) 2023 JEFF Media GbR
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * JavaFinder core class.
 */
public class JavaFinder {

    private final Set<File> searchDirectories = new HashSet<>();

    public static JavaFinderBuilder builder() {
        return new JavaFinderBuilder();
    }

    JavaFinder(Collection<File> searchDirectories) {
        this.searchDirectories.addAll(searchDirectories);

        // Remove non-existing directories
        searchDirectories.removeIf(file -> file == null || !file.isDirectory());

        // Remove subdirectories of already added locations
        Collection<File> locationsCopy = Collections.unmodifiableCollection(searchDirectories);
        searchDirectories.removeIf(file -> {
            for(File other : locationsCopy) {
                if(other.equals(file)) continue;
                if(file.getAbsolutePath().startsWith(other.getAbsolutePath() + File.separator)) return true;
            }
            return false;
        });
    }

    /**
     * Finds Java installations on the system, sorted from newest to oldest, and JDKs before JREs of the same version.
     *
     * @return list of Java installations
     */
    public @NotNull List<JavaInstallation> findInstallations() {
        Set<JavaInstallation> installations = new HashSet<>();
        for (File location : searchDirectories) {
            //("Searching for Java installations in " + location.getAbsolutePath());
            if(location.isDirectory()) {
                //System.out.println("| - " + location.getAbsolutePath() + " is a directory");
                installations.addAll(new DirectoryCrawler(location, OperatingSystem.CURRENT).findInstallations());
            }
        }
        return installations.stream().sorted().collect(Collectors.toList());
    }

    /**
     * Finds Java installations on the system asynchronously, sorted from newest to oldest, and JDKs before JREs of the same version.
     *
     * @return future containing list of Java installations
     */
    public @NotNull CompletableFuture<List<JavaInstallation>> findInstallationsAsync() {
        return CompletableFuture.supplyAsync(this::findInstallations);
    }

    /**
     * Returns a set of commonly used Java installation locations for the current operating system. All files returned
     * are guaranteed to exist and to be directories.
     *
     * @return set of common Java installation locations
     */
    static Set<File> getDefaultJavaLocations() {
        Set<File> locations = new HashSet<>();
        String userHome = System.getProperty("user.home");
        File currentJavaHome = new File(System.getProperty("java.home"));

        // Check parent of current JAVA_HOME - if the current JRE is there, then others might be there too
        File parentOfCurrentJavaHome = currentJavaHome.getParentFile();
        File userHomeFile = new File(userHome);
        if(!parentOfCurrentJavaHome.equals(userHomeFile)) {
            locations.add(currentJavaHome.getParentFile());
        }

        locations.add(currentJavaHome);
        locations.add(new File(userHomeFile, ".jdks"));

        if (userHome != null) {
            // SDKMan
            locations.add(new File(userHome, String.join(File.separator, ".sdkman", "candidates", "java")));
        }

        switch (OperatingSystem.CURRENT) {
            case WINDOWS: {
                String programFiles = System.getenv("ProgramFiles");
                String programFilesX86 = System.getenv("ProgramFiles(x86)");
                if (programFiles != null) {
                    locations.add(new File(programFiles, "Java"));
                }
                if (programFilesX86 != null) {
                    locations.add(new File(programFilesX86, "Java"));
                }
                break;
            }
            case LINUX: {
                locations.add(new File("/usr/lib/jvm"));
                break;
            }
            case MACOS: {
                locations.add(new File("/Library/Java/JavaVirtualMachines"));
                locations.add(new File("/Library/Internet Plug-Ins/JavaAppletPlugin.plugin/Contents/Home"));
                if (userHome != null) {
                    locations.add(new File(userHome, "Library/Java/JavaVirtualMachines"));
                }
                break;
            }

            case OTHER: {
                // Show warning?
                break;
            }
        }

        return locations;
    }

    /**
     * Prints a list of all found Java versions to STDOUT, prefixing the currently running version with an asterisk.
     *
     * @param args not used
     */
    public static void main(String[] args) {
        JavaFinder.builder().build().findInstallations().forEach(java -> {
            System.out.println((java.isCurrentJavaVersion() ? "* " : "  ") + java.getType() + " " + java.getVersion().getMajor() + " (" + java.getVersion().getFullVersion() + ") at " + java.getHomeDirectory().getAbsolutePath());
        });
    }

}
