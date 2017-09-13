package io.github.monjhall.glowme;

public abstract class DataVerification {

	// Class variables.
	protected boolean isValid;
	protected String errorMessage;
	
	// Getter for isValid.
	public boolean getIsValid() {
		return this.isValid;
	}
	
	// Getter for errorMessage.
	public String getErrorMessage() {
		return this.errorMessage;
	}
	
}
