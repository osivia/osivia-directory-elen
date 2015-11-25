<%@page import="javax.portlet.WindowState"%>
<%@ include file="/WEB-INF/jsp/include.jsp"%>
<%@ page contentType="text/plain; charset=UTF-8"%>

<%@ page import="fr.toutatice.outils.ldap.entity.Application"%>

<portlet:defineObjects />
<portlet:actionURL var="creationWorkspace" name="creationWorkspace"/> 
<portlet:renderURL var="annuler"><portlet:param name="action" value="showListe" /></portlet:renderURL>
<portlet:renderURL var="retour"><portlet:param name="action" value="demandeCreation" /></portlet:renderURL>



<form:form method="post" cssClass="form-horizontal" id="formCreationWks2" action="${creationWorkspace}" commandName="formCreation">  	
				
	<p>L'espace de travail que vous allez créer aura les caractéristiques suivantes :</p>		
		
	<div class="form-group">
		<label class="col-sm-4 control-label"><op:translate  key="label.nomEspace" /></label>
		<div class="col-sm-8">
			<p class="form-control-static">${workspace.nom}
			</p>	
		
		</div>
	</div>
	
	<div class="form-group">
		<label class="col-sm-4 control-label"><op:translate  key="label.codeEspace" /></label>
		<div class="col-sm-8">
			<p class="form-control-static">${workspace.shortname}
			</p>	
		
		</div>
	</div>

	
	<div class="form-group">
		<label class="col-sm-4 control-label"><op:translate  key="label.descriptionEspace" /></label>
		<div class="col-sm-8">
			<p class="form-control-static">${workspace.description}
			</p>	
		
		</div>
	</div>
	

	<div class="form-group">
				<div class="col-sm-8 col-sm-offset-4">
					<button class="btn btn-primary" type="submit"><i class="glyphicons halflings ok"></i><span><op:translate  key="label.btn.valider" /></span></button>
					<a href="${retour}" class="btn btn-default"><i class="glyphicons halflings step-backward"></i> <span><op:translate  key="label.btn.previous"/></span> </a>
		      		<a href="${annuler}" class="btn btn-default"><i class="glyphicons ban"></i> <span><op:translate  key="label.btn.annuler"/></span> </a>
		
				</div>		
	</div>	


		
</form:form>