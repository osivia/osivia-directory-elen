
<ul class="nav nav-tabs">

	<c:choose>
		<c:when test="${currentTab == 'recherchePersonne'}">
			<li class="active"><a href="#"><op:translate  key="tabs.recherchePersonne" /></a></li>
		</c:when>
		<c:otherwise>
			<li><a href="<portlet:renderURL><portlet:param name="action" value="recherchePersonne" /></portlet:renderURL>"><op:translate  key="tabs.recherchePersonne" /></a></li>
		</c:otherwise>
	</c:choose>
	
	<c:if
		test="${config.enableOverload && (formulaire.levelSurchargeUserConnecte == 'SUPERADMINISTRATEUR'||formulaire.levelSurchargeUserConnecte == 'ADMINISTRATEUR'||formulaire.levelSurchargeUserConnecte == 'ASSISTANCE')}">
		
		<c:choose>
			<c:when test="${currentTab == 'mesSurcharges'}">
				<li class="active"><a href="#"><op:translate  key="tabs.mesSurcharges" /></a></li>
			</c:when>
			<c:otherwise>
				<li><a href="<portlet:renderURL><portlet:param name="action" value="mesSurcharges" /></portlet:renderURL>"><op:translate  key="tabs.mesSurcharges" /></a></li>
			</c:otherwise>
		</c:choose>
		
	</c:if>
	
	<c:if
		test="${config.enableOverload && (formulaire.levelSurchargeUserConnecte == 'SUPERADMINISTRATEUR'||formulaire.levelSurchargeUserConnecte == 'ADMINISTRATEUR')}">
		
		<c:choose>
			<c:when test="${currentTab == 'personnesSurchargees'}">
				<li class="active"><a href="#"><op:translate  key="tabs.personnesSurchargees" /></a></li>
			</c:when>
			<c:otherwise>
				<li><a href="<portlet:renderURL><portlet:param name="action" value="personnesSurchargees" /></portlet:renderURL>"><op:translate  key="tabs.personnesSurchargees" /></a></li>
			</c:otherwise>
		</c:choose>				
		
		
	</c:if>
	
</ul>
