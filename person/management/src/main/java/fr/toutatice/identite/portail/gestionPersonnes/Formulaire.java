package fr.toutatice.identite.portail.gestionPersonnes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;




@Service
@Scope("prototype, request")
public class Formulaire {

	String filtreNom="";

	private String filtreDptCns="";
	
	private Map<String, String> listeDptCns;
	
	
	
	private List<PersonUrl> listePersonnesRecherchees = new ArrayList<PersonUrl>();
	private List<PersonUrl> listeMesSurcharges = new ArrayList<PersonUrl>();
	private List<PersonUrl> listePersonnesSurchargees = new ArrayList<PersonUrl>();
	
	private HabilitationSurcharge.level levelSurchargeUserConnecte;

	public String getFiltreNom() {
		return filtreNom;
	}
	public void setFiltreNom(String filtreNom) {
		this.filtreNom = filtreNom;
	}
	
	/**
	 * @return the filtreDptCns
	 */
	public String getFiltreDptCns() {
		return filtreDptCns;
	}
	/**
	 * @param filtreDptCns the filtreDptCns to set
	 */
	public void setFiltreDptCns(String filtreDptCns) {
		this.filtreDptCns = filtreDptCns;
	}
	/**
	 * @return the listeDptCns
	 */
	public Map<String, String> getListeDptCns() {
		return listeDptCns;
	}
	/**
	 * @param listeDptCns the listeDptCns to set
	 */
	public void setListeDptCns(Map<String, String> listeDptCns) {
		this.listeDptCns = listeDptCns;
	}
	/**
	 * @return the listePersonnesRecherchees
	 */
	public List<PersonUrl> getListePersonnesRecherchees() {
		return listePersonnesRecherchees;
	}

	/**
	 * @param listePersonnesRecherchees
	 *            the listePersonnesRecherchees to set
	 */
	public void setListePersonnesRecherchees(List<PersonUrl> listePersonnesRecherchees) {
		this.listePersonnesRecherchees = listePersonnesRecherchees;
	}

	/**
	 * @return the listeMesSurcharges
	 */
	public List<PersonUrl> getListeMesSurcharges() {
		return listeMesSurcharges;
	}

	/**
	 * @param listeMesSurcharges
	 *            the listeMesSurcharges to set
	 */
	public void setListeMesSurcharges(List<PersonUrl> listeMesSurcharges) {
		this.listeMesSurcharges = listeMesSurcharges;
	}

	/**
	 * @return the listePersonnesSurchargees
	 */
	public List<PersonUrl> getListePersonnesSurchargees() {
		return listePersonnesSurchargees;
	}

	/**
	 * @param listePersonnesSurchargees
	 *            the listePersonnesSurchargees to set
	 */
	public void setListePersonnesSurchargees(List<PersonUrl> listePersonnesSurchargees) {
		this.listePersonnesSurchargees = listePersonnesSurchargees;
	}
	/**
	 * @return the levelSurchargeUserConnecte
	 */
	public HabilitationSurcharge.level getLevelSurchargeUserConnecte() {
		return levelSurchargeUserConnecte;
	}

	/**
	 * @param levelSurchargeUserConnecte
	 *            the levelSurchargeUserConnecte to set
	 */
	public void setLevelSurchargeUserConnecte(HabilitationSurcharge.level levelSurchargeUserConnecte) {
		this.levelSurchargeUserConnecte = levelSurchargeUserConnecte;
	}
	public void cleanFiltre(){
		this.filtreNom="";
		this.filtreDptCns="";

	}
	
	
}
