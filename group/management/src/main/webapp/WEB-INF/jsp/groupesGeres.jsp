<%@ page contentType="text/plain; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/include.jsp"%>
<%@ include file="/WEB-INF/jsp/tabs.jsp"%>


<portlet:defineObjects />

<portlet:renderURL var="filtrerGroupesGeresByOrga">
	<portlet:param name="action" value="filtrerGroupesGeresByOrga" />
	<portlet:param name="idOrga" value="SELECTED_TEXT" />
</portlet:renderURL>

<div class="row">
	<div class="col-sm-12">
		<form:form id="formGestionGroupes" name="formGestionGroupes"
			method="post" modelAttribute="formulaire" class="form-horizontal">
			<c:if test="${not formulaire.contexteOrga}">

				<div class="form-group">
					<form:label path="filtreOrga" cssClass="col-md-4 control-label">
						<op:translate  key="label.filtreOrga" />
					</form:label>

					<div class="col-md-8">
						<form:select class="form-control" id="filtreOrga"
							path="filtreOrga"
							onchange="refreshOnChangeGestionOrga(this,'${filtrerGroupesGeresByOrga}');">
							<form:options items="${orgaUserConnecte}" itemValue="id"
								itemLabel="description" />
						</form:select>
					</div>
				</div>

			</c:if>

		</form:form>
	</div>
</div>
<div class="row">
	<div class="col-sm-12">
		<div class="table-responsive">
			<display:table id="groupeUrl" name="listeGroupeUrl">
				<display:column titleKey="label.tbl.group" sortable="true">
					<i class="glyphicons group"></i> <a title="${groupeUrl.profil.description}" href="${groupeUrl.url}" class="no-ajax-link">${groupeUrl.profil.displayName}</a>
				</display:column>
			</display:table>
		</div>

	</div>
</div>




