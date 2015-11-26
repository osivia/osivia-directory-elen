<%@ include file="/WEB-INF/jsp/include.jsp"%>
<%@ page contentType="text/plain; charset=UTF-8"%>

<portlet:defineObjects />
<portlet:actionURL name="validerModifWks" var="valider"><portlet:param name="path" value="${workspace.path}"/></portlet:actionURL>
<portlet:renderURL var="retourEspace"><portlet:param name="action" value="consulterEspace"/><portlet:param name="path" value="${workspace.path}"/></portlet:renderURL>
<portlet:renderURL var="retourListe"><portlet:param name="action" value="liste"/></portlet:renderURL>


<h3>${workspace.nom} (${workspace.shortname})</h3>

<blockquote>
	<p>${workspace.description}</p>
</blockquote>



<form:form method="post" cssClass="form-horizontal" commandName="formModifWks" action="${valider}">  	
			
	<h4>Modification de l'espace</h4>
	
	<div class="form-group">
		<label class="col-sm-4 control-label"><op:translate  key="label.nomEspace" /></label>
		<div class="col-sm-8">
			<form:input class="form-control" type="text" name="nom" path="nom" value="${workspace.nom}"/>
			<font style="color: #C11B17;"><form:errors path="nom" /></font>	
		</div>
	</div>
	
	<div class="form-group">
		<label class="col-sm-4 control-label"><op:translate  key="label.descriptionEspace" /></label>
		<div class="col-sm-8">
			<textarea class="form-control path="description" name="description" rows="4" >${workspace.description}</textarea>
			<font style="color: #C11B17;"><form:errors path="description" /></font>	
		</div>
	</div>
	

	<div class="form-group">
			<div class="col-sm-8 col-sm-offset-4">
				<button class="btn btn-primary" type="submit"><i class="glyphicons halflings ok"></i> <span><op:translate  key="label.btn.valider" /></span></button>
				<c:if test="${provenance=='consulterEspace'}">
					<a href="${retourEspace}" class="btn btn-default"><i class="glyphicons ban"></i> <span><op:translate  key="label.btn.annuler"/></span> </a>
	      		</c:if>
	      		<c:if test="${provenance!='consulterEspace'}">
	      			<a href="${retourListe}" class="btn btn-default"><i class="glyphicons ban"></i> <span><op:translate  key="label.btn.annuler"/></span> </a>
	      			
	      		</c:if>
		
			</div>		
	</div>	


	
		
		
</form:form>