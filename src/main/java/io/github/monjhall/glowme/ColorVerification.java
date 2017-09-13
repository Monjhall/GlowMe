package io.github.monjhall.glowme;

import org.bukkit.ChatColor;

public class ColorVerification extends DataVerification {

	// Class variables.
	private String verifiedColor;

	// Constructor verifies the duration quality.
	public ColorVerification(String colorString) {

		// Verify the color is a ChatColor.
		try {
			ChatColor.valueOf(colorString.toUpperCase());
		} catch (IllegalArgumentException e) {
			verifiedColor = "Invalid Color";
			isValid = false;
			errorMessage = "Color must be one of the allowed scoreboard colors.";
			return;
		}

		// Verify the color is not a format.
		if (ChatColor.valueOf(colorString.toUpperCase()).isFormat()) {
			verifiedColor = "Invalid Color";
			isValid = false;
			errorMessage = "Color must be a color, not a format code.";
			return;
		}

		// Apply to proper values if it has passed all tests.
		verifiedColor = colorString.toUpperCase();
		isValid = true;
		errorMessage = "No errors occurred when verifying color.";
	}

	// Getter for verifiedDuration.
	public String getVerifiedColor() {
		return this.verifiedColor;
	}
}
