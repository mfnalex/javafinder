package com.jeff_media.javafinder;

import java.io.File;
import java.io.FileFilter;

public class ProgramFilesFileFilter implements FileFilter {

    public static final ProgramFilesFileFilter INSTANCE = new ProgramFilesFileFilter();

    @Override
    public boolean accept(File path) {
        String name = path.getName().toLowerCase();
        return name.contains("java") || name.contains("jdk") || name.contains("jre");
    }
}
