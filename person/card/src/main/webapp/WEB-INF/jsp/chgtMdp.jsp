<%@ include file="/WEB-INF/jsp/include.jsp"%>
<script type="text/javascript">
	var $JQry = jQuery.noConflict();
	
		$JQry(function() {
		$JQry('.password').pstrength();
		});
</script>


<portlet:defineObjects />

<portlet:actionURL var="modifMdp" windowState="<%= WindowState.NORMAL.toString() %>"><portlet:param name="action" value="updateMdp" /></portlet:actionURL>
<portlet:renderURL var="retourFiche" windowState="<%= WindowState.NORMAL.toString() %>"><portlet:param name="action" value="" /></portlet:renderURL>

<h1>Modification de votre mot de passe :</h1>


<form:form name="chgtMdp" method="post" commandName="formChgtMdp" id="chgtMdpFichePersonneForm" action="${modifMdp}" cssClass="form-horizontal">

	<div class="form-group">
		<form:label path="mdpActuel" cssClass="col-md-2 control-label">
			<op:translate  key="label.pwd.current" />
		</form:label>	
	
		<div class="col-md-10"><form:input path="mdpActuel" type="password" cssClass="form-control"/>
			<form:errors path="mdpActuel" />
		</div>
	</div>
	
	<div class="form-group">
		<form:label path="nouveauMdp" cssClass="col-md-2 control-label">
			<op:translate  key="label.pwd.new" />
		</form:label>	
	
		<div class="col-md-10"><form:input path="nouveauMdp" type="password" cssClass="form-control password"/>
			<form:errors path="nouveauMdp" />
		</div>
	</div>
	
	<div class="form-group">
		<form:label path="confirmMdp" cssClass="col-md-2 control-label">
			<op:translate  key="label.pwd.confirm" />
		</form:label>	
	
		<div class="col-md-10"><form:input path="confirmMdp" type="password" cssClass="form-control"/>
			<form:errors path="confirmMdp" />
		</div>
	</div>
	
	<div>
		<div class="col-md-2"></div>
		<div class="col-md-10">
			<button class="btn btn-primary" onclick="form.submit"> 
				<i class="glyphicons ok_2"></i>
				<span><op:translate  key="label.btn.ok" /></span>
			</button>
			
			<a class="btn btn-default" id="annuler" href="${retourFiche}">
				<op:translate  key="label.btn.back" />
			</a>
				
		</div>
	</div>
	

</form:form>