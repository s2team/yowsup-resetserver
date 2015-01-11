package de.votesapp.yowsuprest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.function.BiConsumer;

import lombok.extern.slf4j.Slf4j;

import org.slf4j.Logger;

@Slf4j
public class ProcessLoggerThread extends Thread {

	private final InputStream inputStream;
	private final BiConsumer<String, Logger> doLog;
	private final String name;

	public ProcessLoggerThread(final InputStream inputStream, final String name, final BiConsumer<String, Logger> log) {
		super();
		this.inputStream = inputStream;
		this.name = name;
		this.doLog = log;
	}

	@Override
	public void run() {
		try {
			log.info("Logger {} Starts", name);
			final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
			String line = reader.readLine();
			while (line != null) {
				doLog.accept(line, log);
				line = reader.readLine();
			}
			reader.close();
			log.info("Logger {} Ends", name);
		} catch (final IOException e) {
			log.error("The log reader died unexpectedly.");
		}
	}
}
