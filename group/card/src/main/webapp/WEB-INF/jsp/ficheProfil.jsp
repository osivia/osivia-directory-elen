<%@ page contentType="text/plain; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/include.jsp"%>



<portlet:defineObjects />

<portlet:actionURL var="rechercherMembre">
	<portlet:param name="action" value="rechercherMembre" />
</portlet:actionURL>
<portlet:actionURL var="valider">
	<portlet:param name="action" value="valider" />
</portlet:actionURL>
<portlet:renderURL var="delegation">
	<portlet:param name="action" value="gestionManagers" />
</portlet:renderURL>
<portlet:renderURL var="gestionMembres"
	windowState="<%=WindowState.MAXIMIZED.toString()%>">
	<portlet:param name="action" value="gestionMembres" />
</portlet:renderURL>


<h2 class="col-md-12">${ficheProfil.profil.displayName}
	(${ficheProfil.profil.cn})</h2>


<blockquote class="">
	<p>${ficheProfil.profil.description}</p>
</blockquote>


<div class="row">
	<div class="col-md-6">

		<c:if test="${not empty listeMembresUrl}">

			<c:choose>
				<c:when test="${ficheProfil.profil.peuplement== 'MIXTE'}">
					<c:set var="title_col" value="Membre(s) par défaut" />
				</c:when>
				<c:otherwise>
					<c:set var="title_col" value="Membre(s)" />
				</c:otherwise>
			</c:choose>

			<c:choose>
				<c:when test="${ficheProfil.chgtMembres}">
					<div class="table-responsive">
						<display:table name="listeMembresUrl" id="membre" pagesize="5">
							<display:column title="Membre(s)" sortable="true">
								<div class="no-ajax-link">
									<i class="glyphicons user"> </i>
									<c:choose>
										<c:when test="${membre.clicable}">
											<a href="${membre.url}" title="Voir le détail">
												${membre.personne.cn} (${membre.personne.uid}) </a>
										</c:when>
										<c:otherwise>
										${membre.personne.cn} (${membre.personne.uid}) 
									</c:otherwise>
									</c:choose>
								</div>

							</display:column>

						</display:table>
					</div>
				</c:when>
				<c:otherwise>
					Ce profil contient trop de membres pour que la liste puisse être affichée.<br />

				</c:otherwise>
			</c:choose>

		</c:if>

		<c:if
			test="${not empty listeMembresExplicitesUrl && ficheProfil.profil.peuplement== 'MIXTE'}">

			<div class="table-responsive">
				<display:table name="listeMembresExplicitesUrl" id="membre" pagesize="5">
					<display:column title="Membre(s) ajouté(s)" sortable="true">

						<i class="glyphicons user"> </i>
						<c:choose>
							<c:when test="${membre.clicable}">
								<a href="${membre.url}" title="Voir le détail"
									class="no-ajax-link"> ${membre.personne.cn}
									(${membre.personne.uid}) </a>
							</c:when>
							<c:otherwise>
							${membre.personne.cn} (${membre.personne.uid}) 
						</c:otherwise>
						</c:choose>
					</display:column>



				</display:table>



			</div>
		</c:if>


		<c:if
			test="${levelUserConnecte == 'ADMINISTRATEUR' || levelUserConnecte == 'GESTIONNAIRE'}">
			<c:if test="${ficheProfil.profil.peuplement != 'IMPLICITE'}">

				<a href="${gestionMembres}" class="btn btn-primary no-ajax-link"> <i
					class="glyphicons group"></i> <span>Gérer les membres</span>
				</a>

			</c:if>
		</c:if>
	</div>

	<div class="col-md-6">

		<c:if test="${levelUserConnecte != 'LECTEUR'}">

			<c:if test="${not empty listeManagersUrl}">
				<div class="table-responsive">
					<display:table name="listeManagersUrl" id="manager" pagesize="5">

						<display:column title="Gestionnaire(s)" sortable="true">

								<i class="glyphicons ${manager.logo}"> </i>
								<c:choose>
									<c:when test="${manager.clicable}">
										<a href="${manager.url}" title="Voir le détail" class="no-ajax-link">
											${manager.displayName} (${manager.id}) </a>
									</c:when>
									<c:otherwise>
								${manager.displayName} (${manager.id})
							</c:otherwise>
								</c:choose>

						</display:column>
					</display:table>
				</div>
			</c:if>


			<c:if test="${not empty listeExplicitManagersUrl}">

				<div class="table-responsive">
					<display:table name="listeExplicitManagersUrl" id="manager" pagesize="5">

						<display:column title="Gestionnaire(s) délégué(s)" sortable="true">

								<i class="glyphicons ${manager.logo}"> </i>
								<c:choose>
									<c:when test="${manager.clicable}">
										<a href="${manager.url}" title="Voir le détail" class="no-ajax-link">
											${manager.displayName} (${manager.id}) </a>
									</c:when>
									<c:otherwise>
								${manager.displayName} (${manager.id})
							</c:otherwise>
								</c:choose>

						</display:column>

					</display:table>

				</div>
			</c:if>

			<div class="btn-group no-ajax-link">
				<a href="${delegation}" class="btn btn-default"
					onclick="return confirm('Voulez vous déléguer votre responsabilité à une tierce persone pour la gestion de ce groupe ? \nPour ajouter/supprimer un membre utilisez l\'interface de gestion des membres')">
					<i class="glyphicons keys"></i> <span>Déléguer la gestion du
						groupe</span>
				</a>
			</div>
		</c:if>

	</div>
</div>

