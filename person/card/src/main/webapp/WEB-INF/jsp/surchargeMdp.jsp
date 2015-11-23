<%@ include file="/WEB-INF/jsp/include.jsp"%>



<portlet:defineObjects />

<portlet:actionURL var="surchargeMdp"><portlet:param name="action" value="surchargeMdp" /></portlet:actionURL>
<portlet:renderURL var="retourFiche"><portlet:param name="action" value="fichePersonne" /></portlet:renderURL>

<h1>Surcharge du mot de passe :</h1>


<form:form name="surchargeMdp" method="post" commandName="formSurchargeMdp" id="surchargeMdpFichePersonneForm" action="${surchargeMdp}" cssClass="form-horizontal">



	<div class="form-group">
		<c:if test="${fiche.levelUserConnecteSurcharge == 'DROIT_SURCHARGE'}">
			<form:label path="motifSurcharge" cssClass="col-md-2 control-label">
				<is:getProperty key="label.motifSurcharge" />
			</form:label>
		</c:if>
		<c:if test="${fiche.levelUserConnecteSurcharge == 'DROIT_SURCHARGE_ASSISTANCE'}">
			<form:label path="motifSurcharge" cssClass="col-md-2 control-label">
				<is:getProperty key="label.refTicket" />
			</form:label>
		</c:if>
	
		<div class="col-md-10">
			<form:textarea path="motifSurcharge" name="motifSurcharge" cssClass="form-control" value="${fiche.motifSurcharge}" />
			<form:errors path="motifSurcharge" />
		</div>	
	</div>


	<div class="form-group">
		<form:label path="mdpSurcharge" cssClass="col-md-2 control-label">
			<is:getProperty key="label.mdpSurcharge" />
		</form:label>
	
		<div class="col-md-10"><form:input path="mdpSurcharge" type="password" cssClass="form-control"/>
			<form:errors path="mdpSurcharge" />
		</div>
	</div>

	
	<div class="form-group">
		<form:label path="mdpUserConnecte" cssClass="col-md-2 control-label">
			<is:getProperty key="label.mdpUserConnecte" />
		</form:label>
	
		<div class="col-md-10"><form:input path="mdpUserConnecte" type="password" cssClass="form-control"/>
			<form:errors path="mdpUserConnecte" />
		</div>
	</div>
	

	<div>
		<div class="col-md-offset-2 col-md-10">
			<button class="btn btn-primary" onclick="form.submit"> 
				<i class="glyphicons ok_2"></i>
				<span><is:getProperty key="label.btn.ok" /></span>
			</button>
			
			<a class="btn btn-default" id="annuler" href="${retourFiche}">
				<is:getProperty key="label.btn.back" />
			</a>
				
		</div>
	</div>
</form:form>