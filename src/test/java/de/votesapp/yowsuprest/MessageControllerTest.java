package de.votesapp.yowsuprest;

import static org.mockito.Mockito.mock;

import org.junit.Test;

public class MessageControllerTest {
	MessageController messageController = new MessageController(mock(YowsupConfig.class));

	@Test
	public void should_delete_with_correct_ids() throws Exception {
		messageController.deleteMessageFromInbox("421331869-115");
		messageController.deleteMessageFromInbox("1433501807-.-354");
	}
}
