package fr.toutatice.outils.ldap.entity;

import java.util.List;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;

import org.osivia.portal.api.directory.DirectoryBean;

import fr.toutatice.outils.ldap.exception.ToutaticeAnnuaireException;

public interface Profil extends DirectoryBean {

	
	public enum typePeuplement {IMPLICITE,EXPLICITE,MIXTE;
		public String getString() {
	    return this.name();}
	}
	
	/**
	 * Getter du nom du profil (identifiant LDAP)
	 * @return nom du profil
	 */
	public String getCn();

	public void setCn(String cn);

	public String getDisplayName();

	public void setDisplayName(String displayName);

	/**
	 * DN du profil (construit à la demande, attribut non récupéré dans l'annuaire)
	 * @return dn du profil
	 */
	public String getDn();

	public String getDescription();

	public void setDescription(String description);

	public String getType();

	public void setType(String type);

	public typePeuplement getPeuplement();

	public void setPeuplement(typePeuplement peuplement);

	/**
	 * Liste des DN des membres du profil
	 * @return
	 * @throws ToutaticeAnnuaireException 
	 */
	public List<String> getListeMembers() throws ToutaticeAnnuaireException;

	public void setListeMembers(List<String> listeMembers);

	/**Ajout d'un membre au profil
	 * Un test vérifie si cette personne n'est pas déjà membre (case non sensitive), si c'est le cas un log d'erreur est généré
	 * @param dnMember dn de la personne que l'on souhaite ajouter comme membre
	 * @throws ToutaticeAnnuaireException 
	 * @throws NamingException 
	 */
	public void addMember(String dnMember) throws ToutaticeAnnuaireException;

	public Attribute getListeMembersAttributes();

	public void setListeMembersAttributes(Attribute listeMembersAttributes);

	/**
	 * Suppression d'un membre du profil
	 * Si la personne n'est pas dans la liste un log d'erreur est généré
	 * @param dnMember dn de la personne que l'on souhaite retirer de la liste des membres
	 * @throws ToutaticeAnnuaireException 
	 * @throws NamingException 
	 */
	public void removeMember(String dnMember) throws ToutaticeAnnuaireException;

	public void removeMemberFromMemberAttributes(String dnMember);

	/**
	 * Indique si la personne dont le dn est passé en paramètre est membre du profil
	 * @param dnMember DN de la personne testée
	 * @return true si la personne est membre du profil, false sinon
	 */
	public boolean isMember(String dnMember);

	/**
	 * Liste des DN des membres explicites du profil (membres rajoutés manuellement, et non via LDAPOMATIC)
	 * Note : LDAPOMATIC ajoute la liste des membres explicites à la liste des membres implicites lors de chaque passage
	 * @return
	 */
	public List<String> getListeExplicitMembers();

	public void setListeExplicitMembers(List<String> listeExplicitMembers);

	/**Ajout d'un membre explicite au profil. Le membre est également ajouté à la liste des membres implicites
	 * Un test vérifie si cette personne n'est pas déjà membre explicite ou implicite (case non sensitive), si c'est le cas un log d'erreur est généré
	 * @param dnMember dn de la personne que l'on souhaite ajouter comme membre explicite
	 * @throws ToutaticeAnnuaireException 
	 */
	public void addExplicitMember(String dnExplicitMember)
			throws ToutaticeAnnuaireException;

	/**
	 * Suppression d'un membre explicite du profil. Le membre est également supprimé de la liste des membres
	 * Si la personne n'est pas dans la liste un log d'erreur est généré
	 * @param dnExplicitMember dn de la personne que l'on souhaite retirer de la liste des membres explicites
	 * @throws ToutaticeAnnuaireException 
	 */
	public void removeExplicitMember(String dnExplicitMember)
			throws ToutaticeAnnuaireException;

	/**
	 * Indique si la personne dont le dn est passé en paramètre est membre explicite du profil
	 * @param dnMember DN de la personne testée
	 * @return true si la personne est membre explicite du profil, false sinon
	 */
	public boolean isExplicitMember(String dnMember);

	/**
	 * Liste des DN des managers du profil (ie les personnes habilitées à modifier le profil
	 * @return
	 */
	public List<String> getListeManagers();

	public void setListeManagers(List<String> listeManagers);

	public List<String> getListeExplicitManagers();

	public void setListeExplicitManagers(List<String> listeExplicitManagers);

	public void addExplicitManager(String dnManager);

	public void removeExplicitManager(String dnManager);

	public String getFiltreRecherche();

	public void setFiltreRecherche(String filtreRecherche);

	/**
	 * Indique si la personne dont le dn est passé en paramètre est manager du profil
	 * La personne est manager si son dn ou si un des profils est dans la liste des managers du profil
	 * @param dnMember DN de la personne testée
	 * @return true si la personne est manager du profil, false sinon
	 */
	public boolean isManagedBy(Person p);

	public Profil getNewProfil(String cn, String displayName,
			String description, String type, typePeuplement peuplement);

	/**
	 * Récupère le DN complet du profil (avec la base de l'annuaire)
	 * @param cn nom du profil
	 * @return dn de l'objet dans l'annuaire
	 */
	public String findFullDn(String cn);

	/**
	 * Recherche d'un profil par son adresse (DN)
	 * @param dn adresse du profil recherché
	 * @return profil recherché
	 * @throws ToutaticeAnnuaireException 
	 */
	public Profil findProfilByDn(String dn);

	/**
	 * Recherche d'un profil par son nom
	 * @param cn nom exact du profil recherché
	 * @return profil recherché
	 * @throws ToutaticeAnnuaireException 
	 */
	public Profil findProfilByCn(String cn);

	/**
	 * Recherche de profils par nom
	 * @param cn début du nom des profils recherchés
	 * @return liste des profils correspondant aux critères
	 */
	public List<Profil> findProfilByDebutCn(String cn);

	public List<Profil> findProfilByRneNom(String rne, String cn);

	/**
	 * Recherche de profils via un filtre LDAP
	 * @param filtre filtre LDAP
	 * @return profils correspondant au filtre
	 */
	public List<Profil> findProfilByFiltre(String filtre);

	public void create() throws ToutaticeAnnuaireException;

	public void delete() throws ToutaticeAnnuaireException;

	/**
	 * Mise à jour d'un profil
	 * Mise à jour du nom, de la description, des listes des membres, membres explicites et managers
	 * La mise à jour de la liste des membres entraine la mise à jour des profils de chacun des membres ajoutés ou supprimés
	 * @throws ToutaticeAnnuaireException 
	 * @throws NamingException 
	 */
	public void updateProfil() throws ToutaticeAnnuaireException;

	/**
	 * Recherche des profils d'une personne
	 * @param p personne dont on recherche les profils
	 * @return liste des objets profils associés à la personne
	 */
	public List<Profil> findProfilsPersonne(Person p);

	/**
	 * Recherche de la liste des classes d'un établissement
	 * @param rne
	 * @return liste des profils des classes de l'établissement
	 */
	public List<Profil> findListeClassesEtb(String rne);

	/**
	 * Recherche de la classe d'un élève
	 * @param eleve
	 * @return profil de la classe de l'élève
	 */
	public Profil findClasseEleve(Person eleve);

	public List<Structure> findOrganisationsLiees();


}