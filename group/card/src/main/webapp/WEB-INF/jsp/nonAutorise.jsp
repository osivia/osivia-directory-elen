<%@ page contentType="text/plain; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/include.jsp"%>


<h3>Accès non autorisé</h3>

	<div class="ligne">
		Vous n'êtes pas habilité à accéder à ces informations
	</div>

	<div class="ligne">
	
		<p class="portlet-action-link">
			<a class="portlet-menuitem" title="retour" 
				href="<portlet:renderURL><portlet:param name="action" value="monProfil" /></portlet:renderURL>"> Retour à  la fiche du profil
			</a>
		</p>

	</div>


