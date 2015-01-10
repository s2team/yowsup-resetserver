package de.votesapp.yowsuprest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import lombok.Data;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

@RestController
@RequestMapping("/messages")
public class MessageController {
	ObjectMapper om = new ObjectMapper();

	@RequestMapping(value = "/inbox", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ArrayNode readAllMessagesFromInbox() throws JsonProcessingException, IOException {
		return readPath("/tmp/yowsup/inbox");
	}

	@RequestMapping(value = "/inbox/{id}", method = RequestMethod.DELETE)
	public Map<String, Boolean> deleteMessageFromInbox(@PathVariable final String id) throws IOException {
		if (id.matches("[0-9]+-[0-9]+")) {
			final Map<String, Boolean> sucess = new HashMap<>();
			sucess.put("success", Files.deleteIfExists(Paths.get("/tmp/yowsup/inbox", id + ".jsonpickle")));
			return sucess;
		} else {
			throw new IllegalArgumentException("Thats not an id!");
		}
	}

	@RequestMapping(value = "/outbox", method = RequestMethod.POST)
	public void sendMessage(@RequestBody final NewMessage message) throws IOException {
		om.writeValue(Paths.get("/tmp/yowsup/outbox/", System.currentTimeMillis() + ".json").toFile(), message);
	}

	@RequestMapping(value = "/outbox", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ArrayNode readAllMessagesFromOutbox() throws JsonProcessingException, IOException {
		return readPath("/tmp/yowsup/outbox");
	}

	@RequestMapping(value = "/outbox", method = RequestMethod.DELETE)
	public Map<String, Boolean> deleteMessageFromOutbox(@PathVariable final String id) throws IOException {
		if (id.matches("[0-9]+-[0-9]+")) {
			final Map<String, Boolean> sucess = new HashMap<>();
			sucess.put("success", Files.deleteIfExists(Paths.get("/tmp/yowsup/inbox", id + ".jsonpickle")));
			return sucess;
		} else {
			throw new IllegalArgumentException("Thats not an id!");
		}
	}

	private ArrayNode readPath(final String path) throws IOException {
		final ArrayNode arrayNode = JsonNodeFactory.instance.arrayNode();

		Files.walk(Paths.get(path)).forEach(filePath -> {
			if (Files.isRegularFile(filePath)) {
				JsonNode jsonNode;
				try {
					jsonNode = om.readTree(filePath.toFile());
					arrayNode.add(jsonNode);
				} catch (final Exception e) {
					e.printStackTrace();
				}
			}
		});
		return arrayNode;
	}

	@Data
	public static class NewMessage {
		String to;
		String text;
	}
}
