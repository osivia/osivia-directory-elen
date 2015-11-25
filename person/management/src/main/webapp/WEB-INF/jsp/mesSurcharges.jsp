<%@ include file="/WEB-INF/jsp/include.jsp"%>
<%@ include file="/WEB-INF/jsp/tabs.jsp"%>
<%@ page contentType="text/plain; charset=UTF-8"%>
<%@ taglib prefix="tg" tagdir="/WEB-INF/tags" %>

<portlet:defineObjects />

<div class="row">
	<div class="col-md-12">
		<p class="text-right">

			<portlet:actionURL var="refresh" name="refresh">
				<portlet:param name="source" value="mesSurcharges" />
			</portlet:actionURL>

			<a class="btn btn-default no-ajax-link" href="${refresh}"> <i class="glyphicons refresh"></i> 
			<op:translate  key="btn.refresh" /></a>
		</p>
	</div>
</div>


<div class="row">
	<div class="col-md-12">

		<display:table id="personUrl" name="formulaire.listeMesSurcharges" pagesize="10">
		
			<display:column titleKey="label.tbl.name" sortable="true">
				<i class="glyphicons user"></i>
				<a title="${personUrl.personne.cn}" href="${personUrl.url}"
					class="no-ajax-link">${personUrl.personne.cn}
					(${personUrl.personne.uid})</a>
			</display:column>
			
			<display:column titleKey="label.tbl.idSurcharge">
				${personUrl.personne.uid}
			</display:column>
		
			<display:column titleKey="label.tbl.motif">
				<c:if test="${personUrl.personne.typeSurcharge == 'SUPERADMIN'}">
					<i class="glyphicons sheriffs_star" title="<op:translate  key="label.role.sadmin" />"></i>
				</c:if>
				<c:if test="${personUrl.personne.typeSurcharge == 'ADMIN'}">
					<i class="glyphicons cogwheel" title="<op:translate  key="label.role.admin" />"></i>
				</c:if>
				<c:if test="${personUrl.personne.typeSurcharge == 'ASSISTANCE'}">
					<i class="glyphicons life_preserver" title="<op:translate  key="label.role.assistance" />"></i>
				</c:if>
			
				<i class="glyphicons circle_info" title="${personUrl.personne.motifSurcharge}"></i>
			</display:column>
			
			<display:column>
      				
				<portlet:actionURL var="deletion">
					<portlet:param name="action" value="deleteMaSurcharge" />
					<portlet:param name="uid" value="${personUrl.personne.uid}" />
				</portlet:actionURL>

				<a class="fancybox_inline no-ajax-link"
					href="#deleteSurchargeDiv" onclick="prepareDeletion('deleteSurchargeConfirmedBtn','${deletion}')">
					<i class="glyphicons circle_remove"></i>
				</a>      				
			</display:column>
		
		</display:table>
		
	</div>
</div>		


<div id="deleteSurchargeDiv" style="display: none">

	<div class="container-fluid text-center no-ajax-link" id="delete_surcharge">

		<p>
			<op:translate  key="confirm.deleteSurcharge" />
		</p>

		<a id="deleteSurchargeConfirmedBtn" class="btn btn-default btn-warning"
			href=""> <i class="glyphicons halflings warning-sign"></i> <span
			class="hidden-xs"><op:translate  key="YES" /></span>
		</a>
		<button class="btn btn-default" type="button"
			onclick="closeFancybox()">
			<op:translate  key="NO" />
		</button>
	</div>
</div>
