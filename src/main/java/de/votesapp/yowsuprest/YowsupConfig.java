package de.votesapp.yowsuprest;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "yowsup")
public class YowsupConfig {
	private String pythonBin;
	private String yowsupCliBin;
	private String phone;
	private String whatsAppBase64Password;
	private String inboxPath;
	private String outboxPath;
	private String sentPath;
}
