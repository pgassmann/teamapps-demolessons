package org.teamapps.demo.lessons.l15_externalevent;

import org.teamapps.event.Event;

public class NotificationManager {

	public final Event<String> onNotificationPosted = new Event<>();

	public void postNotification(String text) {
		onNotificationPosted.fire(text);
	}

}
