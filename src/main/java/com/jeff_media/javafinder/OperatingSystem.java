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
    @NotNull
    public static final OperatingSystem CURRENT = getCurrentOS();

    @NotNull
    private final String javaExecutableName;
    @NotNull
    private final String javacExecutableName;

    OperatingSystem(@NotNull String javaExecutableName, @NotNull String javacExecutableName) {
        this.javaExecutableName = javaExecutableName;
        this.javacExecutableName = javacExecutableName;
    }

    OperatingSystem() {
        this("java", "javac");
    }

    @NotNull
    private static OperatingSystem getCurrentOS() {
        String osName = System.getProperty("os.name");
        osName = osName == null ? "" : osName.toLowerCase();

        if (osName.startsWith("mac")) {
            return MACOS;
        }

        if (osName.startsWith("linux")) {
            return LINUX;
        }

        if (osName.startsWith("windows")) {
            return WINDOWS;
        }

        return OTHER;
    }

    /**
     * Returns the name of the Java executable for this operating system. This returns "java.exe" for Windows and "java" for all other operating systems.
     *
     * @return name of the Java executable
     */
    @NotNull
    public String getJavaExecutableName() {
        return this.javaExecutableName;
    }

    /**
     * Returns the name of the Java compiler executable for this operating system. This returns "javac.exe" for Windows and "javac" for all other operating systems.
     *
     * @return name of the Java compiler executable
     */
    @NotNull
    public String getJavacExecutableName() {
        return this.javacExecutableName;
    }
}
