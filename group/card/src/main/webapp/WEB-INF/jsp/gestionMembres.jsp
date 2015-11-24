<%@ page contentType="text/plain; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/include.jsp"%>


<portlet:defineObjects />

<portlet:renderURL var="displayAjouterGroupe">
	<portlet:param name="action" value="displayAjoutGroupe" />
</portlet:renderURL>
<portlet:renderURL var="displayAjouterPersonne">
	<portlet:param name="action" value="displayAjoutPersonne" />
</portlet:renderURL>
<portlet:actionURL var="rechercherMembre">
	<portlet:param name="action" value="rechercherMembre" />
</portlet:actionURL>
<portlet:actionURL var="valider">
	<portlet:param name="action" value="valider" />
</portlet:actionURL>
<portlet:actionURL var="annuler">
	<portlet:param name="action" value="annuler" />
</portlet:actionURL>
<portlet:actionURL var="ajouterGroupe">
	<portlet:param name="action" value="ajouterGroupe" />
	<portlet:param name="cn" value="SELECTED_TEXT1" />
</portlet:actionURL>


<h2>Gérer la liste des membres du groupe</h2>


<div class="row">

	<div class="col-md-6">

		<c:choose>
			<c:when
				test="${empty listeMembresUrl && ficheProfil.profil.peuplement== 'EXPLICITE'}">


				<em>Aucun membre n'a encore été ajouté à ce groupe par un
					gestionnaire</em>



			</c:when>
			<c:otherwise>
				<em>Ces membres ont été ajoutés au groupe par un gestionnaire
					ou un gestionnaire délégué;</em>

			</c:otherwise>
		</c:choose>


		<div class="table-responsive">
			<display:table name="listeMembresUrl" id="membre" pagesize="5">

				<display:column title="Membre(s)">
					<c:choose>
						<c:when test="${membre.clicable}">
							<a href="${membre.url}" title="Voir le détail"
								class="no-ajax-link"> ${membre.personne.cn}
								(${membre.personne.uid}) </a>
						</c:when>
						<c:otherwise>
								${membre.personne.cn} (${membre.personne.uid}))
							</c:otherwise>
					</c:choose>

					<c:if test="${membre.statut=='AJOUTER'}">
						<span class="label label-success">Ajouté</span>
					</c:if>
					<c:if test="${membre.statut=='SUPPRIMER'}">
						<span class="label label-danger">Supprimé</span>
					</c:if>

				</display:column>

				<display:column title="Suppression">
					<c:if test="${levelUserConnecte != 'LECTEUR'}">
						<a class="btn btn-default"
							href="<portlet:actionURL>
													<portlet:param name="action" value="deleteMember"/>
					         						<portlet:param name="uid" value="${membre.personne.uid}"/>
			         							</portlet:actionURL>">
							<i class="glyphicons halflings remove"> </i>
						</a>
					</c:if>
				</display:column>


			</display:table>
		</div>


	</div>

	<div class="col-md-6">



		<form:form method="post" modelAttribute="ficheProfil"
			id="formFicheProfilRechMembre" action="${rechercherMembre}"
			class="form-horizontal">


			<div class="panel panel-default">
				<div class="panel-heading no-ajax-link">
					<h3 class="panel-title">
						<a data-toggle="collapse" href="#divNewUser">Nouveau membre <i
							class="glyphicons halflings collapse"></i>
						</a>
					</h3>
				</div>
				<div id="divNewUser"
					class="panel-collapse collapse in ${newUserCollapse}">
					<div class="panel-body">
						<em>Pour ajouter une personne ou un groupe vous devez
							effectuer une recherche dans l'annuaire : </em>

						<div class="form-group">
							<form:label path="filtreMembre" cssClass="col-md-4 control-label">
								<is:getProperty key="label.filtreMembre" />
							</form:label>
							<div class="col-md-8">
								<form:input id="filtreMembre" path="filtreMembre" size="15"
									cssClass="form-control" />

							</div>
						</div>


						<div class="form-group">
							<form:label path="filtreRne" cssClass="col-md-4 control-label">
								<is:getProperty key="label.filtreRne" />
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
							<form:label path="macroProfil" cssClass="col-md-4 control-label">
								<is:getProperty key="label.macroProfil" />
							</form:label>
							<div class="col-md-8">
								<form:select id="macroProfil" path="macroProfil"
									cssClass="form-control">
									<form:options items="${macroProfils}" itemValue="cn"
										itemLabel="description" />
								</form:select>

							</div>
						</div>


						<div class="row">
							<div class="col-md-4"></div>
							<div class="col-md-8">
								<button class="btn btn-default" type="submit">
									<i class="glyphicons search"></i> <span><is:getProperty
											key="btn.search" /></span>
								</button>


							</div>


						</div>





						<c:if test="${! empty listeMembresRechercheUrl}">

							<display:table name="listeMembresRechercheUrl" id="membre" pagesize="5">

								<display:column title="Membre(s)">

									<i class="glyphicons ${manager.logo}"> </i>
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

								<display:column title="Ajout">
									<a class="btn btn-default"
										href="<portlet:actionURL>
													<portlet:param name="action" value="ajoutMember"/>
					         						<portlet:param name="uid" value="${membre.personne.uid}"/>
			         							</portlet:actionURL>">
										<i class="glyphicons halflings plus"> </i>
									</a>
								</display:column>

							</display:table>


						</c:if>
					</div>

				</div>
			</div>


		</form:form>


		<c:if test="${! empty ficheProfil.groupesOrgaLiee}">


			<form:form method="post" modelAttribute="ficheProfil"
				id="formFicheProfilAjoutGroupe" action="${ajouterGroupe}"
				class="form-horizontal">

				<div class="panel panel-default">
					<div class="panel-heading no-ajax-link">
						<h3 class="panel-title">
							<a data-toggle="collapse" href="#divUsersOfGroup">Ajouter un
								groupe <i class="glyphicons halflings collapse"></i>
							</a>
						</h3>
					</div>
					<div id="divUsersOfGroup"
						class="panel-collapse collapse ${usersOfGroupCollapse}">
						<div class="panel-body">

							<div class="form-group">
								<form:label path="filtreAjoutGroupe"
									cssClass="col-md-4 control-label">
									<is:getProperty key="label.filtreAjoutGroupe" />
								</form:label>

								<div class="col-md-8">
									<form:select id="filtreAjoutGroupe" path="filtreAjoutGroupe"
										cssClass="col-md-4 form-control" style="width: 50%">
										<form:options items="${ficheProfil.groupesOrgaLiee}" itemValue="cn"
											itemLabel="description" />
									</form:select>

								</div>
							</div>

							<div class="row">
								<div class="col-md-4"></div>
								<div class="col-md-8">
									<button class="btn btn-default" type="submit" >
										<i class="glyphicons plus"></i> <span><is:getProperty
												key="btn.ajoutGroupe" /></span>
									</button>

								</div>
							</div>
						</div>

					</div>
				</div>

			</form:form>
		</c:if>

	</div>
</div>



				


<div class="btn-group">
	<c:choose>
		<c:when test="${ficheProfil.modified}">
		
			<a class="btn btn-primary" href="${valider}"> <i
				class="glyphicons ok_2"></i> <span>Valider les modifications</span>
		</c:when>
		<c:otherwise>
			<a class="btn btn-primary disabled" href="#"> <i
				class="glyphicons ok_2"></i> <span>Valider les modifications</span>
		
		</c:otherwise>
	</c:choose>



	</a> <a href="${annuler}" id="annuler" value="Annuler"
		class="btn btn-default"> <i class="glyphicons ban"></i> <span>Annuler</span>
	</a>
</div>



