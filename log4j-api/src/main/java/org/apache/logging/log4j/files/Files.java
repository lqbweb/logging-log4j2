package org.apache.logging.log4j.files;

import org.apache.commons.io.DirectoryWalker;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

/**
 * Created by Rubén Pérez on 17/08/2016.
 */
public class Files {
	private static abstract class FilesDirectoryWalker extends DirectoryWalker<File> {
		public FilesDirectoryWalker(int maxDepth) {
			super(null, maxDepth);
		}
		public void go(File directory) throws IOException {
            super.walk(directory, new ArrayList<File>());
        }
	}
	
	public static void walkFileTree(final Path start, Set<FileVisitOption> options, int maxDepth, final FileVisitor<Path> visitor) throws IOException {
		final boolean followSyms = options.contains(FileVisitOption.FOLLOW_LINKS);
		FilesDirectoryWalker res = new FilesDirectoryWalker(maxDepth) {
			@Override
			protected boolean handleDirectory(final File directory, final int depth, final Collection<File> results) throws IOException {
				if(!followSyms) {
					if(!FileUtils.isSymlink(directory)) {
						visitor.preVisitDirectory(new Path(directory), new BasicFileAttributesImpl(directory));
						return true;
					}
					return false;
				} else {
					visitor.preVisitDirectory(new Path(directory), new BasicFileAttributesImpl(directory));
					return true;
				}
			}
			
			@Override
			protected void handleFile(final File file, final int depth, final Collection<File> results) throws IOException {
				if(!followSyms) {
					if(!FileUtils.isSymlink(file)) {
						visitor.visitFile(new Path(file), new BasicFileAttributesImpl(file));
					}
				} else {
					visitor.visitFile(new Path(file),  new BasicFileAttributesImpl(file));
				}
			}
			
		    protected void handleDirectoryEnd(final File directory, final int depth, final Collection<File> results) throws IOException {
		    	visitor.postVisitDirectory(new Path(directory), null);
		    }
		};
		res.go(start.toFile());
	}

	public static void deleteIfExists(Path path) {
		if(path.toFile().exists()) {
			FileUtils.deleteQuietly(path.toFile());
		}
	}

	public static void move(Path source, Path dst) throws IOException {
		File srcFile = source.toFile();
		File toFile = dst.toFile();

		if(srcFile.exists()) {
			if(srcFile.isFile()) {
				FileUtils.moveFile(srcFile, toFile);
			} else {
				FileUtils.moveDirectory(srcFile, toFile);
			}
		}

	}

	public static void copy(Path source, Path dst) throws IOException {
		File srcFile = source.toFile();
		File toFile = dst.toFile();

		if(srcFile.exists()) {
			if(srcFile.isFile()) {
				FileUtils.copyFile(srcFile, toFile);
			} else {
				FileUtils.copyDirectory(srcFile, toFile);
			}
		}
	}

	public static void copy(Path source, Path dst, boolean replaceExisting) throws IOException {
		File toFile = dst.toFile();

		if(toFile.exists()) {
			if(replaceExisting) {
				FileUtils.forceDelete(toFile);
			} else {
				return;
			}
		}
		copy(source, dst);
	}

	public static void copy(Path source, OutputStream dst) throws IOException {
		FileUtils.copyFile(source.toFile(), dst);
	}


	public static void delete(Path source) throws IOException {
		FileUtils.forceDelete(source.toFile());
	}
}
