package io.github.monjhall.glowme;

public class SettingValueVerification extends DataVerification{

	// Class variables.
	private String verifiedSettingValue;
	
	// Constructor verifies the GlowSetting quality.
	public SettingValueVerification(Setting setting, String settingValue) {
		
		// Depending on the glowSetting, verify the data type.
		if (setting == Setting.ACTION) {
			
			// Verify the value is a Setting.
			try {
				verifiedSettingValue = Setting.valueOf(settingValue.toUpperCase()).toString();
			} catch (IllegalArgumentException e) {
				verifiedSettingValue = "Invalid Setting";
				isValid = false;
				errorMessage = "Setting must be one of the known settings.";
				return;
			}
		} else if (setting == Setting.COLOR) {
			
			// Verify the value is a valid color.
			ColorVerification colorVerification = new ColorVerification(settingValue);
			
			// Set the in values to that of the colorVerification and return.
			verifiedSettingValue = colorVerification.getVerifiedColor();
			isValid = colorVerification.getIsValid();
			errorMessage = colorVerification.getErrorMessage();
			return;
		} else if (setting == Setting.DURATION) {
			
			// Verify the value is a valid duration.
			DurationVerification durationVerification = new DurationVerification(settingValue);
			
			// Set the in values to that of the durationVerification and return.
			verifiedSettingValue = Integer.toString(durationVerification.getVerifiedDuration());
			isValid = durationVerification.getIsValid();
			errorMessage = durationVerification.getErrorMessage();
			return;
		} else if (setting == Setting.MESSAGE) {		
			// TODO: Verify the message somehow... Maybe not..
			verifiedSettingValue = settingValue;
			isValid = true;
			errorMessage = "No errors occurred when verifying SettingValue.";
			return;
		}
	}

	// Getter for verifiedSetting.
	public String getverifiedSettingValue() {
		return this.verifiedSettingValue;
	}
}
