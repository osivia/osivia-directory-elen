<%@ include file="/WEB-INF/jsp/include.jsp"%>
<%@ include file="/WEB-INF/jsp/tabs.jsp"%>
<%@ page contentType="text/plain; charset=UTF-8"%>
<%@ taglib prefix="tg" tagdir="/WEB-INF/tags"%>

<portlet:defineObjects />

<portlet:actionURL var="rechercherPersonne" name="rechercherPersonne" />




<form:form method="post" modelAttribute="formulaire"
	action="${rechercherPersonne}" cssClass="form-horizontal">


	<div class="form-group">
		<form:label path="filtreNom" cssClass="col-md-4 control-label">
			<op:translate  key="label.filtreNom" />
		</form:label>

		<div class="col-md-8">
			<form:input path="filtreNom" cssClass="form-control" />
		</div>
	</div>


	<div class="form-group">
		<form:label path="filtreDptCns" cssClass="col-md-4 control-label">
			<op:translate  key="label.filtreDptCns" />
		</form:label>

		<div class="col-md-8">
			<form:select path="filtreDptCns" name="filtreDptCns" cssClass="form-control">
				<form:option value="">-- Tous --</form:option>
				<form:options items="${formulaire.listeDptCns}"/>
			</form:select>
		</div>
	</div>

	<div class="row">
		<div class="col-md-4"></div>

		<div class="col-md-8">
			<div class="btn-toolbar">
				<button class="btn btn-primary" onclick="form.submit">
					<i class="glyphicons search"></i> <span><op:translate 
							key="btn.submit" /></span>
				</button>
			</div>
		</div>

	</div>


	<div class="row">
		<div class="col-md-12">

			<display:table id="personUrl"
				name="formulaire.listePersonnesRecherchees" pagesize="10">

				<display:column titleKey="label.tbl.name" sortable="true" sortProperty="personne.sn">

					<img src="${personUrl.avatar.url}" alt="" class="avatar">
					<a title="${fn:toUpperCase(personUrl.personne.sn)} ${personUrl.personne.givenName}" href="${personUrl.url}"
						class="no-ajax-link">${fn:toUpperCase(personUrl.personne.sn)} ${personUrl.personne.givenName}</a>
				</display:column>


				<c:if test="${config.enableDeletion}">
					<display:column>

						<portlet:actionURL var="deletion">
							<portlet:param name="action" value="deleteUser" />
							<portlet:param name="uid" value="${personUrl.personne.uid}" />
						</portlet:actionURL>

						<a class="btn btn-default fancybox_inline no-ajax-link"
							href="#deleteUserDiv"
							onclick="prepareDeletion('deleteUserConfirmedBtn','${deletion}')">
							<i class="glyphicons user_remove"></i>
						</a>

					</display:column>
				</c:if>



			</display:table>
		</div>
	</div>

	<div class="row">
		<div class="col-md-12">

			<c:if test='${config.enableCreation}'>
				<a href="${urlCreation}" class="btn  btn-primary  no-ajax-link">
					<i class="glyphicons user_add"></i> <span><op:translate 
							key="btn.useradd" /></span>
				</a>
			</c:if>

		</div>
	</div>

	<div id="deleteUserDiv" style="display: none">

		<div class="container-fluid text-center" id="delete_user">

			<p>
				<op:translate  key="confirm.delete" />
			</p>

			<a id="deleteUserConfirmedBtn" class="btn btn-default btn-warning"
				href=""> <i class="glyphicons halflings warning-sign"></i> <span
				class="hidden-xs"><op:translate  key="YES" /></span>
			</a>
			<button class="btn btn-default" type="button"
				onclick="closeFancybox()">
				<op:translate  key="NO" />
			</button>
		</div>
	</div>
</form:form>


