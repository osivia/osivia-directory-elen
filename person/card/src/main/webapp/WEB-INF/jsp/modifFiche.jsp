<%@ include file="/WEB-INF/jsp/include.jsp"%>
<%@ page contentType="text/plain; charset=UTF-8"%>


<portlet:defineObjects />

<portlet:actionURL var="annuler"><portlet:param name="action" value="annuler" /></portlet:actionURL>
<portlet:actionURL var="submit"><portlet:param name="action" value="submit"/></portlet:actionURL>



<form:form method="post" id="formChgtAvatarProfil" commandName="formUpload" action="${submit}" enctype="multipart/form-data" cssClass="form-horizontal">
	
	
	<div class="form-group">
		<div class="col-md-2">
			<img src="${fiche.avatar.url}" alt="avatar" class="avatar pull-right avatar-big" />
		</div>  
		<div class="col-md-10">
			<h1>${fiche.userConsulte.displayName}
				<small>(${fiche.idConsulte})</small>
				</h1>
			
		</div>
	</div>
	
	<div class="form-group">
		<form:label path="file" cssClass="col-md-2 control-label">
			<op:translate  key="label.avatar" />
		</form:label>
		
		<div class="col-md-10">
			<input type="file" name="file" value="" class="form-control"
			accept="image"
			onchange="changeBoolean();"/>
			<font style="color: #C11B17;"><form:errors path="file"/></font>
		</div>
	</div>
	
	<div class="row">
		<form:input type="hidden" path="chargementAvatar" name="chargementAvatar"/>
	</div>


	<div class="form-group">
		<form:label path="title" cssClass="col-md-2 control-label">
			<op:translate  key="label.title" />	
		</form:label>	
	
		<div class="col-md-10">
			<form:select path="title" name="title" value="${fiche.userConsulte.title}" cssClass="form-control">
				<form:option value=""></form:option>
				<form:option value="M.">M.</form:option>
				<form:option value="Mme">Mme</form:option>
			</form:select>
				
		</div>
	</div>



	<div class="form-group">
		<form:label path="sn" cssClass="col-md-2 control-label">
			<op:translate  key="label.sn" />
		
		</form:label>	
	
		<div class="col-md-10">
			<form:input path="sn" name="sn" value="${fiche.userConsulte.sn}" cssClass="form-control"/>
			<font style="color: #C11B17;"><form:errors path="sn"/></font>
				
		</div>
	</div>
	<div class="form-group">
		<form:label path="givenName" cssClass="col-md-2 control-label">
			<op:translate  key="label.givenName" />
			
	
		</form:label>	
	
		<div class="col-md-10">
			<form:input path="givenName" name="givenName" value="${fiche.userConsulte.givenName}" cssClass="form-control"/>
			<font style="color: #C11B17;"><form:errors path="givenName"/></font>
				
		</div>
	</div>	

	<div class="form-group">
		<form:label path="nouveauEmail" cssClass="col-md-2 control-label">
			<op:translate  key="label.mail" />

		</form:label>
		
		<div class="col-md-10">
			<form:input path="nouveauEmail" name="nouveauEmail" type="email" value="${fiche.userConsulte.mail}" cssClass="form-control"/>
			<font style="color: #C11B17;"><form:errors path="nouveauEmail"/></font>
		</div>
	
	</div>

    <div class="form-group">
        <form:label path="profession" cssClass="col-md-2 control-label"><op:translate key="label.profession" /></form:label>
        <div class="col-md-10">
            <form:input path="profession" value="${fiche.profilNuxeo.profession}" cssClass="form-control" />
        </div>
    </div>

	<div class="form-group"> 	
		<form:label path="telFixe" cssClass="col-md-2 control-label">
			<op:translate  key="label.telFixe" />
			
	
		</form:label>
	
		<div class="col-md-10">
			<form:input path="telFixe" name="telFixe" value="${fiche.profilNuxeo.telFixe}" cssClass="form-control"/>
			<font style="color: #C11B17;"><form:errors path="telFixe"/></font>
		</div>
	</div>
	<div class="form-group"> 	
		<form:label path="telMobile" cssClass="col-md-2 control-label">
			<op:translate  key="label.telMobile" />
		
		</form:label>
	
		<div class="col-md-10">
			<form:input path="telMobile" name="telMobile" value="${fiche.profilNuxeo.telMobile}" cssClass="form-control"/>
			<font style="color: #C11B17;"><form:errors path="telMobile"/></font>
		</div>
	</div>
	
	


	<div class="">
		<div class="col-md-2"></div>
		<div class="col-md-10 no-ajax-link">
			<button class="btn btn-primary" onclick="form.submit"> 
				<i class="glyphicons ok_2"></i>
				<span><op:translate  key="label.btn.ok" /></span>
			</button>
			
			<input class="btn btn-default" type="button" id="annuler" value="<op:translate  key="label.btn.back" />" onclick="updatePortletContent(this,'${annuler}');" />
				
		</div>
	</div>


</form:form>

	