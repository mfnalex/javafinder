package com.jeff_media.javafinder;

import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {
        JavaFinder finder = new WindowsJavaFinder();
        finder.findInstallations().forEach(installation -> {
//            System.out.println(installation.getHomeDirectory());
//            System.out.println(installation.getVersion());

            System.out.println("\n\nJava Home: " + installation.getHomeDirectory());
            System.out.println("Java Executable: " + installation.getExecutable());
            System.out.println("Java Version: " + installation.getShortVersion() + " (" + installation.getFullVersion() + ")");
            System.out.println(JavaVersion.fromFullVersionString(installation.getFullVersion()));
            //System.out.println("    " + String.join("\n    ", installation.getCompleteVersionOutput()));

        });
    }
}
