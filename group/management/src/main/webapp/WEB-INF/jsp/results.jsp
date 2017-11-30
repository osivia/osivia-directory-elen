<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>

<%@ page contentType="text/html" isELIgnored="false"%>


<c:if test="${form.displayed}">
    <%-- <div class="panel panel-default">
        <div class="panel-body">
            <c:choose>
                <c:when test="${empty form.groups}"><op:translate key="GROUP_MANAGEMENT_NO_RESULT" /></c:when>
                <c:when test="${fn:length(form.groups) eq 1}"><op:translate key="GROUP_MANAGEMENT_ONE_RESULT" /></c:when>
                <c:otherwise><op:translate key="GROUP_MANAGEMENT_MULTIPLE_RESULTS" args="${fn:length(form.groups)}"/></c:otherwise>
            </c:choose>
        </div>
        
        <div class="list-group">
            <c:forEach items="${form.groups}" var="group">
                <a href="javascript:;" class="list-group-item ${group.cn eq form.selected ? 'active' : ''}" data-id="${group.cn}">
                    <span>${empty group.displayName ? group.cn : group.displayName}</span>
                </a>
            </c:forEach>
        </div>
    </div> --%>


    <div class="table table-hover">
        <!-- Table header -->
        <div class="table-row table-header">
            <c:choose>
                <c:when test="${empty form.groups}"><op:translate key="GROUP_MANAGEMENT_NO_RESULT" /></c:when>
                <c:when test="${fn:length(form.groups) eq 1}"><op:translate key="GROUP_MANAGEMENT_ONE_RESULT" /></c:when>
                <c:otherwise><op:translate key="GROUP_MANAGEMENT_MULTIPLE_RESULTS" args="${fn:length(form.groups)}"/></c:otherwise>
            </c:choose>
        </div>
        
        <!-- Table body -->
        <c:forEach items="${form.groups}" var="group">
            <div class="table-row">
                <%@ include file="result.jspf" %>
            </div>
        </c:forEach>
    </div>
</c:if>
