package com.jeff_media.javafinder;

/**
 * Enum representing the operating system
 */
public enum OperatingSystem {
    /**
     * macOS, Mac OSX, etc.
     */
    MACOS,
    /**
     * Linux
     */
    LINUX,
    /**
     * Any other OS
     */
    OTHER,
    /**
     * Windows
     */
    WINDOWS("java.exe", "javac.exe");

    /**
     * The current operating system
     */
    public static final OperatingSystem CURRENT = getCurrentOS();

    private final String javaExecutableName;
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

        return OTHER;
    }

    /**
     * Returns the name of the Java executable for this operating system. This returns "java.exe" for Windows and "java" for all other operating systems.
     * @return name of the Java executable
     */
    public String getJavaExecutableName() {
        return this.javaExecutableName;
    }

    /**
     * Returns the name of the Java compiler executable for this operating system. This returns "javac.exe" for Windows and "javac" for all other operating systems.
     * @return name of the Java compiler executable
     */
    public String getJavacExecutableName() {
        return this.javacExecutableName;
    }
}
