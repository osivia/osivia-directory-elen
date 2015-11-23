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
			<is:getProperty key="label.title" />
		</label>
		<div class="col-md-10">
			<p class="form-control-static">${fiche.userConsulte.title}</p>
		</div>
	</div>
	<div class="form-group">
		<label class="col-md-2 control-label">
			<is:getProperty key="label.sn" />
		</label>
		<div class="col-md-10">
			<p class="form-control-static">${fiche.userConsulte.sn}</p>
		</div>
	</div>
	<div class="form-group">
		<label class="col-md-2 control-label">
			<is:getProperty key="label.givenName" />
		</label>
		<div class="col-md-10">
			<p class="form-control-static">${fiche.userConsulte.givenName}</p>
		</div>
	</div>

	<c:if test="${fiche.levelConsultation.showBio}">
		<div class="form-group">
			<label class="col-md-2 control-label">
				<is:getProperty key="label.bio" />
						
			</label>
			<div class="col-md-10">
				<p class="form-control-static">${fiche.profilNuxeo.bio}</p>
			</div>
		</div>
	</c:if>

	<div class="form-group">
		<label class="col-md-2 control-label">
			<is:getProperty key="label.departementCns" />
			
		
		</label>
		<div class="col-md-10">
			<p class="form-control-static">${fiche.departementCns}</p>
		</div>
	</div>		
	<div class="form-group">
		<label class="col-md-2 control-label">
			<is:getProperty key="label.entiteAdm" />
		</label>
		<div class="col-md-10">
			<p class="form-control-static">${fiche.profilNuxeo.entiteAdm}</p>
		</div>
	</div>
	

	<div class="form-group">
		<label class="col-md-2 control-label">
			<is:getProperty key="label.telFixe" />
			
		</label>
		<div class="col-md-10">
			<p class="form-control-static">${fiche.profilNuxeo.telFixe}</p>
		</div>
	</div>
	<div class="form-group">
		<label class="col-md-2 control-label">
			<is:getProperty key="label.telMobile" />
		</label>
		<div class="col-md-10">
			<p class="form-control-static">${fiche.profilNuxeo.telMobile}</p>
		</div>
	</div>
	<c:if test="${fiche.levelConsultation.showMail}">
		<div class="form-group">
			<label class="col-md-2 control-label">
				<is:getProperty key="label.mail" />
			</label>
			<div class="col-md-10"><p class="form-control-static">${fiche.userConsulte.email}</p></div>
		</div>
	</c:if>	


	<div class="form-group">
		<label class="col-md-2 control-label">
			<is:getProperty key="label.mailGenerique" />
		</label>
		<div class="col-md-10">
			<p class="form-control-static">${fiche.profilNuxeo.mailGenerique}</p>
		</div>
	</div>
	<div class="form-group">
		<label class="col-md-2 control-label">
			<is:getProperty key="label.referent" />
		</label>
		<div class="col-md-10">
			<p class="form-control-static">${fiche.profilNuxeo.referent}</p>
		</div>
	</div>

	<c:if
		test="${fiche.levelUserConnecteSurcharge == 'DROIT_SURCHARGE' || fiche.levelUserConnecteSurcharge == 'DROIT_SURCHARGE_ASSISTANCE' || fiche.levelUserConnecteSurcharge == 'NON_SURCHARGEABLE'}">
		<div class="form-group">
			<label class="col-md-2 control-label">
				<is:getProperty key="label.profile" />
				(
				<c:out value="${fn:length(fiche.userConsulte.listeProfils)}" />
				)

				<i class="glyphicons glyphicons-life-preserver" 
					data-toggle="tooltip" data-container="body" data-original-title="<is:getProperty key="label.security.help" />"
					title="<is:getProperty key="label.security.help" />"></i>
				
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
					class="glyphicons pencil"></i> <span><is:getProperty
							key="label.btn.edit" /></span>
				</a>

			</c:if>
<!--
			<c:if test="${fiche.levelUserConnecteModifPwdMail == 'DROITMODIF'}">
				<a class="btn btn-default" href="${chgtMdp}"> <i
					class="glyphicons keys"></i> <span><is:getProperty
							key="label.btn.chgpwd" /></span>
				</a>
			</c:if>
-->

			<c:if test="${(fiche.levelUserConnecteRazMdp == 'DROITRAZ')}">
				<a class="btn btn-default fancybox_inline no-ajax-link"
					href="#razMdpDiv"> <span><is:getProperty
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
							class="glyphicons keys"></i> <span><is:getProperty
									key="label.btn.surcharge" /></span>
						</a>
					</c:when>

					<c:when
						test="${fiche.levelUserConnecteSurcharge == 'DROIT_SURCHARGE_ASSISTANCE'}">
						<a class="btn btn-primary fancybox_inline no-ajax-link"
							href="#assistanceSurchargeDiv"> <i class="glyphicons keys"></i>
							<span><is:getProperty key="label.btn.surcharge" /></span>
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
				<is:getProperty key="confirm.surcharge" />
			</p>

			<a id="assistanceSurchargeConfirmedBtn"
				class="btn btn-default btn-warning" href="${surcharge}"> <i
				class="glyphicons halflings warning-sign"></i> <span
				class="hidden-xs"><is:getProperty key="YES" /></span>
			</a>
			<button class="btn btn-default" type="button"
				onclick="closeFancybox()">
				<is:getProperty key="NO" />
			</button>
		</div>
	</div>
</c:if>
<c:if test="${(fiche.levelUserConnecteRazMdp == 'DROITRAZ')}">
	<div id="razMdpDiv" style="display: none">
		<div class="container-fluid text-center" id="razMdp">

			<p>
				<is:getProperty key="confirm.razMdp" />
			</p>

			<a id="razMdpConfirmBtn" class="btn btn-default btn-warning"
				href="${razMdp}"> <i class="glyphicons halflings warning-sign"></i>
				<span class="hidden-xs"><is:getProperty key="YES" /></span>
			</a>
			<button class="btn btn-default" type="button"
				onclick="closeFancybox()">
				<is:getProperty key="NO" />
			</button>
		</div>
	</div>
</c:if>