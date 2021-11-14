package org.teamapps.demolessons.basics.p2_application.l03_servlet;

import org.teamapps.event.Event;

public class ServletNotificationManager {

	public final Event<String> onNotificationPosted = new Event<>();

	public void postNotification(String text) {
		onNotificationPosted.fire(text);
	}

}
