package de.votesapp.yowsuprest;

import java.io.IOException;
import java.text.MessageFormat;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class YowsupProcessSupervisor {

	private final String binaryPathWithParameters;

	@Autowired
	public YowsupProcessSupervisor(final YowsupConfig cfg) {
		binaryPathWithParameters = MessageFormat.format("{0} {1} demos -l {2}:{3} --filer {4} {5} {6}", //
				cfg.getPythonBin(), //
				cfg.getYowsupCliBin(), //
				cfg.getPhone(), //
				cfg.getWhatsAppBase64Password(), //
				cfg.getInboxPath(), //
				cfg.getOutboxPath(), //
				cfg.getSentPath());
	}

	// Enable this if Java should manage the python process
	// @PostConstruct
	public void startNewMonitor() {
		new Thread(new Monitor(binaryPathWithParameters)).start();
	}

	@RequiredArgsConstructor
	private static class Monitor implements Runnable {
		private final String binaryPathWithParameters;

		@Override
		public void run() {
			boolean run = true;
			while (run) {
				Process p = null;
				try {
					log.info("Starting Yowsup: " + binaryPathWithParameters);
					p = Runtime.getRuntime().exec(binaryPathWithParameters);
					new ProcessLoggerThread(p.getInputStream(), "StdOut", (line, log) -> log.info(line)).start();
					new ProcessLoggerThread(p.getErrorStream(), "StdErr", (line, log) -> log.error(line)).start();
					p.waitFor();
				} catch (final IOException e) {
					log.error("Yowsup died", e);
					try {
						log.info("Wait 60 seconds before restarting");
						Thread.sleep(60_000);
					} catch (final InterruptedException e1) {
					}
				} catch (final InterruptedException e) {
					log.info("Monitor got interrupted");
					if (p != null) {
						p.destroyForcibly();
					}
					run = false;
				} finally {
					if (p != null) {
						p.destroyForcibly();
					}
				}
				if (p != null) {
					final int exitVal = p.exitValue();
					log.info("Yowsup ends with: {}", exitVal);
				}
			}
		}
	}
}
