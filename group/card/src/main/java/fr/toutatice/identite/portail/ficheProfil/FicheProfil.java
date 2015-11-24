/*
 * (C) Copyright 2014 Académie de Rennes (http://www.ac-rennes.fr/) and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-2.1.html
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 *
 * Contributors:
 *  aguihomat
 * 
 *    
 */
package fr.toutatice.identite.portail.ficheProfil;

import java.util.ArrayList;
import java.util.List;


import fr.toutatice.outils.ldap.entity.Profil;
import fr.toutatice.outils.ldap.entity.Person;

public class FicheProfil {
	
	private Profil profil;
	
	private List<PersonStatut> listeMembresTries = new ArrayList<PersonStatut>();
	private List<PersonStatut> listeMembresExplicitTries = new ArrayList<PersonStatut>();
	private List<ManagerStatut> listeManagersTries = new ArrayList<ManagerStatut>();
	private List<String> listeManagersAjout = new ArrayList<String>();
	private List<Person> listeMembresRecherche = new ArrayList<Person>();
	
	private String filtreMembre="";
	private String filtreManager="";
	private String filtreRne="";
	private String filtreAjoutGroupe="";
	private String typeManager="person";
	private String macroProfil="";
	
	List<Profil> groupesOrgaLiee = new ArrayList<Profil>();
	
	//private int nbMembresImplicitesProfil;
	//private int nbMembresExplicitesProfil;
	private boolean chgtMembres;
	
	private boolean modified=false;
	
	
	public Profil getProfil() {
		return profil;
	}
	public void setProfil(Profil profil) {
		this.profil = profil;
	}
	
	
	public String getFiltreMembre() {
		return filtreMembre;
	}
	public void setFiltreMembre(String filtreMembre) {
		this.filtreMembre = filtreMembre;
	}
	public List<String> getListeManagersAjout() {
		return listeManagersAjout;
	}
	public void setListeManagersAjout(List<String> listeManagersAjout) {
		this.listeManagersAjout = listeManagersAjout;
	}
	public String getFiltreManager() {
		return filtreManager;
	}
	public void setFiltreManager(String filtreManager) {
		this.filtreManager = filtreManager;
	}
	public String getFiltreAjoutGroupe() {
		return filtreAjoutGroupe;
	}
	public void setFiltreAjoutGroupe(String filtreAjoutGroupe) {
		this.filtreAjoutGroupe = filtreAjoutGroupe;
	}
	public List<Person> getListeMembresRecherche() {
		return listeMembresRecherche;
	}
	public void setListeMembresRecherche(List<Person> listeMembresRecherche) {
		this.listeMembresRecherche = listeMembresRecherche;
	}
	public String getFiltreRne() {
		return filtreRne;
	}
	public void setFiltreRne(String filtreRne) {
		this.filtreRne = filtreRne;
	}
	public String getTypeManager() {
		return typeManager;
	}
	public void setTypeManager(String typeManager) {
		this.typeManager = typeManager;
	}
	
	public String getMacroProfil() {
		return macroProfil;
	}
	public void setMacroProfil(String macroProfil) {
		this.macroProfil = macroProfil;
	}
/*	public int getNbMembresImplicitesProfil() {
		return nbMembresImplicitesProfil;
	}
	public void setNbMembresImplicitesProfil(int nbMembresImplicitesProfil) {
		this.nbMembresImplicitesProfil = nbMembresImplicitesProfil;
	}
	public int getNbMembresExplicitesProfil() {
		return nbMembresExplicitesProfil;
	}
	public void setNbMembresExplicitesProfil(int nbMembresExplicitesProfil) {
		this.nbMembresExplicitesProfil = nbMembresExplicitesProfil;
	}*/
	public boolean isChgtMembres() {
		return chgtMembres;
	}
	public void setChgtMembres(boolean chgtMembres) {
		this.chgtMembres = chgtMembres;
	}
	public List<PersonStatut> getListeMembresTries() {
		return listeMembresTries;
	}
	public void setListeMembresTries(List<PersonStatut> listeMembresTries) {
		this.listeMembresTries = listeMembresTries;
	}
	public boolean isModified() {
		return modified;
	}
	public void setModified(boolean modified) {
		this.modified = modified;
	}
	public List<PersonStatut> getListeMembresExplicitTries() {
		return listeMembresExplicitTries;
	}
	public void setListeMembresExplicitTries(List<PersonStatut> listeMembresExplicitTries) {
		this.listeMembresExplicitTries = listeMembresExplicitTries;
	}
	public List<Profil> getGroupesOrgaLiee() {
		return groupesOrgaLiee;
	}
	public void setGroupesOrgaLiee(List<Profil> groupesOrgaLiee) {
		this.groupesOrgaLiee = groupesOrgaLiee;
	}
	public List<ManagerStatut> getListeManagersTries() {
		return listeManagersTries;
	}
	public void setListeManagersTries(List<ManagerStatut> listeManagersTries) {
		this.listeManagersTries = listeManagersTries;
	}
	

}
