<%@ page contentType="text/plain; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/include.jsp"%>



<portlet:defineObjects />

<c:set var="namespace"><portlet:namespace /></c:set>

<portlet:renderURL var="modifier"><portlet:param name="action" value="modifier" /></portlet:renderURL>
<portlet:renderURL var="retourListe"><portlet:param name="action" value="retourListe" /></portlet:renderURL>
<portlet:renderURL var="retourWks"><portlet:param name="action" value="consulterEspace" /><portlet:param name="path" value="${workspace.path}"/></portlet:renderURL>




<h3>${workspace.nom} (${workspace.shortname})</h3>
	
<div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">

	<div class="panel panel-default">
	
	    <div class="panel-heading" role="tab" id="headingOne">
	      <h4 class="panel-title">
	        <a class="no-ajax-link" data-toggle="collapse" data-parent="#accordion" href="#collapseOne" aria-expanded="true" aria-controls="collapseOne">
						<op:translate  key="label.animateur" />
			</a>
	      </h4>
	    </div>
	    
	    
	    <div id="collapseOne" class="panel-collapse collapse in" role="tabpanel" aria-labelledby="headingOne">
	      	<div class="panel-body">
	       
	       			<c:choose>
						 <c:when test="${empty workspace.membresGroupeAnimateurs && empty workspace.listeProfilAnimateurs}">
								<p class="text-muted">Il n'y a encore aucune personne ni groupe d'associé au rôle <op:translate  key="label.animateurs" /></p>
						</c:when>
						
						<c:otherwise>
							<div class="table-responsive">
								<display:table name="workspace.membresGroupeAnimateurs" id="prsAnimateur" pagesize="10">
									
									<display:column title="Personnes" sortable="true">
										<i class="glyphicons user"> </i>
										${prsAnimateur.cn} (${prsAnimateur.uid})
											
									</display:column>
						
								</display:table>
								
								<c:if test="${config.enableGroups == true}">
									<display:table name="workspace.listeProfilAnimateurs" id="prfAnimateur" pagesize="10">
										
										<display:column title="Groupes" sortable="true">
											<i class="glyphicons group"> </i>
											${prfAnimateur.displayName}
	
										</display:column>
							
									</display:table>
								</c:if>
							</div>	
						</c:otherwise>
					</c:choose>	
					
					<c:if test="${workspace.modifiableByUser}">
							<p class="text-right no-ajax-link">
							       		<a href="<portlet:renderURL>
							       						    <portlet:param name="action" value="modifierRole" />
							       						    <portlet:param name="path" value="${workspace.path}"/>
															<portlet:param name="nomRole" value="animateur"/>
															<portlet:param name="provenance" value="consulterRole"/>
												</portlet:renderURL>"
							       			class="btn btn-default">
											<i class="glyphicons group"> </i>
											<span><op:translate  key="label.gererRoleAnimateur" /></span>
										</a>
							</p>
						</c:if>
	       
	       </div>
	    </div>
	  </div>





<div class="panel panel-default">
    
	    <div class="panel-heading" role="tab" id="headingTwo">
	      	<h4 class="panel-title">
		        <a class="no-ajax-link" data-toggle="collapse" data-parent="#accordion" href="#collapseTwo" aria-expanded="true" aria-controls="collapseTwo">
					<op:translate  key="label.contributeur" />
				</a>
			</h4>	
		</div>	
	
	
		 <div id="collapseTwo" class="panel-collapse collapse in" role="tabpanel" aria-labelledby="headingTwo">
		 
	     	<div class="panel-body">
	 	
			 	<c:choose>
					 <c:when test="${empty workspace.membresGroupeContributeurs && empty workspace.listeProfilContributeurs}">
							<p class="text-muted">Il n'y a encore aucune personne ni groupe d'associé au rôle <op:translate  key="label.contributeurs" /></p>
					</c:when>
					
					<c:otherwise>
						<div class="table-responsive">
							<display:table name="workspace.membresGroupeContributeurs" id="prsContributeur" pagesize="10">
								
								<display:column title="Personnes" sortable="true">
									<i class="glyphicons user"> </i>
									${prsContributeur.cn} (${prsContributeur.uid})
										
								</display:column>
					
							</display:table>
							
							<c:if test="${config.enableGroups == true}">
								<display:table name="workspace.listeProfilContributeurs" id="prfContributeur" pagesize="10">
									
									<display:column title="Groupes" sortable="true">
										<i class="glyphicons group"> </i>
										${prfContributeur.displayName}
											
									</display:column>
						
								</display:table>
							</c:if>
						</div>	
					</c:otherwise>
				</c:choose>	

	
				<c:if test="${workspace.modifiableByUser}">
					<p class="text-right no-ajax-link">
					       		<a href="<portlet:renderURL>
					       						    <portlet:param name="action" value="modifierRole" />
					       						    <portlet:param name="path" value="${workspace.path}"/>
													<portlet:param name="nomRole" value="contributeur"/>
													<portlet:param name="provenance" value="consulterRole"/>
										</portlet:renderURL>"
					       			class="btn btn-default">
									<i class="glyphicons group"> </i>
									<span><op:translate  key="label.gererRoleContributeur" /></span>
								</a>
					</p>
				</c:if>
			</div>
			
		</div>

	</div>
	
	<c:if test="${config.enableReaders == true}">
		<div class="panel panel-default">
	    
		    <div class="panel-heading" role="tab" id="headingThree">
		      	<h4 class="panel-title">
			        <a class="no-ajax-link" data-toggle="collapse" data-parent="#accordion" href="#collapseThree" aria-expanded="true" aria-controls="collapseThree">
						<op:translate  key="label.lecteur" />
					</a>
				</h4>	
			</div>	
		
		
			 <div id="collapseThree" class="panel-collapse collapse in" role="tabpanel" aria-labelledby="headingThree">
			 
		     	<div class="panel-body">
		 	
				 	<c:choose>
						 <c:when test="${empty workspace.membresGroupeLecteurs && empty workspace.listeProfilLecteurs}">
								<p class="text-muted">Il n'y a encore aucune personne ni groupe d'associé au rôle <op:translate  key="label.lecteurs" /></p>
						</c:when>
						
						<c:otherwise>
							<div class="table-responsive">
								<display:table name="workspace.membresGroupeLecteurs" id="prsLecteur" pagesize="10">
									
									<display:column title="Personnes" sortable="true">
										<i class="glyphicons user"> </i>
										${prsLecteur.cn} (${prsLecteur.uid})
											
									</display:column>
						
								</display:table>
								
								<c:if test="${config.enableGroups == true}">
									<display:table name="workspace.listeProfilLecteurs" id="prfLecteur" pagesize="10">
										
										<display:column title="Groupes" sortable="true">
											<i class="glyphicons group"> </i>
													${prfLecteur.displayName}
										</display:column>
							
									</display:table>
								</c:if>
						</div>	
					</c:otherwise>
				</c:choose>	
				
				<c:if test="${workspace.modifiableByUser}">
					<p class="text-right no-ajax-link">
					       		<a href="<portlet:renderURL>
					       						    <portlet:param name="action" value="modifierRole" />
					       						    <portlet:param name="path" value="${workspace.path}"/>
													<portlet:param name="nomRole" value="lecteur"/>
													<portlet:param name="provenance" value="consulterRoles"/>
										</portlet:renderURL>"
					       			class="btn btn-default">
									<i class="glyphicons group"> </i>
									<span><op:translate  key="label.gererRoleLecteur" /></span>
								</a>
					</p>
				</c:if>
		
			</div>
			
		</div>
		
	</c:if>


</div>	
