package com.jeff_media.javafinder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DirectoryCrawler {

    private final File searchDir;
    private final List<JavaInstallation> installations = new ArrayList<>();
    private final String javaExecutableName;
    private final String javacExecutableName;

    public DirectoryCrawler(File searchDir, OperatingSystem os) {
        this.searchDir = searchDir;
        this.javaExecutableName = os.getJavaExecutableName();
        this.javacExecutableName = os.getJavacExecutableName();
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
                        boolean isJdk = new File(file, javacExecutableName).canExecute();
                        if (javaExecutable.canExecute()) {
                            installations.add(new JavaInstallation(searchDir, javaExecutable, isJdk ? JavaType.JDK : JavaType.JRE));
                        }
                    } else {
                        findInstallations(file);
                    }
                }
            }
        }
    }
}
