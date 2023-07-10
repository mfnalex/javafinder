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

import java.io.File;
import java.io.IOException;
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
                            try {
                                installations.add(new JavaInstallation(searchDir, javaExecutable, isJdk ? javaCExecutable : null, isJdk ? JavaType.JDK : JavaType.JRE));
                            } catch (IOException ignored) {
                                // ignored.printStackTrace();
                            }
                        }
                    } else {
                        findInstallations(file);
                    }
                }
            }
        }
    }
}
