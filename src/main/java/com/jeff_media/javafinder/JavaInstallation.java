package com.jeff_media.javafinder;

import lombok.Getter;
import lombok.ToString;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
public class JavaInstallation implements Comparable<JavaInstallation> {

    private final File homeDirectory;
    private final File executable;
    private final List<String> completeVersionOutput;
    private final String shortVersion;
    private final String fullVersion;
    private final JavaVersion javaVersion;

    public JavaInstallation(File homeDirectory, File executable) {
        this.homeDirectory = homeDirectory;
        this.executable = executable;
        this.completeVersionOutput = captureCompleteVersionOutput();
        shortVersion = completeVersionOutput.get(0).split("\"")[1];
        fullVersion = completeVersionOutput.get(1).split("\\(build ")[1].split("\\)")[0];
        javaVersion = JavaVersion.fromFullVersionString(fullVersion);
    }

    private List<String> captureCompleteVersionOutput() {
        ProcessBuilder builder = new ProcessBuilder(executable.getAbsolutePath(), "-version");
        List<String> completeVersionOutput = new ArrayList<>();
        try {
            Process process = builder.start();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    completeVersionOutput.add(line);
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return completeVersionOutput;
    }

    @Override
    public int compareTo(final JavaInstallation o) {
        return javaVersion.compareTo(o.javaVersion);
    }
}
