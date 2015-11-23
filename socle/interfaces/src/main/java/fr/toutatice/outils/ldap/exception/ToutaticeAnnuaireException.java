package fr.toutatice.outils.ldap.exception;

public class ToutaticeAnnuaireException extends Exception {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ToutaticeAnnuaireException(Exception e) {
		super(e);
	}
	
	public ToutaticeAnnuaireException(String message) {
		super(message);
	}

}
