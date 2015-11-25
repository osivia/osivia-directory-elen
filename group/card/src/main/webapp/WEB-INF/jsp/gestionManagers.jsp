<%@ page contentType="text/plain; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/include.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>


<portlet:defineObjects />

<portlet:actionURL var="valider">
	<portlet:param name="action" value="valider" />
</portlet:actionURL>
<portlet:actionURL var="annuler">
	<portlet:param name="action" value="annuler" />
</portlet:actionURL>
<portlet:actionURL var="rechercherManager">
	<portlet:param name="action" value="rechercherManager" />
</portlet:actionURL>
<portlet:renderURL var="reloadManager">
	<portlet:param name="action" value="reloadManager" />
	<portlet:param name="typeManager" value="SELECTED_TEXT1" />
	<portlet:param name="rne" value="SELECTED_TEXT2" />
	<portlet:param name="filtre" value="SELECTED_TEXT3" />
</portlet:renderURL>


<h2>Gérer la liste des gestionnaires du groupe</h2>


<div class="row">

	<div class="col-md-6">

		<em>Personnes ou groupes de personnes disposant de la
			délégation pour la gestion de ce groupe. Vous êtes habilité à
			ajouter des personnes ou groupes à cette liste</em>

		<c:choose>
			<c:when test="${empty listeManagersGestionUrl}">

				<em>Aucun gestionnaire délégué n'a encore été ajouté</em>

			</c:when>

			<c:otherwise>

				<div class="table-responsive">
					<display:table name="listeManagersGestionUrl" id="manager" pagesize="5">

						<display:column title="Gestionnaire(s) délégué(s)">

							<i class="glyphicons ${manager.logo}"></i>
							<c:choose>
								<c:when test="${manager.clicable}">
										<a href="${manager.url}" title="Voir le détail" class="no-ajax-link">
											${manager.displayName} (${manager.id}) </a>
								</c:when>
								<c:otherwise>
									${manager.displayName} (${manager.id})
								</c:otherwise>
								
							</c:choose>
							<c:if test="${manager.statut=='AJOUTER'}">
								<span class="label label-success">Ajouté</span>
							</c:if>
							<c:if test="${manager.statut=='SUPPRIMER'}">
								<span class="label label-danger">Supprimé</span>
							</c:if>

						</display:column>

						<display:column title="Suppression">
							<c:if test="${manager.droitDelete}">
								<a class="btn btn-default"
									href="<portlet:actionURL>
												<portlet:param name="action" value="deleteManager"/>
				         						<portlet:param name="dnManager" value="${manager.dn}"/>
		         							</portlet:actionURL>">
									<i class="glyphicons halflings remove"> </i>
								</a>
							</c:if>
						</display:column>


					</display:table>
				</div>

				<em>Ces gestionnaires délégués ont été ajoutés au groupe par un
					gestionnaire ou un gestionnaire délégué</em>


			</c:otherwise>

		</c:choose>

		<p class="text-center">
		
			<div class=""btn-toolbar">
	
				<c:choose>
					<c:when test="${ficheProfil.modified}">
		
						<button class="btn btn-primary"
							onclick="if (!confirm('Etes vous sûr de vouloir déléguer votre responsabilité de gestion de ce groupe aux personnes contenues dans la liste ?')) 
						 			return false; updatePortletContent(this,'${valider}')">
							<i class="glyphicons ok"> </i> Valider les modifications
						</button>
					
					</c:when>
					<c:otherwise>
						<button class="btn btn-primary disabled" href="#">
							<i class="glyphicons ok"> </i> Valider les modifications
						</button>
					
					</c:otherwise>
				</c:choose>
				
	
				<a class="btn btn-default" title="retour" href="${annuler}"><span
					class="libelle">Annuler</span></a>
	
			</div>
		</p>


	</div>



	<div class="col-md-6">

		<form:form method="post" modelAttribute="ficheProfil"
			id="formFicheProfilRechManager" action="${rechercherManager}"
			class="form-horizontal">
			<div class="panel panel-default">
				<div class="panel-heading no-ajax-link">
					<h3 class="panel-title">
						<a data-toggle="collapse" href="#divNewUser">Ajouter un 
							gestionnaire délégué <i class="glyphicons halflings collapse"></i>
						</a>
					</h3>
				</div>
				<div id="divNewUser"
					class="panel-collapse collapse in ${newUserCollapse}">
					<div class="panel-body">
						<em>Pour ajouter une personne ou un groupe vous devez
							effectuer une recherche dans l'annuaire : </em>

						<div class="form-group">
							<form:label path="filtreManager"
								cssClass="col-md-4 control-label">
								<op:translate  key="label.filtreManager" />
							</form:label>
							<div class="col-md-8">
								<form:input id="filtreManager" path="filtreManager" size="15"
									cssClass="form-control" />

							</div>
						</div>


						<div class="form-group">
							<form:label path="filtreRne" cssClass="col-md-4 control-label">
								<op:translate  key="label.filtreRne" />
							</form:label>
							<div class="col-md-8">
								<form:select id="filtreRne" path="filtreRne"
									cssClass="form-control">
									<form:options items="${etbUserConnecte}" itemValue="id"
										itemLabel="description" />
								</form:select>

							</div>
						</div>

						<div class="form-group">
							<form:label path="typeManager" cssClass="col-md-4 control-label">
								<op:translate  key="label.typeManager" />
							</form:label>
							<div class="col-md-8">
								<div class="radio">
									<label>
										<form:radiobutton path="typeManager" value="person"
											onchange="refreshOnChange(this,filtreRne,filtreManager,'${reloadManager}');" />
											Personne
									</label>
								</div>
								<div class="radio">
									<label>
										<form:radiobutton path="typeManager" value="profil"
											onchange="refreshOnChange(this,filtreRne,filtreManager,'${reloadManager}');" />
											Groupe						
									</label>
								</div>								
								

									
							</div>
						</div>



						<c:if test="${ficheProfil.typeManager=='person'}">

							<div class="form-group">
								<form:label path="macroProfil" cssClass="col-md-4 control-label">
									<op:translate  key="label.macroProfil" />
								</form:label>
								<div class="col-md-8">
									<form:select id="macroProfil" path="macroProfil"
										cssClass="form-control">
										<form:options items="${macroProfils}" itemValue="cn"
											itemLabel="description" />
									</form:select>

								</div>
							</div>

						</c:if>



						<div class="row">
							<div class="col-md-4"></div>
							<div class="col-md-8">
								<button class="btn btn-default" type="submit">
									<i class="glyphicons search"></i> <span><op:translate 
											key="btn.search" /></span>
								</button>


							</div>


						</div>





						<c:if test="${! empty listeManagersUrlAjout}">

							<display:table name="listeManagersUrlAjout" id="manager" pagesize="5">
								<display:column>
									<display:column title="Membre(s)">
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

									<display:column title="Ajout">
										<c:if test="${manager.droitDelete}">

											<a class="btn btn-default"
												href="<portlet:actionURL>
													<portlet:param name="action" value="ajoutManager"/>
					         						<portlet:param name="dnManager" value="${manager.dn}"/>
			         							</portlet:actionURL>">
												<i class="glyphicons halflings plus"> </i>
											</a>
											</a>
										</c:if>
									</display:column>

								</display:column>

							</display:table>


						</c:if>
					</div>

				</div>
			</div>
		</form:form>

	</div>
</div>
