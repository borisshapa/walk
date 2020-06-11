package ru.ifmo.rain.shaposhnikov.walk;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

import static java.nio.file.FileVisitResult.CONTINUE;

public class FnvRecursiveFileVisitor extends SimpleFileVisitor<Path> {
    BufferedWriter writer;
    private static final int FNV_32_PRIME = 0x01000193;
    private static final int BUFFER_SIZE = 1024;

    public FnvRecursiveFileVisitor(final BufferedWriter writer) {
        this.writer = writer;
    }

    private FileVisitResult write(final int hash, final String path) throws IOException {
        writer.write(String.format("%08x %s%n", hash, path));
        return CONTINUE;
    }

    public void visitFile(final String path) throws IOException {
        try {
            Files.walkFileTree(Paths.get(path), this);
        } catch (final InvalidPathException e) {
            write(0, path);
        }
    }

    @Override
    public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
        int hash = 0x811c9dc5;
        try (final InputStream reader = Files.newInputStream(file)) {
            final byte[] buffer = new byte[BUFFER_SIZE];
            int count;
            while ((count = reader.read(buffer)) != -1) {
                for (int i = 0; i < count; i++) {
                    hash *= FNV_32_PRIME;
                    hash ^= buffer[i] & 255;
                }
            }
        } catch (final IOException e) {
            hash = 0;
        }
        return write(hash, file.toString());
    }

    @Override
    public FileVisitResult visitFileFailed(final Path file, final IOException exc) throws IOException {
        return write(0, file.toString());
    }
}
