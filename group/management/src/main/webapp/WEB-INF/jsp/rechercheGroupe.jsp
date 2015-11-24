<%@ page contentType="text/plain; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/include.jsp"%>
<%@ include file="/WEB-INF/jsp/tabs.jsp"%>


<portlet:defineObjects />


<portlet:actionURL var="rechercherGroupe">
	<portlet:param name="action" value="rechercheGroupe" />
</portlet:actionURL>

<form:form method="post" modelAttribute="formulaire" action="${rechercherGroupe}" class="form-horizontal">

<div class="form-group">
		
		<div class="col-sm-8 col-sm-offset-2">
						
				<div class="input-group">
				      <input type="text" name="filtre" id="filtre" class="form-control" value="${formulaire.filtre}" placeholder="<is:getProperty key="label.filtre" />">
				      <span class="input-group-btn">
				      	<button class="btn btn-default" type="submit"><i class="glyphicons search"></i></button>
				      </span>
			    </div>	
    
    	</div>
    	
</div>
    

</form:form>



<div class="table-responsive">
	<display:table id="groupeUrl" name="listeGroupeUrl">
		<display:column titleKey="label.tbl.group" sortable="true">
			<i class="glyphicons group"></i> <a title="${groupeUrl.profil.description}" href="${groupeUrl.url}" class="no-ajax-link">${groupeUrl.profil.displayName}</a>
		</display:column>
	</display:table>
</div>




