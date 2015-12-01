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
<portlet:renderURL var="surcharge">
	<portlet:param name="action" value="surchargeMdp" />
</portlet:renderURL>
<portlet:actionURL var="razMdp">
	<portlet:param name="action" value="razMdp" />
</portlet:actionURL>

<div class="form-horizontal">



	<div class="form-group">
		<div class="col-md-2">
			<img src="${fiche.avatar.url}" alt="avatar"
				class="avatar avatar-big pull-right" />
		</div>
		<div class="col-md-10">
			
		
		
			<h1>
				<c:if test="${fiche.userConsulte.personalTitle}">
					${fiche.userConsulte.personalTitle}  
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
		<label class="col-md-2 control-label">
			<op:translate  key="label.departementCns" />		
		</label>
		<div class="col-md-10">
			<p class="form-control-static">${fiche.departementCns}</p>
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



	<c:if
		test="${fiche.levelUserConnecteSurcharge == 'DROIT_SURCHARGE' || fiche.levelUserConnecteSurcharge == 'DROIT_SURCHARGE_ASSISTANCE' || fiche.levelUserConnecteSurcharge == 'NON_SURCHARGEABLE'}">
		<div class="form-group">
			<label class="col-md-2 control-label">
				<op:translate  key="label.profile" />
				(
				<c:out value="${fn:length(fiche.userConsulte.listeProfils)}" />
				)

				<i class="glyphicons glyphicons-life-preserver" 
					data-toggle="tooltip" data-container="body" data-original-title="<op:translate  key="label.security.help" />"
					title="<op:translate  key="label.security.help" />"></i>
				
			</label>
			<div class="col-md-10 no-ajax-link">

				<display:table id="p" name="listeProfilsUrl" pagesize="5">
					<display:column titleKey="label.tbl.profiles">
						<a href="${p.url}" class="no-ajax-link">${p.profil.displayName}
							(${p.profil.cn})</a>
					</display:column>

				</display:table>

			</div>
		</div>
	</c:if>

</div>


<div class="row">
	<div class="col-md-offset-2 col-md-10">

		<div class="btn-toolbar no-ajax-link">



			<c:if
				test="${fiche.self || fiche.levelUserConnecteModifFiche == 'DROITMODIF'}">

				<a class="btn btn-primary" href="${modify}"> <i
					class="glyphicons pencil"></i> <span><op:translate 
							key="label.btn.edit" /></span>
				</a>

			</c:if>

			<c:if test="${fiche.levelUserConnecteModifPwdMail == 'DROITMODIF'}">
				<a class="btn btn-default" href="${chgtMdp}"> <i
					class="glyphicons keys"></i> <span><op:translate 
							key="label.btn.chgpwd" /></span>
				</a>
			</c:if>

			<c:if test="${(fiche.levelUserConnecteRazMdp == 'DROITRAZ')}">
				<a class="btn btn-default fancybox_inline no-ajax-link"
					href="#razMdpDiv"> <span><op:translate 
							key="label.btn.razMdp" /></span>
				</a>
			</c:if>

			<c:if test="${config.enableOverload}">

				<c:choose>

					<c:when test="${fiche.self}">
					</c:when>


					<c:when
						test="${fiche.levelUserConnecteSurcharge == 'DROIT_SURCHARGE'}">
						<a class="btn btn-primary" href="${surcharge}"> <i
							class="glyphicons keys"></i> <span><op:translate 
									key="label.btn.surcharge" /></span>
						</a>
					</c:when>

					<c:when
						test="${fiche.levelUserConnecteSurcharge == 'DROIT_SURCHARGE_ASSISTANCE'}">
						<a class="btn btn-primary fancybox_inline no-ajax-link"
							href="#assistanceSurchargeDiv"> <i class="glyphicons keys"></i>
							<span><op:translate  key="label.btn.surcharge" /></span>
						</a>
					</c:when>


				</c:choose>

			</c:if>
		</div>

	</div>


</div>

<c:if
	test="${fiche.levelUserConnecteSurcharge == 'DROIT_SURCHARGE_ASSISTANCE' }">
	<div id="assistanceSurchargeDiv" style="display: none">
		<div class="container-fluid text-center" id="assistanceSurcharge">

			<p>
				<op:translate  key="confirm.surcharge" />
			</p>

			<a id="assistanceSurchargeConfirmedBtn"
				class="btn btn-default btn-warning" href="${surcharge}"> <i
				class="glyphicons halflings warning-sign"></i> <span
				class="hidden-xs"><op:translate  key="YES" /></span>
			</a>
			<button class="btn btn-default" type="button"
				onclick="closeFancybox()">
				<op:translate  key="NO" />
			</button>
		</div>
	</div>
</c:if>
<c:if test="${(fiche.levelUserConnecteRazMdp == 'DROITRAZ')}">
	<div id="razMdpDiv" style="display: none">
		<div class="container-fluid text-center" id="razMdp">

			<p>
				<op:translate  key="confirm.razMdp" />
			</p>

			<a id="razMdpConfirmBtn" class="btn btn-default btn-warning"
				href="${razMdp}"> <i class="glyphicons halflings warning-sign"></i>
				<span class="hidden-xs"><op:translate  key="YES" /></span>
			</a>
			<button class="btn btn-default" type="button"
				onclick="closeFancybox()">
				<op:translate  key="NO" />
			</button>
		</div>
	</div>
</c:if>