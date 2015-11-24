<%@ include file="/WEB-INF/jsp/include.jsp"%>
<%@ page contentType="text/plain; charset=UTF-8"%>


<portlet:defineObjects />
<portlet:renderURL var="demandeCreation"> <portlet:param name="action" value="demandeCreation" /></portlet:renderURL>




<h3>Liste des espaces de travail que j'anime</h3>


<div class="no-ajax-link">

	<div class="table-responsive">
			<display:table name="mesWorkspaces" id="wks" pagesize="10">
				<display:column title="Espaces" sortable="true">



					<a href="<portlet:renderURL>
								<portlet:param name="action" value="consulterEspace"/>
         						<portlet:param name="path" value="${wks.path}"/>
		         			</portlet:renderURL>" 								  
				 		title="${wks.description}">${wks.nom}
				</a>			
	
				</display:column>
				<display:column title="Modifier" >
					<c:if test="${wks.modifiableByUser}">
						<a href="<portlet:renderURL>
										<portlet:param name="action" value="modifier"/>
		         						<portlet:param name="path" value="${wks.path}"/>
		         						<portlet:param name="provenance" value="listeWorkspace"/>
				         		</portlet:renderURL>" 								  
						 	title="Modifier les informations de l'espace"><i class="glyphicons halflings pencil"> </i>
						</a>
					</c:if>
				
				</display:column>
				
				<display:column title="Voir les membres" >
								
					<a href="<portlet:renderURL>
									<portlet:param name="action" value="consulterRole"/>
	         						<portlet:param name="path" value="${wks.path}"/>
			         		</portlet:renderURL>" 								  
					 		title="Consulter les rôles de l'espace"><i class="glyphicons group"> </i>
					</a>	
				</display:column>
			</display:table>
	</div>
		

</div>	
	


<c:if test="${createEnable}">
	
     <a href="${demandeCreation}" class="btn btn-primary">
		<i class="glyphicons halflings plus"></i>
		<span><is:getProperty key="label.creerEspace" /></span>
	</a>
		
</c:if>
	

		
		