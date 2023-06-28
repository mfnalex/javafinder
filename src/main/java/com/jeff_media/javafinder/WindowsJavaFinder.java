package com.jeff_media.javafinder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class WindowsJavaFinder implements JavaFinder {

    private final File programFilesDirectory;

    public WindowsJavaFinder() {
        programFilesDirectory = new File(System.getenv("ProgramFiles"));
    }

    @Override
    public List<JavaInstallation> findInstallations() {

        List<JavaInstallation> installations = new ArrayList<>();

        File javaDir = new File(programFilesDirectory, "Java");
        for(File file : javaDir.listFiles()) {
            if(!file.isDirectory()) continue;
            File binDir = new File(file, "bin");
            if(!binDir.isDirectory()) continue;
            File javaExe = new File(binDir, "java.exe");
            if(!javaExe.canExecute()) continue;

            installations.add(new JavaInstallation(file, javaExe));
        }

        return installations;
    }
}
