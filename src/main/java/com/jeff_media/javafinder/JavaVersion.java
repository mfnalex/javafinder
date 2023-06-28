package com.jeff_media.javafinder;

import lombok.Data;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parses the output of java -version according to https://openjdk.org/jeps/223
 */
@Data
public class JavaVersionParser {

    private static final Pattern VERSION_PATTERN = Pattern.compile("(?<MAJOR>[0-9]+)([.](?<MINOR>[0-9]+))?([.](?<SECURITY>[0-9]+))?(-(?<PRE>[a-zA-Z0-9]+))?");
    private static final Pattern PRE_9_VERSION_PATTERN = Pattern.compile("1[.](?<MAJOR>[0-9]+)([.](?<MINOR>[0-9]+))?(_(?<SECURITY>[0-9]+)?)?(-(?<PRE>[a-zA-Z0-9]+))?");

    private final String majorVersion;
    private final String minorVersion;
    private final String securityVersion;
    private final String preReleaseVersion;

    public JavaVersionParser(String version) {

        Pattern pattern = !version.startsWith("1.") ? VERSION_PATTERN : PRE_9_VERSION_PATTERN;
        Matcher matcher = pattern.matcher(version);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid version: " + version);
        }
        majorVersion = matcher.group("MAJOR");
        minorVersion = matcher.group("MINOR");
        securityVersion = matcher.group("SECURITY");
        preReleaseVersion = matcher.group("PRE");

        doSth("asd");
        doSth((CharSequence) "asd");

    }

    public void doSth(String asd) {

    }

    public void doSth(CharSequence asd) {

    }


}
