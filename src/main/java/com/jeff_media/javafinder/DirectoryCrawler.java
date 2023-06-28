package com.jeff_media.javafinder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DirectoryCrawler {

    private final File searchDir;
    private final List<JavaInstallation> installations = new ArrayList<>();
    private final String javaExecutableName;

    public DirectoryCrawler(File searchDir, String javaExecutableName) {
        this.searchDir = searchDir;
        this.javaExecutableName = javaExecutableName;
    }

    public List<JavaInstallation> findInstallations() {
        findInstallations(searchDir);
        return installations;
    }

    private void findInstallations(File searchDir) {
        //System.out.println("Searching " + searchDir.getAbsolutePath());
        File[] children = searchDir.listFiles();
        if (children != null) {
            for (File file : children) {
                if (file.isDirectory()) {
                    if (file.getName().equals("bin")) {
                        File javaExecutable = new File(file, javaExecutableName);
                        if (javaExecutable.canExecute()) {
                            installations.add(new JavaInstallation(searchDir, javaExecutable));
                        }
                    } else {
                        findInstallations(file);
                    }
                }
            }
        }
    }
}
