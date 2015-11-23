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
			<is:getProperty key="label.avatar" />
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
			<is:getProperty key="label.title" />	
		</form:label>	
	
		<div class="col-md-10">
			<form:select path="title" name="title" value="${fiche.userConsulte.title}" cssClass="form-control">
				<form:option value=""></form:option>
				<form:option value="M.">M.</form:option>
				<form:option value="Mme.">Mme.</form:option>
				<form:option value="Mlle.">Mlle.</form:option>
			</form:select>
				
		</div>
	</div>



	<div class="form-group">
		<form:label path="sn" cssClass="col-md-2 control-label">
			<is:getProperty key="label.sn" />
		
		</form:label>	
	
		<div class="col-md-10">
			<form:input path="sn" name="sn" value="${fiche.userConsulte.sn}" cssClass="form-control"/>
			<font style="color: #C11B17;"><form:errors path="sn"/></font>
				
		</div>
	</div>
	<div class="form-group">
		<form:label path="givenName" cssClass="col-md-2 control-label">
			<is:getProperty key="label.givenName" />
			
	
		</form:label>	
	
		<div class="col-md-10">
			<form:input path="givenName" name="givenName" value="${fiche.userConsulte.givenName}" cssClass="form-control"/>
			<font style="color: #C11B17;"><form:errors path="givenName"/></font>
				
		</div>
	</div>	

	<div class="form-group">
		<form:label path="nouveauEmail" cssClass="col-md-2 control-label">
			<is:getProperty key="label.mail" />

		</form:label>
		
		<div class="col-md-10">
			<form:input path="nouveauEmail" name="nouveauEmail" type="email" value="${fiche.userConsulte.email}" cssClass="form-control"/>
			<font style="color: #C11B17;"><form:errors path="nouveauEmail"/></font>
		</div>
	
	</div>

	<div class="form-group">
		<form:label path="bio" cssClass="col-md-2 control-label">
			<is:getProperty key="label.bio" />
			
		
		</form:label>	
	
		<div class="col-md-10">
			<form:textarea path="bio" name="bio" value="${fiche.profilNuxeo.bio}" rows="3" cols="80" cssClass="form-control"/>
				
		</div>
	</div>
	
	
	
	<div class="form-group"> 	
		<form:label path="departementCns" cssClass="col-md-2 control-label">
			<is:getProperty key="label.departementCns" />
			
		
		</form:label>
	
		<div class="col-md-10">
			<form:select path="departementCns" name="departementCns" cssClass="form-control" items="${fiche.listeDptCns}"/>
			<font style="color: #C11B17;"><form:errors path="departementCns"/></font>
		</div>
	</div>
	<div class="form-group"> 	
		<form:label path="entiteAdm" cssClass="col-md-2 control-label">
			<is:getProperty key="label.entiteAdm" />
			
	
		</form:label>
	
		<div class="col-md-10">
			<form:input path="entiteAdm" name="entiteAdm" value="${fiche.profilNuxeo.entiteAdm}" cssClass="form-control"/>
			<font style="color: #C11B17;"><form:errors path="entiteAdm"/></font>
		</div>
	</div>
	<div class="form-group"> 	
		<form:label path="telFixe" cssClass="col-md-2 control-label">
			<is:getProperty key="label.telFixe" />
			
	
		</form:label>
	
		<div class="col-md-10">
			<form:input path="telFixe" name="telFixe" value="${fiche.profilNuxeo.telFixe}" cssClass="form-control"/>
			<font style="color: #C11B17;"><form:errors path="telFixe"/></font>
		</div>
	</div>
	<div class="form-group"> 	
		<form:label path="telMobile" cssClass="col-md-2 control-label">
			<is:getProperty key="label.telMobile" />
		
		</form:label>
	
		<div class="col-md-10">
			<form:input path="telMobile" name="telMobile" value="${fiche.profilNuxeo.telMobile}" cssClass="form-control"/>
			<font style="color: #C11B17;"><form:errors path="telMobile"/></font>
		</div>
	</div>
	<div class="form-group"> 	
		<form:label path="mailGenerique" cssClass="col-md-2 control-label">
			<is:getProperty key="label.mailGenerique" />
	
		</form:label>
	
		<div class="col-md-10">
			<form:input path="mailGenerique" name="mailGenerique" value="${fiche.profilNuxeo.mailGenerique}" cssClass="form-control"/>
			<font style="color: #C11B17;"><form:errors path="mailGenerique"/></font>
		</div>
	</div>				
	<div class="form-group"> 	
		<form:label path="referent" cssClass="col-md-2 control-label">
			<is:getProperty key="label.referent" />
		
		</form:label>
	
		<div class="col-md-10">
			<form:input path="referent" name="referent" value="${fiche.profilNuxeo.referent}" cssClass="form-control"/>
			<font style="color: #C11B17;"><form:errors path="referent"/></font>
		</div>
	</div>	
	
	


	<div class="">
		<div class="col-md-2"></div>
		<div class="col-md-10 no-ajax-link">
			<button class="btn btn-primary" onclick="form.submit"> 
				<i class="glyphicons ok_2"></i>
				<span><is:getProperty key="label.btn.ok" /></span>
			</button>
			
			<input class="btn btn-default" type="button" id="annuler" value="<is:getProperty key="label.btn.back" />" onclick="updatePortletContent(this,'${annuler}');" />
				
		</div>
	</div>


</form:form>

	