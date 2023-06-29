package com.jeff_media.javafinder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

class DirectoryCrawler {

    private final File searchDir;
    private final List<JavaInstallation> installations = new ArrayList<>();
    private final String javaExecutableName;
    private final String javacExecutableName;

    DirectoryCrawler(File searchDir, OperatingSystem os) {
        this.searchDir = searchDir;
        this.javaExecutableName = os.getJavaExecutableName();
        this.javacExecutableName = os.getJavacExecutableName();
    }

    List<JavaInstallation> findInstallations() {
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
                        File javaCExecutable = new File(file, javacExecutableName);
                        boolean isJdk = javaCExecutable.canExecute();
                        if (javaExecutable.canExecute()) {
                            installations.add(new JavaInstallation(searchDir, javaExecutable, isJdk ? javaCExecutable : null, isJdk ? JavaType.JDK : JavaType.JRE));
                        }
                    } else {
                        findInstallations(file);
                    }
                }
            }
        }
    }
}
