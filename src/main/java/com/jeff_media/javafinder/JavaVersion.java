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

import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parses the output of java -version according to https://openjdk.org/jeps/223
 */
public class JavaVersion implements Comparable<JavaVersion> {

    private static final Pattern VERSION_PATTERN = Pattern.compile("(?<major>[0-9]+)([.](?<minor>[0-9]+))?([.](?<security>[0-9]+))?(-(?<prerelease>[a-zA-Z0-9]+))?");
    private static final Pattern PRE_9_VERSION_PATTERN = Pattern.compile("1[.](?<major>[0-9]+)([.](?<minor>[0-9]+))?(_(?<security>[0-9]+)?)?(-(?<prerelease>[a-zA-Z0-9]+))?");

    private final int major;
    private final int minor;
    @Nullable private final String security;
    @Nullable private final String preRelease;

    private final List<String> javaVersionOutput;
    @NotNull private final String shortVersion;
    @NotNull private final String fullVersion;

    private JavaVersion(int major,
                        int minor,
                        @Nullable String security,
                        @Nullable String preRelease,
                        List<String> javaVersionOutput,
                        @NotNull String shortVersion,
                        @NotNull String fullVersion) {
        this.major = major;
        this.minor = minor;
        this.security = security;
        this.preRelease = preRelease;
        this.javaVersionOutput = javaVersionOutput;
        this.shortVersion = shortVersion;
        this.fullVersion = fullVersion;
    }

    /**
     * Creates a JavaVersion object from the output of {@code java -version}
     *
     * @param javaVersionOutput output of {@code java -version}
     * @return JavaVersion object
     */
    public static JavaVersion fromJavaVersionOutput(@NotNull List<String> javaVersionOutput) {
        javaVersionOutput.removeIf(line -> line.startsWith("Picked up "));
        List<String> output = Collections.unmodifiableList(javaVersionOutput);

        String shortVersion = output.get(0).split("\"")[1];
        String fullVersion = output.get(1).split("build ")[1].split("\\)")[0];

        Pattern pattern = !fullVersion.startsWith("1.") ? VERSION_PATTERN : PRE_9_VERSION_PATTERN;
        Matcher matcher = pattern.matcher(fullVersion);
        if (!matcher.find()) {
            throw new IllegalArgumentException("Could not parse version: " + fullVersion);
        }
        String majorStr = matcher.group("major");
        String minorStr = matcher.group("minor");

        int major = Integer.parseInt(majorStr);
        int minor = 0;
        if (minorStr != null) {
            try {
                minor = Integer.parseInt(minorStr);
            } catch (NumberFormatException e) {
                // ignore
            }
        }

        String security = matcher.group("security");
        String preRelease = matcher.group("prerelease");

        return new JavaVersion(major, minor, security, preRelease, javaVersionOutput, shortVersion, fullVersion);
    }

    private int compareOldBeforeNew(@NotNull final JavaVersion o) {
        if (major != o.major) {
            return Integer.compare(major, o.major);
        }
        if (minor != o.minor) {
            return Integer.compare(minor, o.minor);
        }
        if (security != null && o.security != null) {
            return security.compareTo(o.security);
        }
        return 0;
    }

    @Override
    public int compareTo(@NotNull final JavaVersion o) {
        return -compareOldBeforeNew(o);
    }

    /**
     * Returns the major version of the class file format of this Java version, e.g. 61 for Java 17.0.1
     *
     * @return major version of the class file format
     */
    public int getClassFileMajorVersion() {
        return major + 44;
    }

    /**
     * Returns the major version, e.g. 17 for Java 17.0.1. Although java versions before 9 were called 1.x, this method will return x instead of 1 for those versions (so e.g. 8 for Java 1.8.0_292)
     *
     * @return major version
     */
    public int getMajor() {
        return this.major;
    }

    /**
     * Returns the minor version, e.g. 0 for Java 17.0.1
     *
     * @return minor version
     */
    public int getMinor() {
        return this.minor;
    }

    /**
     * Returns the security version, e.g. 1 for Java 17.0.1
     *
     * @return security version
     */
    @Nullable
    public String getSecurity() {
        return this.security;
    }

    /**
     * Returns the pre-release version. This method is private because it's unreliable.
     *
     * @return unreliable pre-release version
     */
    private @Nullable String getPreRelease() {
        return this.preRelease;
    }

    /**
     * Returns the complete output of {@code java -version}
     *
     * @return output of {@code java -version}
     */
    public @NotNull List<String> getJavaVersionOutput() {
        return this.javaVersionOutput;
    }

    /**
     * Returns the short version, e.g. 17.0.1 for Java 17.0.1 or 15 for Java 15
     *
     * @return short version
     */
    public @NotNull String getShortVersion() {
        return this.shortVersion;
    }

    /**
     * Returns the full version, e.g. 17.0.1+12-LTS-39
     *
     * @return full version
     */
    public @NotNull String getFullVersion() {
        return this.fullVersion;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof JavaVersion)) return false;
        final JavaVersion other = (JavaVersion) o;
        if (!other.canEqual(this)) return false;
        if (this.getMajor() != other.getMajor()) return false;
        if (this.getMinor() != other.getMinor()) return false;
        final Object this$security = this.getSecurity();
        final Object other$security = other.getSecurity();
        if (this$security == null ? other$security != null : !this$security.equals(other$security)) return false;
        final Object this$preRelease = this.getPreRelease();
        final Object other$preRelease = other.getPreRelease();
        if (this$preRelease == null ? other$preRelease != null : !this$preRelease.equals(other$preRelease))
            return false;
        final Object this$javaVersionOutput = this.getJavaVersionOutput();
        final Object other$javaVersionOutput = other.getJavaVersionOutput();
        if (this$javaVersionOutput == null ? other$javaVersionOutput != null : !this$javaVersionOutput.equals(other$javaVersionOutput))
            return false;
        final Object this$shortVersion = this.getShortVersion();
        final Object other$shortVersion = other.getShortVersion();
        if (this$shortVersion == null ? other$shortVersion != null : !this$shortVersion.equals(other$shortVersion))
            return false;
        final Object this$fullVersion = this.getFullVersion();
        final Object other$fullVersion = other.getFullVersion();
        if (this$fullVersion == null ? other$fullVersion != null : !this$fullVersion.equals(other$fullVersion))
            return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof JavaVersion;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + this.getMajor();
        result = result * PRIME + this.getMinor();
        final Object $security = this.getSecurity();
        result = result * PRIME + ($security == null ? 43 : $security.hashCode());
        final Object $preRelease = this.getPreRelease();
        result = result * PRIME + ($preRelease == null ? 43 : $preRelease.hashCode());
        final Object $javaVersionOutput = this.getJavaVersionOutput();
        result = result * PRIME + ($javaVersionOutput == null ? 43 : $javaVersionOutput.hashCode());
        final Object $shortVersion = this.getShortVersion();
        result = result * PRIME + ($shortVersion == null ? 43 : $shortVersion.hashCode());
        final Object $fullVersion = this.getFullVersion();
        result = result * PRIME + ($fullVersion == null ? 43 : $fullVersion.hashCode());
        return result;
    }

    public String toString() {
        return "JavaVersion(major=" + this.getMajor() + ", minor=" + this.getMinor() + ", security=" + this.getSecurity() + ", preRelease=" + this.getPreRelease() + ", javaVersionOutput=" + this.getJavaVersionOutput() + ", shortVersion=" + this.getShortVersion() + ", fullVersion=" + this.getFullVersion() + ")";
    }
}
