package com.jeff_media.javafinder;

import lombok.Data;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parses the output of java -version according to https://openjdk.org/jeps/223
 */
@Data
public class JavaVersion {

    private static final Pattern VERSION_PATTERN = Pattern.compile("(?<major>[0-9]+)([.](?<minor>[0-9]+))?([.](?<security>[0-9]+))?(-(?<prerelease>[a-zA-Z0-9]+))?");
    private static final Pattern PRE_9_VERSION_PATTERN = Pattern.compile("1[.](?<major>[0-9]+)([.](?<minor>[0-9]+))?(_(?<security>[0-9]+)?)?(-(?<prerelease>[a-zA-Z0-9]+))?");

    private final String major;
    private final String minor;
    private final String security;
    private final String preRelease;

    public static JavaVersion fromFullVersionString(String version) {

        //System.out.println(version);

        Pattern pattern = !version.startsWith("1.") ? VERSION_PATTERN : PRE_9_VERSION_PATTERN;
        Matcher matcher = pattern.matcher(version);
        if (!matcher.find()) {
            throw new IllegalArgumentException("Could not parse version: " + version);
        }
        String major = matcher.group("major");
        String minor = matcher.group("minor");
        String security = matcher.group("security");
        String preRelease = matcher.group("prerelease");

        return new JavaVersion(major, minor, security, preRelease);
    }


}
