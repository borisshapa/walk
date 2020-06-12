package ru.ifmo.rain.shaposhnikov.walk;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

import static java.nio.file.FileVisitResult.CONTINUE;

/**
 * Extends the {@link SimpleFileVisitor} class.
 * When crawling, it considers the <a href="https://ru.wikipedia.org/wiki/FNV">FNV</a> hash amount from the files.
 *
 * @author Boris Shaposhnikov
 */
public class FnvRecursiveFileVisitor extends SimpleFileVisitor<Path> {
    private final BufferedWriter writer;
    private static final int FNV_32_PRIME = 0x01000193;
    private static final int BUFFER_SIZE = 1024;

    /**
     * Creates a class with the specified {@link BufferedWriter} to output the result.
     *
     * @param writer where to write the result
     */
    public FnvRecursiveFileVisitor(final BufferedWriter writer) {
        this.writer = writer;
    }

    private FileVisitResult write(final int hash, final String path) throws IOException {
        writer.write(String.format("%08x %s%n", hash, path));
        return CONTINUE;
    }

    /**
     * Traverses the file tree with this {@link SimpleFileVisitor}.
     * In case of an error on a specific file, it records <var>00000000</var> as its hash sum.
     *
     * @param path path to the starting file.
     * @throws IOException if an error occurred while crawling files or while writing to the {@link BufferedWriter}.
     * @see Files#walkFileTree(Path, FileVisitor)
     */
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
