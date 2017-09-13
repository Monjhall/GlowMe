package io.github.monjhall.glowme;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

public class EventCauseVerification extends DataVerification {

	// Class variables.
	private String verifiedEventCause;

	// Constructor verifies the event cause.
	public EventCauseVerification(EventType eventType, String eventCause) {

		// Verify depending on the eventType.
		if (eventType == EventType.ITEMCRAFT || eventType == EventType.ITEMCONSUME
				|| eventType == EventType.ITEMPICKUP) {

			// Verify the argument is a material.
			try {
				verifiedEventCause = Material.matchMaterial(eventCause).toString();
			} catch (NullPointerException e) {
				verifiedEventCause = "Invalid Event Cause";
				errorMessage = "The material you're trying to add doesn't exist! Check the list of valid materials.";
				isValid = false;
				return;
			}
		} else if (eventType == EventType.TELEPORT) {

			// Verify the argument is a teleport cause.
			try {
				verifiedEventCause = TeleportCause.valueOf(eventCause).toString();
			} catch (IllegalArgumentException e) {
				verifiedEventCause = "Invalid Event Cause";
				errorMessage = "The teleport cause you're trying to add doesn't exist! Check the list of valid teleport causes.";
				isValid = false;
				return;
			}
		} else if (eventType == EventType.ENTITYKILLED) {

			// Verify the argument is a teleport cause.
			try {
				verifiedEventCause = EntityType.valueOf(eventCause).toString();
			} catch (IllegalArgumentException e) {
				verifiedEventCause = "Invalid Event Cause";
				errorMessage = "The entity you're trying to add doesn't exist! Check the list of valid entities.";
				isValid = false;
				return;
			}
		}
		
		// If the eventCause is verified, return accurate data.
		errorMessage = "No errors occurred when verifying duration.";
		isValid = true;
		return;
	}

	// Getter for verifiedEventCause.
	public String getVerifiedEventCause() {
		return this.verifiedEventCause;
	}
}
