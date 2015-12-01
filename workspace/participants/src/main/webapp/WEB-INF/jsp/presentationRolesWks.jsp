<%@ include file="/WEB-INF/jsp/include.jsp" %>

<%@ page language="java" pageEncoding="UTF-8" isELIgnored="false" %>


<portlet:defineObjects />


<c:set var="administrators" value="${workspace.membresGroupeAnimateurs}" />
<c:set var="members" value="${workspace.membresGroupeContributeurs}" />


<div class="workspace-participants clearfix">
    <!-- Administrators -->
    <c:if test="${not empty administrators}">
        <p class="pull-left">
            <span class="text-muted">
                <c:choose>
                    <c:when test="${fn:length(administrators) eq 1}"><op:translate key="WORKSPACE_PARTICIPANTS_ONE_ADMINISTRATOR" /></c:when>
                    <c:otherwise><op:translate key="WORKSPACE_PARTICIPANTS_ADMINISTRATORS" args="${fn:length(administrators)}" /></c:otherwise>
                </c:choose>
            </span>
            
            <c:forEach var="administrator" items="${administrators}">
                <a href="${administrator.url}" class="no-ajax-link" title="${administrator.nom}" data-toggle="tooltip" data-placement="bottom">
                    <span>
                        <img src="${administrator.avatar.url}" alt="${administrator.nom}">
                    </span>
                </a>
            </c:forEach>
        </p>
    </c:if>

    <!-- Members -->
    <c:if test="${not empty members}">
        <p class="pull-left">
            <span class="text-muted">
                <c:choose>
                    <c:when test="${fn:length(members) eq 1}"><op:translate key="WORKSPACE_PARTICIPANTS_ONE_MEMBER" /></c:when>
                    <c:otherwise><op:translate key="WORKSPACE_PARTICIPANTS_MEMBERS" args="${fn:length(members)}" /></c:otherwise>
                </c:choose>
            </span>
            
            <c:forEach var="member" items="${members}">
                <a href="${member.url}" class="no-ajax-link" title="${member.nom}" data-toggle="tooltip" data-placement="bottom">
                    <span>
                        <img src="${member.avatar.url}" alt="${member.nom}">
                    </span>
                </a>
            </c:forEach>
        </p>
    </c:if>
</div>
