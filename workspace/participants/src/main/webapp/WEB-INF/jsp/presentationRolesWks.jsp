<%@ include file="/WEB-INF/jsp/include.jsp"%>
<%@ page contentType="text/plain; charset=UTF-8"%>
<portlet:defineObjects />


<portlet:renderURL windowState="maximized" var="maximizedURL" />


<div class="row">

	<c:forEach items="${workspace.membresGroupeAnimateurs}" var="prsAnimateur" varStatus="vs">
		<div class="col-sm-${sm} col-md-${md}">
			
			<a href="${prsAnimateur.url}" class="thumbnail no-ajax-link">
			
				<img src="${prsAnimateur.avatar.url}" alt="${prsAnimateur.nom}">
				
				<span class="text-overflow text-muted admins-icon" style="">
					<i class="glyphicons glyphicons-sheriffs-star"> </i>
				</span>
				
				<span class="thumbnail-legend text-overflow small" title="${prsAnimateur.nom}" data-toggle="tooltip" data-placement="bottom">
					${prsAnimateur.nom}
				</span>
			
			
			</a>
		</div>
		
		<c:if test="${(vs.count % thumbnailPerLine) == 0}">
			 <div class="clearfix"></div>
		</c:if>

	</c:forEach>

</div>

<div class="row">

	<c:forEach items="${workspace.membresGroupeContributeurs}" var="prsContributeur" varStatus="vs">
		<div class="col-sm-${sm} col-md-${md}">
			
			<a href="${prsContributeur.url}" class="thumbnail no-ajax-link">
			
				<img src="${prsContributeur.avatar.url}" alt="${prsContributeur.nom}">

				<span class="thumbnail-legend text-overflow small" title="${prsContributeur.nom}" data-toggle="tooltip" data-placement="bottom">
					${prsContributeur.nom}
				</span>
			
			</a>
		</div>
		
		<c:if test="${(vs.count % thumbnailPerLine) == 0}">
			 <div class="clearfix"></div>
		</c:if>

	</c:forEach>
</div>

<c:if test="${allMembers == false}"> 
	<p>
		<a href="${allMembersURL}" class="btn btn-default">
		
			<i class="glyphicons glyphicons-more"> </i>
			
			<span>
				<is:getProperty key="SHOW_MEMBERS" />
			</span>
		</a>
	</p>
</c:if>


