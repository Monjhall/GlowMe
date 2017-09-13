package io.github.monjhall.glowme;

public class DurationVerification extends DataVerification {

	// Class variables.
	private int verifiedDuration;

	// Constructor verifies the duration quality.
	public DurationVerification(String durationString) {

		// Verify the provided duration is an integer.
		try {
			verifiedDuration = Integer.parseInt(durationString);
		} catch (NumberFormatException e) {
			verifiedDuration = -2;
			isValid = false;
			errorMessage = "Duration must be an integer between -1 and 1500, proper usage below.";
			return;
		}

		// Verify the duration is one of the allowed durations.
		if (this.verifiedDuration < -1) {
			verifiedDuration = -2;
			isValid = false;
			errorMessage = "Duration must be an integer between -1 and 1500, proper usage below.";
		} else if (this.verifiedDuration > 1500) {
			verifiedDuration = -2;
			isValid = false;
			errorMessage = "Duration must be a value between 1 and 1500 or a value of -1."
					+ "\nIf you are trying to apply infinite glow, use a duration of -1.";
		} else {
			isValid = true;
			errorMessage = "No errors occurred when verifying duration.";
		}
		
		// Set verifiedDuration to a large number if -1 is found.
		if (this.verifiedDuration == -1) {
			this.verifiedDuration = 1000000;
		}
	}
	
	// Getter for verifiedDuration.
	public int getVerifiedDuration() {
		return this.verifiedDuration;
	}
}
