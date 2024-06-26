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
import java.io.FileFilter;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * JavaFinder core class.
 */
public class JavaFinder {

    private final Set<File> searchDirectories = new HashSet<>();

    JavaFinder(Collection<File> searchDirectories) {
        this.searchDirectories.addAll(searchDirectories);

        // Remove non-existing directories
        searchDirectories.removeIf(file -> file == null || !file.isDirectory());

        // Remove subdirectories of already added locations
        Collection<File> locationsCopy = Collections.unmodifiableCollection(searchDirectories);
        searchDirectories.removeIf(file -> {
            for (File other : locationsCopy) {
                if (other.equals(file)) continue;
                if (file.getAbsolutePath().startsWith(other.getAbsolutePath() + File.separator)) return true;
            }
            return false;
        });
    }

    /**
     * Creates a new JavaFinderBuilder
     *
     * @return JavaFinderBuilder
     */
    public static JavaFinderBuilder builder() {
        return new JavaFinderBuilder();
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
        if (!parentOfCurrentJavaHome.equals(userHomeFile)) {
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
                addProgramFilesSubdir(locations, "Java");
                addProgramFilesSubdir(locations, "Eclipse Foundation");
                addProgramFilesSubdir(locations, "Eclipse Adoptium");
                addProgramFilesSubdirs(locations, ProgramFilesFileFilter.INSTANCE);
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
    
    private static void addProgramFilesSubdir(Collection<File> locations, String name) {
        String programFiles = System.getenv("ProgramFiles");
        String programFilesX86 = System.getenv("ProgramFiles(x86)");
        if (programFiles != null) {
            addFolderIfExists(locations, new File(programFiles, name));
        }
        if (programFilesX86 != null) {
            addFolderIfExists(locations, new File(programFilesX86, name));
        }
    }

    private static void addProgramFilesSubdirs(Collection<File> locations, FileFilter predicate) {
        String programFiles = System.getenv("ProgramFiles");
        String programFilesX86 = System.getenv("ProgramFiles(x86)");
        if (programFiles != null) {
            addSubdirs(locations, new File(programFiles), predicate);
        }
        if (programFilesX86 != null) {
            addSubdirs(locations, new File(programFilesX86), predicate);
        }
    }

    private static void addSubdirs(Collection<File> locations, File folder, FileFilter predicate) {
        if(folder.isDirectory()) {
            try {
                for(File subdir : Objects.requireNonNull(folder.listFiles(predicate))) {
                    addFolderIfExists(locations, subdir);
                }
            } catch (Exception ignored) {

            }
        }
    }

    private static void addFolderIfExists(Collection<File> locations, File file) {
        if(locations.contains(file)) {
            return;
        }
        if(file.isDirectory()) {
            locations.add(file);
        }
    }

    /**
     * Finds Java installations on the system, sorted from newest to oldest, and JDKs before JREs of the same version.
     *
     * @return list of Java installations
     * @deprecated use {@link #findInstallationsAsync()} instead
     */
    @Deprecated
    public @NotNull List<JavaInstallation> findInstallations() {
        Set<JavaInstallation> installations = new HashSet<>();
        for (File location : searchDirectories) {
            if (location.isDirectory()) {
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
        Set<JavaInstallation> installations = ConcurrentHashMap.newKeySet();

        return CompletableFuture.allOf(searchDirectories
                        .stream()
                        .filter(File::isDirectory)
                        .map(location -> CompletableFuture.runAsync(() -> installations
                                .addAll(new DirectoryCrawler(location, OperatingSystem.CURRENT).findInstallations())))
                        .toArray(CompletableFuture[]::new))
                .thenApply(Void -> installations.stream().sorted().collect(Collectors.toList()));

    }


}
