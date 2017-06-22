<%@ include file="/WEB-INF/jsp/include.jsp"%>
<%@ page contentType="text/plain; charset=UTF-8"%>

<script type="text/javascript">
	var $JQry = jQuery.noConflict();
	
		$JQry(function() {
		$JQry('.password').pstrength();
		});
</script>


<portlet:defineObjects />

<portlet:actionURL var="annuler"><portlet:param name="action" value="annuler" /></portlet:actionURL>
<portlet:actionURL var="submit"><portlet:param name="action" value="submit"/></portlet:actionURL>



<form:form method="post" id="formCreation" commandName="formCreation" action="${submit}" enctype="multipart/form-data" cssClass="form-horizontal">


	<div class="form-group">
		<form:label path="title" cssClass="col-md-4 control-label">
			<op:translate  key="label.title" />	
		</form:label>	
	
		<div class="col-md-8">
			<form:select path="title" name="title" cssClass="form-control">
				<form:option value=""></form:option>
				<form:option value="M.">M.</form:option>
				<form:option value="Mme">Mme</form:option>
			</form:select>
				
		</div>
	</div>


	<t:input path="sn"/>
	<t:input path="givenName"/>
	<t:input path="uid"/>
	
	<div class="form-group">
		<form:label path="nouveauMdp" cssClass="col-md-4 control-label">
			<op:translate  key="label.pwd.new" />
		</form:label>	
	
		<div class="col-md-8"><form:input path="nouveauMdp" type="password" cssClass="form-control password"/>
			<form:errors path="nouveauMdp" />
		</div>
	</div>
	
	<div class="form-group">
		<form:label path="confirmMdp" cssClass="col-md-4 control-label">
			<op:translate  key="label.pwd.confirm" />
		</form:label>	
	
		<div class="col-md-8"><form:input path="confirmMdp" type="password" cssClass="form-control"/>
			<form:errors path="confirmMdp" />
		</div>
	</div>
	

	
	<t:input path="email"/>
	
	<div class="row">
		<div class="col-md-4"></div>
		
		<div class="col-md-8">
			<div class="btn-toolbar">
				<button class="btn btn-primary" onclick="form.submit"> 
					<i class="glyphicons glyphicons-user_add"></i>
					<span><op:translate  key="label.btn.ok" /></span>
				</button>
				
				<input class="btn btn-default" type="reset" id="Effacer" value="<op:translate  key="label.btn.clear" />"  />
			</div>		
		</div>
	
	</div>

</form:form>
