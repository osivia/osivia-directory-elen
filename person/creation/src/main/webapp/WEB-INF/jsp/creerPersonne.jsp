<%@ include file="/WEB-INF/jsp/include.jsp"%>
<%@ page contentType="text/plain; charset=UTF-8"%>


<portlet:defineObjects />

<portlet:actionURL var="annuler"><portlet:param name="action" value="annuler" /></portlet:actionURL>
<portlet:actionURL var="submit"><portlet:param name="action" value="submit"/></portlet:actionURL>



<form:form method="post" id="formCreation" commandName="formCreation" action="${submit}" enctype="multipart/form-data" cssClass="form-horizontal">


	<t:input path="sn"/>
	<t:input path="givenName"/>
	<t:input path="uid"/>
	<t:input path="email"/>
	
	<div class="row">
		<div class="col-md-4"></div>
		
		<div class="col-md-8">
			<div class="btn-toolbar">
				<button class="btn btn-primary" onclick="form.submit"> 
					<i class="glyphicons user_add"></i>
					<span><is:getProperty key="label.btn.ok" /></span>
				</button>
				
				<input class="btn btn-default" type="reset" id="Effacer" value="<is:getProperty key="label.btn.clear" />"  />
			</div>		
		</div>
	
	</div>

</form:form>
