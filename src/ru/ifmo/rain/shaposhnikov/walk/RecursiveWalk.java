package ru.ifmo.rain.shaposhnikov.walk;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class RecursiveWalk {
    private final Path inputFile;
    private final Path outputFile;

    RecursiveWalk(final String inputFile, final String outputFile) throws WalkerException {
        this.inputFile = getPath(inputFile, "Invalid path to input file");
        this.outputFile = getPath(outputFile, "Invalid path to output file");

        if (this.outputFile.getParent() != null && Files.notExists(this.outputFile.getParent())) {
            try {
                Files.createDirectories(this.outputFile.getParent());
            } catch (final IOException e) {
                throw new WalkerException("Error during creating directories: " + e.getMessage(), e);
            } catch (final SecurityException e) {
                throw new WalkerException("Creating directories permission denied.", e);
            }
        }
    }

    private Path getPath(final String filePath, final String errorMessage) throws WalkerException {
        try {
            return Paths.get(filePath);
        } catch (final InvalidPathException e) {
            throw new WalkerException(errorMessage + ": " + e.getMessage());
        }
    }

    private void walk() throws WalkerException {
        try (final BufferedReader reader = Files.newBufferedReader(inputFile)) {
            try (final BufferedWriter writer = Files.newBufferedWriter(outputFile)) {
                final FnvRecursiveFileVisitor visitor = new FnvRecursiveFileVisitor(writer);
                try {
                    String filePath;
                    while ((filePath = reader.readLine()) != null) {
                        try {
                            visitor.visitFile(filePath);
                        } catch (final IOException e) {
                            throw new WalkerException("Error during writing to output file.", e);
                        }
                    }
                } catch (final IOException e) {
                    throw new WalkerException("Error during reading input file.", e);
                }
            } catch (final IOException e) {
                throw new WalkerException("Error during opening output file for writing.", e);
            } catch (final SecurityException e) {
                throw new WalkerException("Write permission denied.", e);
            }
        } catch (final IOException e) {
            throw new WalkerException("Error during opening input file for reading.", e);
        } catch (final SecurityException e) {
            throw new WalkerException("Read permission denied.", e);
        }
    }

    public static void main(final String[] args) {
        try {
            if (args == null || args.length < 2 || args[0] == null || args[1] == null) {
                throw new WalkerException("2 arguments expected");
            }
            new RecursiveWalk(args[0], args[1]).walk();
        } catch (final WalkerException e) {
            System.err.println(e.getMessage());
        }
    }
}
