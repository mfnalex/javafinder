package com.jeff_media.javafinder;

import java.util.List;

class Main {

    public static void main(String[] args) {
        JavaFinder finder = JavaFinder.builder().build();
        List<JavaInstallation> installations = finder.findInstallationsAsync().join();

        for (JavaInstallation java : installations) {
            print(java);
        }
    }

    private static void print(JavaInstallation java) {
        System.out.println((java.isCurrentJavaVersion() ? "* " : "  ") + java.getType() + " " + java.getVersion().getMajor() + " (" + java.getVersion().getFullVersion() + ") at " + java.getHomeDirectory().getAbsolutePath());
    }
}
