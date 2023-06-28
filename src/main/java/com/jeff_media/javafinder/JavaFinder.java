package com.jeff_media.javafinder;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public class JavaFinder {

    public static Collection<JavaInstallation> findInstallations() {
        Set<JavaInstallation> installations = new LinkedHashSet<>();
        for (File location : getDefaultJavaLocations()) {
            installations.addAll(new DirectoryCrawler(location, OperatingSystem.CURRENT.getJavaExecutableName()).findInstallations());
        }
        return installations;
    }

    private static Set<File> getDefaultJavaLocations() {
        Set<File> locations = new HashSet<>();

        File currentJavaHome = new File(System.getProperty("java.home"));
        locations.add(currentJavaHome.getParentFile());

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
                String userHome = System.getProperty("user.home");
                if(userHome != null) {
                    locations.add(new File(userHome, ".sdkman/candidates/java"));
                }
                break;
            }
            case MACOS: {
                locations.add(new File("/Library/Java/JavaVirtualMachines"));
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
            System.out.println("Found Java " + java.getJavaVersion().getMajor() + " (" + java.getFullVersion() + ") at " + java.getHomeDirectory().getAbsolutePath());
        });
    }

}
