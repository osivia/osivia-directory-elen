<%@ include file="/WEB-INF/jsp/include.jsp"%>
<%@ page import="fr.toutatice.outils.ldap.entity.Person"%>
<%@page import="javax.portlet.WindowState"%>

<head>

<link type="text/css" rel="stylesheet"
	href="<%=request.getContextPath()%>/css/monProfilStyle.css" />

</head>


<h1>ERREUR</h1>

	<div class="ligne">
		Une erreur technique s'est produite, veuillez renouvellez l'op&eacute;ration ult&eacute;rieurement
	</div>

	<div class="ligne">
		<div class="bouton">
			<a href="<portlet:renderURL windowState="<%= WindowState.NORMAL.toString() %>"><portlet:param name="action" value="monProfil" /></portlet:renderURL>">Retour � ma fiche profil</a>   
		</div>
	</div>

</form>
