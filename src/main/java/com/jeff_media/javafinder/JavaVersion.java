package com.jeff_media.javafinder;

import lombok.Data;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.sun.xml.internal.ws.util.VersionUtil.compare;

/**
 * Parses the output of java -version according to https://openjdk.org/jeps/223
 */
@Data
public class JavaVersion implements Comparable<JavaVersion> {

    private static final Pattern VERSION_PATTERN = Pattern.compile("(?<major>[0-9]+)([.](?<minor>[0-9]+))?([.](?<security>[0-9]+))?(-(?<prerelease>[a-zA-Z0-9]+))?");
    private static final Pattern PRE_9_VERSION_PATTERN = Pattern.compile("1[.](?<major>[0-9]+)([.](?<minor>[0-9]+))?(_(?<security>[0-9]+)?)?(-(?<prerelease>[a-zA-Z0-9]+))?");

    private final int major;
    private final int minor;
    private final String security;
    private final String preRelease;

    public static JavaVersion fromFullVersionString(String version) {

        //System.out.println(version);

        Pattern pattern = !version.startsWith("1.") ? VERSION_PATTERN : PRE_9_VERSION_PATTERN;
        Matcher matcher = pattern.matcher(version);
        if (!matcher.find()) {
            throw new IllegalArgumentException("Could not parse version: " + version);
        }
        String majorStr = matcher.group("major");
        String minorStr = matcher.group("minor");
        String security = matcher.group("security");
        String preRelease = matcher.group("prerelease");

        int major = Integer.parseInt(majorStr);
        int minor = 0;
        if (minorStr != null) {
            try {
                minor = Integer.parseInt(minorStr);
            } catch (NumberFormatException e) {
                // ignore
            }
        }
        return new JavaVersion(major, minor, security, preRelease);
    }

    @Override
    public int compareTo(final JavaVersion o) {
        if(major != o.major) {
            return Integer.compare(major, o.major);
        }
        if(minor != o.minor) {
            return Integer.compare(minor, o.minor);
        }
        if(security != null && o.security != null) {
            return security.compareTo(o.security);
        }
        return 0;
    }
}
