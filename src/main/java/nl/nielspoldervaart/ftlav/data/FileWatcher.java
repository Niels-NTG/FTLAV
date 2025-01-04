package nl.nielspoldervaart.ftlav.data;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.*;
import java.util.function.Consumer;

@Slf4j
public class FileWatcher implements Runnable {

	public final File targetFile;
	private final Consumer<File> consumer;
	private final Thread thread;

	public FileWatcher(File targetFile, Consumer<File> consumer) throws FileNotFoundException {
		if (targetFile == null || !targetFile.exists()) {
			throw new FileNotFoundException("No file was submitted to the file watcher");
		}
		this.targetFile = targetFile;
		this.consumer = consumer;
		this.thread = new Thread(this);
		log.info("Starting watching file {} for changes", targetFile.getAbsolutePath());
	}

	public void watch() {
		thread.setDaemon(true);
		thread.start();
	}

	public void unwatch() {
		log.info("Stopping watching file {} for changes", targetFile.getAbsolutePath());
		try {
			thread.interrupt();
		} catch (SecurityException ignored) {}
	}

	@Override
	protected void finalize() {
		try {
			thread.interrupt();
		} catch (SecurityException ignored) {}
	}

	@Override
	public void run() {
		try (WatchService watchService = FileSystems.getDefault().newWatchService()) {
			Path targetPath = targetFile.toPath();
			targetPath.getParent().register(
				watchService,
				StandardWatchEventKinds.ENTRY_CREATE
			);
			boolean poll = true;
			while (poll) {
				WatchKey key = watchService.take();
				for (WatchEvent<?> event : key.pollEvents()) {
					if (event.kind() == StandardWatchEventKinds.ENTRY_CREATE) {
						Path changedFilePath = (Path) event.context();
						if (targetPath.getFileName().equals(changedFilePath)) {
							consumer.accept(targetFile);
						}
					}
				}
				poll = key.reset();
			}
		} catch (IOException | InterruptedException | ClosedWatchServiceException e) {
			Thread.currentThread().interrupt();
		}
	}

}
