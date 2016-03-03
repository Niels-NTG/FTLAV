package net.ntg.ftl.util;

import java.io.File;
import java.util.TimerTask;

public abstract class FileWatcher extends TimerTask {
	private long timeStamp;
	private final File file;

	public FileWatcher(File file) {
		this.file = file;
		this.timeStamp = file.lastModified();
	}

	public final void run() {
		long watchTimeStamp = file.lastModified();

		if (this.timeStamp != watchTimeStamp) {
			this.timeStamp = watchTimeStamp;
			onChange(file);
		}
	}

	protected abstract void onChange(File file);
}