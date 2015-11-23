package fr.toutatice.outils.ldap.dao;

import java.util.ArrayList;
import java.util.List;

public class Helper {
	
	
	
	private Helper() {
		super();
	}

	public static List<String> supprimerDoublonsCaseNonSensitive(List<String> liste) {
		
		List<String> listeRetour = new ArrayList<String>();
		List<String> listeMinuscule = new ArrayList<String>();
		
		for(String s : liste) {
			listeMinuscule.add(s.toLowerCase());
		}
		
		for(String s:liste) {
			int i=0;
			for(String smin : listeMinuscule) {
				if(s.toLowerCase().equals(smin)) {
					i++;
				}
			}
			if (i==1) {
				listeRetour.add(s);
			} else {
				boolean trouve = false;
				for(String sret : listeRetour){
					if(sret.toLowerCase().equals(s.toLowerCase())){
						trouve = true;
					}
				}
				if(!trouve){
					listeRetour.add(s);
				}
			}
		}
		
		return listeRetour;
	}

}
