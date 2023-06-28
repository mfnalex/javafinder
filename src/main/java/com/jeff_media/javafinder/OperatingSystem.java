package com.jeff_media.javafinder;

import lombok.Getter;

/**
 * Enum representing the operating system, ordered best to worst
 */
public enum OperatingSystem {
    MACOS,
    LINUX,
    UNKNOWN,
    WINDOWS("java.exe", "javac.exe");

    public static final OperatingSystem CURRENT = getCurrentOS();

    @Getter
    private final String javaExecutableName;
    @Getter
    private final String javacExecutableName;

    OperatingSystem(String javaExecutableName, String javacExecutableName) {
        this.javaExecutableName = javaExecutableName;
        this.javacExecutableName = javacExecutableName;
    }

    OperatingSystem() {
        this("java", "javac");
    }

    private static OperatingSystem getCurrentOS() {
        String osName = System.getProperty("os.name");

        if (osName.startsWith("Mac")) {
            return MACOS;
        }

        if (osName.startsWith("Linux") || osName.startsWith("LINUX")) {
            return LINUX;
        }

        if (osName.startsWith("Windows")) {
            return WINDOWS;
        }

        return UNKNOWN;
    }
}
