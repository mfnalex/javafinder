package com.jeff_media.javafinder;

import java.io.File;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * JavaFinder core class.
 */
public class JavaFinder {

    /**
     * Finds Java installations on the system, sorted from newest to oldest, and JDKs before JREs of the same version.
     * @return list of Java installations
     */
    public static List<JavaInstallation> findInstallations() {
        Set<JavaInstallation> installations = new HashSet<>();
        for (File location : getDefaultJavaLocations()) {
            installations.addAll(new DirectoryCrawler(location, OperatingSystem.CURRENT).findInstallations());
        }
        return installations.stream().sorted().collect(Collectors.toList());
    }

    /**
     * Returns a set of commonly used Java installation locations for the current operating system. This can contain
     * non-existing directories.
     * @return set of common Java installation locations
     */
    private static Set<File> getDefaultJavaLocations() {
        Set<File> locations = new HashSet<>();
        String userHome = System.getProperty("user.home");
        File currentJavaHome = new File(System.getProperty("java.home"));

        // Check parent of current JAVA_HOME - if the current JRE is there, then others might be there too
        locations.add(currentJavaHome.getParentFile());

        if(userHome != null) {
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
                if(userHome != null) {
                    locations.add(new File(userHome, "Library/Java/JavaVirtualMachines"));
                }
                break;
            }

            case UNKNOWN: {
                // Show warning?
                break;
            }
        }

        locations.removeIf(file -> file == null || !file.isDirectory());

        return locations;
    }

    public static void main(String[] args) {
        findInstallations().forEach(java -> {
            System.out.println((java.isCurrentJavaVersion() ? "* " : "  ") + java.getType() + " " + java.getJavaVersion().getMajor() + " (" + java.getFullVersion() + ") at " + java.getHomeDirectory().getAbsolutePath());
        });
    }

}
