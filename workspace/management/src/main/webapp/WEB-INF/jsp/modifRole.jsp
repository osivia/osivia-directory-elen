<%@ include file="/WEB-INF/jsp/include.jsp"%>
<%@ page contentType="text/plain; charset=UTF-8"%>


<portlet:defineObjects />

<portlet:actionURL name="validerModifRole" var="valider"><portlet:param name="nomRole" value="${nomRole}"/><portlet:param name="provenance" value="${provenance}"/></portlet:actionURL>
<portlet:actionURL var="annuler" name="annulerModif"><portlet:param name="provenance" value="${provenance}"/></portlet:actionURL>
<portlet:actionURL var="ajouterGroupe" name="ajouterGroupe">
										<portlet:param name="nomRole" value="${nomRole}"/>
										<portlet:param name="provenance" value="${provenance}"/>
										<portlet:param name="cn" value="SELECTED_TEXT1"/>
</portlet:actionURL>

<portlet:actionURL var="rechercherPersonne" name="rechercherPersonne">
										<portlet:param name="nomRole" value="${nomRole}"/>
										<portlet:param name="provenance" value="${provenance}"/>
</portlet:actionURL>

<portlet:actionURL var="rechercherGroupe" name="rechercherGroupe">
										<portlet:param name="nomRole" value="${nomRole}"/>
										<portlet:param name="provenance" value="${provenance}"/>
										<portlet:param name="cn" value="SELECTED_TEXT1"/>
</portlet:actionURL>

<h3>${workspace.nom} (${workspace.shortname})</h3>
	

