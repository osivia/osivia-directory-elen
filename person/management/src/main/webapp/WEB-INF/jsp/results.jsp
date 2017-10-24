<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>

<%@ page contentType="text/html" isELIgnored="false"%>


<div class="table table-hover">
    <!-- Table header -->
    <div class="table-row table-header">
        <c:choose>
            <c:when test="${empty form.users}">
                <span><op:translate key="PERSON_MANAGEMENT_NO_RESULT" /></span>
            </c:when>
            
            <c:when test="${fn:length(form.users) eq 1}">
                <span><op:translate key="PERSON_MANAGEMENT_ONE_RESULT" /></span>
            </c:when>
            
            <c:otherwise>
                <span><op:translate key="PERSON_MANAGEMENT_MULTIPLE_RESULTS" args="${fn:length(form.users)}"/></span>
            </c:otherwise>
        </c:choose>
    </div>
    
    <!-- Table body -->
    <c:if test="${not empty form.users}">
        <c:forEach items="${form.users}" var="user">
            <div class="table-row">
                <a href="#" class="person no-ajax-link ${user.id eq form.selectedUserId ? 'active' : ''}" data-id="${user.id}">
                    <!-- Avatar -->
                    <span class="person-avatar">
                        <c:choose>
                            <c:when test="${empty user.avatarUrl}">
                                <i class="glyphicons glyphicons-user"></i>
                            </c:when>
                            
                            <c:otherwise>
                                <img src="${user.avatarUrl}" alt="">
                            </c:otherwise>
                        </c:choose>
                    </span>
                    
                    <!-- Display name -->
                    <span class="person-title">${user.displayName}</span>
                    
                    <!-- Extra -->
                    <c:if test="${not empty user.extra}">
                        <span class="person-extra">${user.extra}</span>
                    </c:if>
                </a>
            </div>
        </c:forEach>
    </c:if>
</div>
