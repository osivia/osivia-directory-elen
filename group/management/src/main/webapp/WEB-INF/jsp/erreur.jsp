<%@ include file="/WEB-INF/jsp/include.jsp"%>
<%@ page import="fr.toutatice.outils.ldap.entity.Person"%>
<%@page import="javax.portlet.WindowState"%>



<h3>Erreur</h3>

	<div class="ligne">
		Une erreur s'est produite. Merci de r&eacute;-essayer ult&eacute;rieurement
	 	<a href="<portlet:renderURL><portlet:param name="action" value="" /></portlet:renderURL>">Recharger</a> 
	 </div>
