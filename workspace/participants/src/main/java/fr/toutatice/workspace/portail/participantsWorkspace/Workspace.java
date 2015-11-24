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
package fr.toutatice.workspace.portail.participantsWorkspace;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.toutatice.outils.ldap.entity.Person;
import fr.toutatice.outils.ldap.entity.Profil;
import fr.toutatice.workspace.portail.participantsWorkspace.bean.ParticipantUrl;


@Component
@Scope("prototype")
public class Workspace{

	String nom="";
	String shortname;
	String description="";
	String auteur="";
	String sourceOrganisationnelle="";
	String theme="acrennes-en-etab-style01";
	
	String path;
	String pathParent;
	String url;
	String permaLink;
	
	@Autowired
	Profil groupeAnimateurs;
	@Autowired
	Profil groupeContributeurs;
	@Autowired
	Profil groupeLecteurs;
	
	List<ParticipantUrl> membresGroupeAnimateurs = new ArrayList<ParticipantUrl>();
	List<ParticipantUrl> membresGroupeContributeurs = new ArrayList<ParticipantUrl>();
	List<ParticipantUrl> membresGroupeLecteurs = new ArrayList<ParticipantUrl>();
		
	List<ParticipantUrl> listeAnimateurs = new ArrayList<ParticipantUrl>();
	List<ParticipantUrl> listeContributeurs = new ArrayList<ParticipantUrl>();
	List<ParticipantUrl> listeLecteurs = new ArrayList<ParticipantUrl>();
	
	boolean modeOpenWorkspace;
	boolean modifiableByUser = false;
	
	
	String uidPersonne="";
	String cnGroupe;
	List<Person> listePersonRecherche = new ArrayList<Person>();
	List<Profil> listeGroupeRecherche = new ArrayList<Profil>();
	
	public String getNom() {
		return nom;	}
	public void setNom(String nom) {
		this.nom = nom;	}
	public String getShortname() {
		return shortname;	}
	public void setShortname(String shortname) {
		this.shortname = shortname;	}
	public String getDescription() {
		return description;	}
	public void setDescription(String description) {
		this.description = description;	}
	
	public Profil getGroupeAnimateurs() {
		return groupeAnimateurs;
	}
	public void setGroupeAnimateurs(Profil groupeAnimateurs) {
		this.groupeAnimateurs = groupeAnimateurs;
	}
	public Profil getGroupeContributeurs() {
		return groupeContributeurs;
	}
	public void setGroupeContributeurs(Profil groupeContributeurs) {
		this.groupeContributeurs = groupeContributeurs;
	}
	public Profil getGroupeLecteurs() {
		return groupeLecteurs;
	}
	public void setGroupeLecteurs(Profil groupeLecteurs) {
		this.groupeLecteurs = groupeLecteurs;
	}
	public boolean isModeOpenWorkspace() {
		return modeOpenWorkspace;	}
	public void setModeOpenWorkspace(boolean modeOpenWorkspace) {
		this.modeOpenWorkspace = modeOpenWorkspace;	}
	public String getPath() {
		return path;	}
	public void setPath(String path) {
		this.path = path;	}
	public String getUrl() {
		return url;	}
	public String getPathParent() {
		return pathParent;	}
	public void setPathParent(String pathParent) {
		this.pathParent = pathParent;	}
	public void setUrl(String url) {
		this.url = url;	}
	public String getPermaLink() {
		return permaLink;	}
	public void setPermaLink(String permaLink) {
		this.permaLink = permaLink;	}
	public String getSourceOrganisationnelle() {
		return sourceOrganisationnelle;	}
	public void setSourceOrganisationnelle(String sourceOrganisationnelle) {
		this.sourceOrganisationnelle = sourceOrganisationnelle;	}
	public String getAuteur() {
		return auteur;
	}
	public void setAuteur(String auteur) {
		this.auteur = auteur;
	}
	public List<ParticipantUrl> getListeAnimateurs() {
		return listeAnimateurs;
	}
	public void setListeAnimateurs(List<ParticipantUrl> listeAnimateurs) {
		this.listeAnimateurs = listeAnimateurs;
	}
	public List<ParticipantUrl> getListeContributeurs() {
		return listeContributeurs;
	}
	public void setListeContributeurs(List<ParticipantUrl> listeContributeurs) {
		this.listeContributeurs = listeContributeurs;
	}
	public List<ParticipantUrl> getListeLecteurs() {
		return listeLecteurs;
	}
	public void setListeLecteurs(List<ParticipantUrl> listeLecteurs) {
		this.listeLecteurs = listeLecteurs;
	}
	public List<ParticipantUrl> getMembresGroupeAnimateurs() {
		return membresGroupeAnimateurs;
	}
	public void setMembresGroupeAnimateurs(List<ParticipantUrl> membresGroupeAnimateurs) {
		this.membresGroupeAnimateurs = membresGroupeAnimateurs;
	}
	public List<ParticipantUrl> getMembresGroupeContributeurs() {
		return membresGroupeContributeurs;
	}
	public void setMembresGroupeContributeurs(List<ParticipantUrl> membresGroupeContributeurs) {
		this.membresGroupeContributeurs = membresGroupeContributeurs;
	}
	public List<ParticipantUrl> getMembresGroupeLecteurs() {
		return membresGroupeLecteurs;
	}
	public void setMembresGroupeLecteurs(List<ParticipantUrl> membresGroupeLecteurs) {
		this.membresGroupeLecteurs = membresGroupeLecteurs;
	}
	public boolean isModifiableByUser() {
		return modifiableByUser;
	}
	public void setModifiableByUser(boolean modifiableByUser) {
		this.modifiableByUser = modifiableByUser;
	}
	
	
	public String getUidPersonne() {
		return uidPersonne;
	}
	public void setUidPersonne(String uidPersonne) {
		this.uidPersonne = uidPersonne;
	}
	public List<Person> getListePersonRecherche() {
		return listePersonRecherche;
	}
	public void setListePersonRecherche(List<Person> listePersonRecherche) {
		this.listePersonRecherche = listePersonRecherche;
	}
	public String getCnGroupe() {
		return cnGroupe;
	}
	public void setCnGroupe(String cnGroupe) {
		this.cnGroupe = cnGroupe;
	}
	public List<Profil> getListeGroupeRecherche() {
		return listeGroupeRecherche;
	}
	public void setListeGroupeRecherche(List<Profil> listeGroupeRecherche) {
		this.listeGroupeRecherche = listeGroupeRecherche;
	}
	public String getTheme() {
		return theme;
	}
	public void setTheme(String theme) {
		this.theme = theme;
	}
	
	
}
