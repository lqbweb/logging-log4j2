package org.apache.logging.log4j.files;

import java.io.File;
import java.util.concurrent.TimeUnit;

public class BasicFileAttributesImpl implements BasicFileAttributes{
	private final File file;
	
	public static class FileKey {
		private File file;
		public FileKey(File file) {
			this.file=file;
		}
		
		@Override
		public int hashCode() {
			long res = file.length();
			res+=file.lastModified();
			if(file.isHidden())
				res+=2;
			if(file.isDirectory()) {
				res+=3;
			}
			return (int) res;
		}
		
		@Override
		public boolean equals(Object dst) {
			if(dst instanceof FileKey) {
				int dstHashCode = ((FileKey) dst).hashCode();
				return dstHashCode == this.hashCode();
			}
			return false;
		}
	}
	
	public BasicFileAttributesImpl(File f) {
		this.file=f;
	}
	
	@Override
	public FileTime lastModifiedTime() {
		long time = this.file.lastModified();
		return FileTime.from(time, TimeUnit.MILLISECONDS);
	}

	@Override
	public boolean isDirectory() {
		return file.isDirectory();
	}

	@Override
	public long size() {
		return this.file.length();
	}

	@Override
	public Object fileKey() {
		return new FileKey(this.file);
	}

}