<h4>Modification du rôle : <op:translate  key="label.${nomRole}" /> </h4>

	
<div class="row">
	<div class="col-sm-6">
	
			<div class="table-responsive">
				<display:table name="workspace.listeGestionPersonnes" id="membre" pagesize="10">
					<display:column>
	
								<a href="<portlet:actionURL name="deletePersonne">
				         						<portlet:param name="uid" value="${membre.personne.uid}"/>
				         						<portlet:param name="nomRole" value="${nomRole}"/>
				         						<portlet:param name="provenance" value="${provenance}"/>
		         							</portlet:actionURL>">
		         						<i class="glyphicons halflings remove"> </i>
		         				</a>	
					
					</display:column>
					
					<display:column title="Personne(s)" sortable="true">
						<i class="glyphicons user"> </i>
						${membre.personne.cn} (${membre.personne.uid})
						<c:if test="${membre.statut=='AJOUTER'}">
									<span class="label label-success">Ajouté</span>
								</c:if>
								<c:if test="${membre.statut=='SUPPRIMER'}">
									<span class="label label-danger">Supprimé</span>
						</c:if>
					</display:column>
		
				</display:table>
				
				
				<c:if test="${config.enableGroups == true}">
					<display:table name="workspace.listeGestionProfils" id="profil" pagesize="10">
						<display:column>
								<a href="<portlet:actionURL name="deleteGroupe">
				         						<portlet:param name="cn" value="${profil.profil.cn}"/>
				         						<portlet:param name="nomRole" value="${nomRole}"/>
				         						<portlet:param name="provenance" value="${provenance}"/>
		         							</portlet:actionURL>">
		         						<i class="glyphicons halflings remove"> </i>
		         				</a>				
						</display:column>
						
						<display:column title="Groupe(s)" sortable="true">
							<i class="glyphicons group"> </i>
							${profil.profil.displayName}
							<c:if test="${profil.statut=='AJOUTER'}">
										<span class="label label-success">Ajouté</span>
									</c:if>
									<c:if test="${profil.statut=='SUPPRIMER'}">
										<span class="label label-danger">Supprimé</span>
							</c:if>
						</display:column>
			
					</display:table>
				</c:if>
			</div>
		
		
	</div>	
	
	<div class="col-sm-6">

		<c:if test="${config.enableGroups == true}">
			<h4>Ajouter un groupe au rôle</h4>
			
			<form:form method="post" modelAttribute="formGestion"
				id="formModifRoleRechGroupe" action="${rechercherGroupe}"
				class="form-horizontal">
				
				<div class="form-group">
					<form:label class="control-label col-sm-4" path="cnGroupe"><op:translate  key="label.selectGroup" /></form:label>
					<div class="col-sm-8">
						<div class="input-group">
							<form:select class="form-control" id="filtreAjoutGroupe" path="cnGroupe">
									<form:options items="${groupesOrgaUser}" itemValue="cn" itemLabel="description" />
							</form:select>
							<span class="input-group-btn">
						      	<button class="btn btn-default" type="button" onclick="ajouterGroupe(filtreAjoutGroupe,'${ajouterGroupe}');"><i class="glyphicons halflings plus"></i></button>
						     </span>
						</div>  
					</div>
				</div>
				
				<div class="form-group">
					<form:label class="control-label col-sm-4" path="cnGroupe">Ou saisissez le nom du groupe</form:label>
					<div class="col-sm-8">
						<div class="input-group">
							<input type="text" name="cnGroupe" id="cnGroupe"  class="form-control" value="${workspace.cnGroupe}" />
							<span class="input-group-btn">
						      	<button class="btn btn-default" type="submit"><i class="glyphicons halflings search"></i></button>
						     </span>
						</div>  
					</div>
				</div>

			</form:form>
			
			<c:if test="${! empty workspace.listeGroupeRecherche}">
				<div class="table-responsive">
					<display:table name="workspace.listeGroupeRecherche" id="profil" pagesize="10">
						<display:column>
		
			         				<a href="<portlet:actionURL name="ajouterGroupe">
						         						<portlet:param name="cn" value="${profil.cn}"/>
						         						<portlet:param name="nomRole" value="${nomRole}"/>
						         						<portlet:param name="provenance" value="${provenance}"/>
				         							</portlet:actionURL>">
				         						<i class="glyphicons halflings plus"> </i>
				         			</a>	
						
						</display:column>
						
						<display:column title="Groupes Recherchés" sortable="true">
							<i class="glyphicons group"> </i>
							${profil.displayName} (${profil.cn})
						</display:column>
			
					</display:table>
				
				</div>		
			</c:if>			
			
			<hr>
			
		</c:if>
		
		
		<h4>Ajouter une personne au rôle</h4>
		
		<form:form method="post" modelAttribute="formGestion"
			id="formModifRoleRechMembre" action="${rechercherPersonne}"
			class="form-horizontal">
		
			<div class="form-group">
				<form:label class="control-label col-sm-4" path="uidPersonne">Saisissez le nom ou l'identifiant de la personne recherchée</form:label>
				<div class="col-sm-8">
					<div class="input-group">
						<input type="text" name="uidPersonne" id="uidPersonne" class="form-control"  value="${workspace.uidPersonne}" />
						<span class="input-group-btn">
					      	<button class="btn btn-default" type="submit">
					      		<i class="glyphicons halflings search"></i>
					      	</button>
					     </span>
					</div>  
				</div>
			</div>
		
		</form:form>
		
		
	
		<c:if test="${! empty workspace.listePersonRecherche}">
			<div class="table-responsive">
				<display:table name="workspace.listePersonRecherche" id="membre" pagesize="10">
					<display:column>
	
								<a href="<portlet:actionURL name="ajoutPersonne">
				         						<portlet:param name="uid" value="${membre.uid}"/>
				         						<portlet:param name="nomRole" value="${nomRole}"/>
				         						<portlet:param name="provenance" value="${provenance}"/>
		         							</portlet:actionURL>">
		         						<i class="glyphicons halflings plus"> </i>
		         				</a>	
					
					</display:column>
					
					<display:column title="Personnes Recherchées" sortable="true">
						<i class="glyphicons user"> </i>
						${membre.cn} (${membre.uid})
					</display:column>
		
				</display:table>
				
		
			</div>

		</c:if>			
			
	
	</div>
</div>



<p class="no-ajax-link">			
	<a href="${valider}" class="btn btn-primary"><i class="glyphicons ok"></i> <span><op:translate  key="label.btn.valider"/></span> </a>
	<a href="${annuler}" class="btn btn-default"><i class="glyphicons ban"></i> <span><op:translate  key="label.btn.annuler"/></span> </a>
</p>	
	




	