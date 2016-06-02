<%@ page contentType="text/plain; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/include.jsp"%>

<portlet:defineObjects />

<portlet:renderURL var="modify">
	<portlet:param name="action" value="modify" />
</portlet:renderURL>
<portlet:renderURL var="chgtEmail">
	<portlet:param name="action" value="chgtEmail" />
</portlet:renderURL>
<portlet:renderURL var="chgtMdp">
	<portlet:param name="action" value="chgtMdp" />
</portlet:renderURL>

<div class="form-horizontal">

	<div class="form-group">
		<div class="col-md-2">
			<img src="${fiche.avatar.url}" alt="avatar"
				class="avatar avatar-big pull-right" />
		</div>
		<div class="col-md-10">
			
		
		
			<h1>
				<c:if test="${fiche.userConsulte.title}">
					${fiche.userConsulte.title}  
				</c:if>
			
				${fiche.userConsulte.displayName}  

				<c:if test="${fiche.levelConsultation.showLogin}">
					<small>(${fiche.idConsulte})</small>
				</c:if>
			</h1>
		</div>
	</div>


	<div class="form-group">
		<label class="col-md-2 control-label">
			<op:translate  key="label.title" />
		</label>
		<div class="col-md-10">
			<p class="form-control-static">${fiche.userConsulte.title}</p>
		</div>
	</div>
	<div class="form-group">
		<label class="col-md-2 control-label">
			<op:translate  key="label.sn" />
		</label>
		<div class="col-md-10">
			<p class="form-control-static">${fiche.userConsulte.sn}</p>
		</div>
	</div>
	<div class="form-group">
		<label class="col-md-2 control-label">
			<op:translate  key="label.givenName" />
		</label>
		<div class="col-md-10">
			<p class="form-control-static">${fiche.userConsulte.givenName}</p>
		</div>
	</div>
    
    <div class="form-group">
        <label class="col-md-2 control-label"><op:translate key="label.profession" /></label>
        <div class="col-md-10">
            <p class="form-control-static">${fiche.profilNuxeo.profession}</p>
        </div>
    </div>

	<div class="form-group">
		<label class="col-md-2 control-label">
			<op:translate  key="label.telFixe" />
			
		</label>
		<div class="col-md-10">
			<p class="form-control-static">${fiche.profilNuxeo.telFixe}</p>
		</div>
	</div>
	<div class="form-group">
		<label class="col-md-2 control-label">
			<op:translate  key="label.telMobile" />
		</label>
		<div class="col-md-10">
			<p class="form-control-static">${fiche.profilNuxeo.telMobile}</p>
		</div>
	</div>
	<c:if test="${fiche.levelConsultation.showMail}">
		<div class="form-group">
			<label class="col-md-2 control-label">
				<op:translate  key="label.mail" />
			</label>
			<div class="col-md-10"><p class="form-control-static">${fiche.userConsulte.email}</p></div>
		</div>
	</c:if>	


</div>


<div class="row">
	<div class="col-md-offset-2 col-md-10">

		<div class="btn-toolbar no-ajax-link">

			<a class="btn btn-primary" href="${modify}"> <i
				class="glyphicons pencil"></i> <span><op:translate 
						key="label.btn.edit" /></span>
			</a>

			<a class="btn btn-default" href="${chgtMdp}"> <i
				class="glyphicons keys"></i> <span><op:translate 
						key="label.btn.chgpwd" /></span>
			</a>
		</div>

	</div>


</div>

