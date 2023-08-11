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
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Java installation
 */
public class JavaInstallation implements Comparable<JavaInstallation> {

    private final @NotNull File homeDirectory;
    private final @NotNull File javaExecutable;
    private final @Nullable File javacExecutable;

    private final @NotNull JavaVersion version;
    private final @NotNull JavaType type;
    private final boolean isCurrentJavaVersion;

    public JavaInstallation(@NotNull File homeDirectory,
                            @NotNull File javaExecutable,
                            @Nullable File javaxExecutable,
                            @NotNull JavaType type) throws IOException {
        this.homeDirectory = homeDirectory;
        this.javaExecutable = javaExecutable;
        this.javacExecutable = javaxExecutable;
        this.type = type;
        this.version = JavaVersion.fromJavaVersionOutput(captureJavaVersionOutput());
        String currentJavaHome = System.getProperty("java.home");
        if (currentJavaHome != null) {
            isCurrentJavaVersion = currentJavaHome.equals(homeDirectory.getAbsolutePath());
        } else {
            isCurrentJavaVersion = false;
        }
    }

    private List<String> captureJavaVersionOutput() throws IOException {
        ProcessBuilder builder = new ProcessBuilder(javaExecutable.getAbsolutePath(), "-version");
        List<String> completeVersionOutput = new ArrayList<>();
            Process process = builder.start();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    completeVersionOutput.add(line);
                }
            }

        return completeVersionOutput;
    }

    @Override
    public int compareTo(final JavaInstallation o) {
        int result = version.compareTo(o.version);
        if (result != 0) {
            return result;
        }
        return type.compareTo(o.type);
    }

    /**
     * Returns the home directory of this Java installation
     *
     * @return home directory
     */
    @NotNull
    public File getHomeDirectory() {
        return this.homeDirectory;
    }

    /**
     * Returns the executable of this Java installation
     *
     * @return executable
     */
    @NotNull
    public File getJavaExecutable() {
        return this.javaExecutable;
    }

    /**
     * Returns the javac executable of this Java installation if it's a JDK, or null if it's a JRE
     *
     * @return javac executable, or null if this is a JRE
     */
    @Nullable
    public File getJavacExecutable() {
        return this.javacExecutable;
    }

    /**
     * Returns the version of this Java installation
     *
     * @return version
     */
    public @NotNull JavaVersion getVersion() {
        return this.version;
    }

    /**
     * Returns the type of this Java installation
     *
     * @return type
     */
    public @NotNull JavaType getType() {
        return this.type;
    }

    /**
     * Returns true if this Java installation is the same as the one currently being used
     *
     * @return true if this is the current Java installation
     */
    public boolean isCurrentJavaVersion() {
        return this.isCurrentJavaVersion;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof JavaInstallation)) return false;
        final JavaInstallation other = (JavaInstallation) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$homeDirectory = this.getHomeDirectory();
        final Object other$homeDirectory = other.getHomeDirectory();
        if (this$homeDirectory == null ? other$homeDirectory != null : !this$homeDirectory.equals(other$homeDirectory))
            return false;
        final Object this$executable = this.getJavaExecutable();
        final Object other$executable = other.getJavaExecutable();
        if (this$executable == null ? other$executable != null : !this$executable.equals(other$executable))
            return false;
        final Object this$version = this.getVersion();
        final Object other$version = other.getVersion();
        if (this$version == null ? other$version != null : !this$version.equals(other$version)) return false;
        final Object this$type = this.getType();
        final Object other$type = other.getType();
        if (this$type == null ? other$type != null : !this$type.equals(other$type)) return false;
        if (this.isCurrentJavaVersion() != other.isCurrentJavaVersion()) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof JavaInstallation;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $homeDirectory = this.getHomeDirectory();
        result = result * PRIME + ($homeDirectory == null ? 43 : $homeDirectory.hashCode());
        final Object $executable = this.getJavaExecutable();
        result = result * PRIME + ($executable == null ? 43 : $executable.hashCode());
        final Object $version = this.getVersion();
        result = result * PRIME + ($version == null ? 43 : $version.hashCode());
        final Object $type = this.getType();
        result = result * PRIME + ($type == null ? 43 : $type.hashCode());
        result = result * PRIME + (this.isCurrentJavaVersion() ? 79 : 97);
        return result;
    }

    public String toString() {
        return "JavaInstallation(homeDirectory=" + this.getHomeDirectory() + ", executable=" + this.getJavaExecutable() + ", version=" + this.getVersion() + ", type=" + this.getType() + ", isCurrentJavaVersion=" + this.isCurrentJavaVersion() + ")";
    }
}
