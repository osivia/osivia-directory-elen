

<div class="row">
	<div class="col-sm-12">
		<ul class="nav nav-tabs">
			<c:if
				test="${level == 'ADMINISTRATEUR' || not empty formulaire.listeGroupesGeres}">

				<c:choose>
					<c:when test="${currentTab == 'groupesGeres'}">
						<li class="active"><a href="#"><is:getProperty key="tabs.groupesGeres" /></a></li>
					</c:when>
					<c:otherwise>
						<li><a href="<portlet:renderURL><portlet:param name="action" value="groupesGeres" /></portlet:renderURL>">
								<is:getProperty key="tabs.groupesGeres" /></a></li>
					</c:otherwise>
				</c:choose>

			</c:if>

			<c:if test="${level == 'ADMINISTRATEUR'}">



				<c:choose>
					<c:when test="${currentTab == 'mesGroupes'}">
						<li class="active"><a href="#"><is:getProperty key="tabs.mesGroupes" /></a></li>
					</c:when>
					<c:otherwise>
						<li><a href="<portlet:renderURL><portlet:param name="action" value="mesGroupes" /></portlet:renderURL>"><is:getProperty key="tabs.mesGroupes" /></a></li>
					</c:otherwise>
				</c:choose>


				<c:choose>
					<c:when test="${currentTab == 'rechercheGroupe'}">
						<li class="active"><a href="#"><is:getProperty key="tabs.rechercheGroupe" /></a></li>
					</c:when>
					<c:otherwise>
						<li><a href="<portlet:renderURL><portlet:param name="action" value="rechercheAvancee" /></portlet:renderURL>"><is:getProperty key="tabs.rechercheGroupe" /></a></li>
					</c:otherwise>
				</c:choose>
			</c:if>

		</ul>
	</div>
</div>