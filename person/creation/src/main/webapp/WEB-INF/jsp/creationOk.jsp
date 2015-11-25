<%@ include file="/WEB-INF/jsp/include.jsp"%>
<%@ page contentType="text/plain; charset=UTF-8"%>

<portlet:defineObjects />

<portlet:renderURL var="creerPersonne"><portlet:param name="action" value="creerPersonne" /></portlet:renderURL>

	<div class="row no-ajax-link">
		<div class="col-md-4"></div>
		<div class="col-md-8">
			<div class="btn-toolbar">
				<a class="btn btn-default" href="${creerPersonne}">
					<i class="glyphicons user_add"></i>
					
					<span><op:translate  key="label.btn.create" /></span>
				</a>
				
				<a class="btn btn-primary" href="${urlFichePersonne}">
					<i class="glyphicons user"></i>
					<span><op:translate  key="label.btn.edit" /></span>
				</a>				
			</div>
		
		</div>	
	</div>
	
	
</div>