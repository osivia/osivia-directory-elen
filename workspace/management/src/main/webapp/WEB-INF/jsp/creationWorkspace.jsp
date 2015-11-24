<%@ include file="/WEB-INF/jsp/include.jsp"%>
<%@ page contentType="text/plain; charset=UTF-8"%>


<portlet:defineObjects />
<portlet:actionURL var="demandeCreation" name="demandeCreationAction"/> 
<portlet:renderURL var="retour"><portlet:param name="action" value="retourListe" /></portlet:renderURL>



<form:form method="post" cssClass="form-horizontal" id="formCreationWks" name="formCreationWks" modelAttribute="workspace" action="${demandeCreation}">  	
			
	<c:if test="${workspace.modeOpenWorkspace}">
		<h3>Création d'un open-workspace</h3>
	</c:if>

	<c:if test="${not workspace.modeOpenWorkspace}">
		<h3>Création d'un espace de travail</h3>
	</c:if>
	

	<div class="form-group">
		<label class="col-sm-4 control-label"><is:getProperty key="label.nomEspace" /></label>
		<div class="col-sm-8">
			<form:input class="form-control" type="text" name="nom" path="nom" value="${workspace.nom}"/>
			<font style="color: #C11B17;"><form:errors path="nom" /></font>	
		</div>
	</div>
	
	<div class="form-group">
		<label class="col-sm-4 control-label"><is:getProperty key="label.descriptionEspace" /></label>
		<div class="col-sm-8">
			<textarea class="form-control path="description" name="description" rows="4" >${workspace.description}</textarea>
			<font style="color: #C11B17;"><form:errors path="description" /></font>	
		</div>
	</div>
	
	
	<div class="form-group">
			<div class="col-sm-8 col-sm-offset-4">
				<button class="btn btn-primary" type="submit"><i class="glyphicons halflings play"></i><span><is:getProperty key="label.btn.suivant" /></span></button>

	      		<a href="${retour}" class="btn btn-default"><i class="glyphicons ban"></i> <span><is:getProperty key="label.btn.annuler"/></span> </a>
	
			</div>		
	</div>	

		

		
	
	
	

		
		
</form:form>