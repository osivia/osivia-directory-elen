package fr.toutatice.identite.portail.gestionGroupes;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import fr.toutatice.outils.ldap.entity.Profil;




@Service
@Scope("prototype, request")
public class Formulaire {

	
	private String filtre="";
	private List<Profil> listeMesGroupes = new ArrayList<Profil>();
	private List<Profil> listeGroupesGeres = new ArrayList<Profil>();
	private List<Profil> listeGroupesRecherches = new ArrayList<Profil>();
	private String filtreOrga="";
	private boolean contexteOrga=false;
	
	public String getFiltre() {
		return filtre;
	}

	public void setFiltre(String filtre) {
		this.filtre = filtre;
	}


	public List<Profil> getListeMesGroupes() {
		return listeMesGroupes;
	}
	public void setListeMesGroupes(List<Profil> listeMesGroupes) {
		this.listeMesGroupes = listeMesGroupes;
	}
	public List<Profil> getListeGroupesGeres() {
		return listeGroupesGeres;
	}

	public void setListeGroupesGeres(List<Profil> listeGroupesGeres) {
		this.listeGroupesGeres = listeGroupesGeres;
	}

	public List<Profil> getListeGroupesRecherches() {
		return listeGroupesRecherches;
	}

	public void setListeGroupesRecherches(List<Profil> listeGroupesRecherches) {
		this.listeGroupesRecherches = listeGroupesRecherches;
	}

	public String getFiltreOrga() {
		return filtreOrga;
	}
	public void setFiltreOrga(String filtreOrga) {
		this.filtreOrga = filtreOrga;
	}
	

	public boolean isContexteOrga() {
		return contexteOrga;
	}

	public void setContexteOrga(boolean contexteOrga) {
		this.contexteOrga = contexteOrga;
	}

	public Formulaire() {
		super();
		this.filtre="";
	}

	
}
