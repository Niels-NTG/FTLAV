package nl.nielspoldervaart.ftlav.data;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.ClosedWatchServiceException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@Slf4j
public class FileWatcher implements Runnable {

	public final File targetFile;
	private final Consumer<File> consumer;
	private final Thread thread;
	private final Debouncer debouncer;

	public FileWatcher(File targetFile, Consumer<File> consumer) throws FileNotFoundException {
		if (targetFile == null || !targetFile.exists()) {
			throw new FileNotFoundException("No file was submitted to the file watcher");
		}
		this.targetFile = targetFile;
		this.consumer = consumer;
		this.thread = new Thread(this);
		log.info("Starting watching file {} for changes", targetFile.getAbsolutePath());
		debouncer = new Debouncer();
	}

	public void watch() {
		thread.setDaemon(true);
		thread.start();
	}

	public void unwatch() {
		log.info("Stopping watching file {} for changes", targetFile.getAbsolutePath());
		try {
			debouncer.shutdown();
			thread.interrupt();
		} catch (SecurityException ignored) {}
	}

	@Override
	protected void finalize() {
		try {
			debouncer.shutdown();
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
							debouncer.debounce(
								this.getClass(),
								() -> consumer.accept(targetFile),
								200,
								TimeUnit.MILLISECONDS
							);
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
