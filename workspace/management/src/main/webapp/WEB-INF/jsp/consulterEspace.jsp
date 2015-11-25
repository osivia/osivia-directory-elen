<%@ include file="/WEB-INF/jsp/include.jsp"%>
<%@ page contentType="text/plain; charset=UTF-8"%>



<portlet:defineObjects />
<portlet:renderURL var="modifier"><portlet:param name="action" value="modifier" />
									<portlet:param name="path" value="${workspace.path}" />
									<portlet:param name="provenance" value="consulterEspace"/>
</portlet:renderURL>
<portlet:renderURL var="consulterRole"><portlet:param name="action" value="consulterRole" />
									<portlet:param name="path" value="${workspace.path}" />
</portlet:renderURL>
<portlet:renderURL var="retour"><portlet:param name="action" value="retourListe" /></portlet:renderURL>




<h3>${workspace.nom} (${workspace.shortname})</h3>


<div class="form-horizontal">


	<div class="form-group">
		<label class="col-sm-4 control-label"><op:translate  key="label.yAcceder" /></label>
		<div class="col-sm-8 no-ajax-link form-control-static">
			<a href="${workspace.url}" title="${workspace.description}">${workspace.nom}</a>
		</div>
	</div>
	
	<div class="form-group">
		<label class="col-sm-4 control-label"><op:translate  key="label.permalien" /></label>
		<div class="col-sm-8">
			<textarea class="col-sm-12" rows="3">${workspace.permaLink}</textarea>
			<p class="text-muted">(lien permettant au personnes habilitées d'accéder directement à l'espace de travail)</p>
		</div>
	</div>

		


	<div class="form-group">
		<div class="col-sm-8 col-sm-offset-4">
			<a href="${modifier}" class="btn btn-primary"> <i class="glyphicons halflings pencil"></i> <span><op:translate  key="label.btn.modifEspace" /></span></a>
			<a href="${consulterRole}" class="btn btn-primary"><i class="glyphicons group"></i> <span><op:translate  key="label.btn.consulterRole"/></span> </a>
			<a href="${retour}" class="btn btn-primary"><i class="glyphicons halflings step-backward"></i> <span><op:translate  key="label.btn.retour" /></span> </a>
		</div>		
	</div>	

</div>	



	
	
