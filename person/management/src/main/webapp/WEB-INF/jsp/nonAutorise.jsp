<%@ include file="/WEB-INF/jsp/include.jsp"%>
<%@page import="javax.portlet.WindowState"%>

<head>

<link type="text/css" rel="stylesheet"
	href="<%=request.getContextPath()%>/css/recherchePersonneStyle.css" />

</head>


<h3>Accès non autorisé</h3>

	<div class="ligne">
		Vous n'êtes pas habilité à accéder à cette fonctionnalité
	</div>

	<div class="ligne">
		<div class="bouton">
			<a href="<portlet:renderURL windowState="<%= WindowState.NORMAL.toString() %>"><portlet:param name="action" value="recherchePersonne" /></portlet:renderURL>">Retour à la recherche de personne</a>   
		</div>
	</div>


