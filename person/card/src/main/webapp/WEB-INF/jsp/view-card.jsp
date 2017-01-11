<%@ page contentType="text/plain; charset=UTF-8"%>

<%@ include file="/WEB-INF/jsp/include.jsp"%>

<portlet:defineObjects />

<portlet:actionURL name="modify" var="modifyUrl">
</portlet:actionURL>
<portlet:actionURL name="chgPwd" var="chgPwdUrl">
</portlet:actionURL>
<portlet:actionURL name="delete" var="deleteUrl">
    <portlet:param name="controller" value="edit" />
</portlet:actionURL>



<div class="portlet-filler container-fluid">
	<div class="form-horizontal">

		<div class="form-group">
			<div class="col-md-2">
				<img src="${card.avatar.url}" alt="avatar"
					class="avatar avatar-big pull-right" />
			</div>
			<div class="col-md-10">



				<h1>
					<c:if test="${card.userConsulte.title}">
						${card.userConsulte.title}  
					</c:if>

					${card.userConsulte.displayName}
				</h1>
			</div>
		</div>


		<div class="form-group">
			<label class="col-md-2 control-label"> <op:translate
					key="label.title" />
			</label>
			<div class="col-md-10">
				<p class="form-control-static">${card.userConsulte.title}</p>
			</div>
		</div>
		<div class="form-group">
			<label class="col-md-2 control-label"> <op:translate
					key="label.sn" />
			</label>
			<div class="col-md-10">
				<p class="form-control-static">${card.userConsulte.sn}</p>
			</div>
		</div>
		<div class="form-group">
			<label class="col-md-2 control-label"> <op:translate
					key="label.givenName" />
			</label>
			<div class="col-md-10">
				<p class="form-control-static">${card.userConsulte.givenName}</p>
			</div>
		</div>

		<div class="form-group">
			<label class="col-md-2 control-label"><op:translate
					key="label.occupation" /></label>
			<div class="col-md-10">
				<p class="form-control-static">${card.nxProfile.occupation}</p>
			</div>
		</div>

		<div class="form-group">
			<label class="col-md-2 control-label"> <op:translate
					key="label.phone" />

			</label>
			<div class="col-md-10">
				<p class="form-control-static">${card.nxProfile.phone}</p>
			</div>
		</div>
		<div class="form-group">
			<label class="col-md-2 control-label"> <op:translate
					key="label.mobilePhone" />
			</label>
			<div class="col-md-10">
				<p class="form-control-static">${card.nxProfile.mobilePhone}</p>
			</div>
		</div>

		<div class="form-group">
			<label class="col-md-2 control-label"> <op:translate
					key="label.mail" />
			</label>
			<div class="col-md-10">
				<p class="form-control-static">${card.userConsulte.mail}</p>
			</div>
		</div>



	</div>

	<!-- Buttons -->
	<div class="portlet-toolbar adapt-scrollbar">
		<c:if test="${card.levelEdition == 'ALLOW'}">
			<div class="row">
				<div class="col-md-offset-2 col-md-10">

					<div class="btn-toolbar no-ajax-link">

						<a class="btn btn-primary" href="${modifyUrl}"> <i
							class="glyphicons pencil"></i> <span><op:translate
									key="label.btn.edit" /></span>
						</a> <a class="btn btn-default" href="${chgPwdUrl}"> <i
							class="glyphicons keys"></i> <span><op:translate
									key="label.btn.chgpwd" /></span>
						</a>
						
						<c:if test="${card.levelDeletion == 'ALLOW'}">
							<div class="pull-right">
								<!-- Delete -->
								<button type="button" class="btn btn-danger" data-toggle="modal"
									data-target="#${namespace}-delete-modal">
									<i class="glyphicons glyphicons-bin"></i> <span><op:translate
											key="DELETE" /></span>
								</button>
							</div>
						</c:if>						
					</div>
				</div>
			</div>
		</c:if>

		<c:if test="${card.levelDeletion == 'ALLOW'}">
			<div id="${namespace}-delete-modal" class="modal fade" role="dialog">
				<div class="modal-dialog modal-sm">
					<div class="modal-content">
						<div class="modal-header">
							<button type="button" class="close" data-dismiss="modal">
								<i class="glyphicons glyphicons-remove"></i> <span
									class="sr-only"><op:translate key="CLOSE" /></span>
							</button>

							<h4 class="modal-title">
								<op:translate key="DELETE_PERSON_MODAL_TITLE" />
							</h4>
						</div>

						<div class="modal-body">
							<p>
								<op:translate key="DELETE_PERSON_MODAL_MESSAGE" />
							</p>
						</div>

						<div class="modal-footer">
							<a href="${deleteUrl}" class="btn btn-danger"
								data-dismiss="modal"> <i class="glyphicons glyphicons-bin"></i>
								<span><op:translate key="DELETE" /></span>
							</a>

							<button type="button" class="btn btn-default"
								data-dismiss="modal">
								<span><op:translate key="CANCEL" /></span>
							</button>
						</div>
					</div>
				</div>
			</div>
		</c:if>
	</div>
</div>