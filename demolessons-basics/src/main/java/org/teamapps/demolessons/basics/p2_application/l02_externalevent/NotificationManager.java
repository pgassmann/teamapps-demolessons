package org.teamapps.demolessons.basics.p2_application.l02_externalevent;

import org.teamapps.event.Event;

public class NotificationManager {

	public final Event<String> onNotificationPosted = new Event<>();

	public void postNotification(String text) {
		onNotificationPosted.fire(text);
	}

}
