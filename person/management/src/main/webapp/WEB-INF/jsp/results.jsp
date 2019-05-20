<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>

<%@ page contentType="text/html" isELIgnored="false"%>


<table class="table table-hover">
    <%--Table header--%>
    <thead>
        <tr>
            <th>
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
            </th>
        </tr>
    </thead>
    
    <%--Table body--%>
    <c:if test="${not empty form.users}">
        <tbody>
            <c:forEach items="${form.users}" var="user">
                <tr>
                    <td class="position-relative ${user.id eq form.selectedUserId ? 'table-active' : ''}">
                        <div class="d-flex">
                            <%--Avatar--%>
                            <div class="mr-2">
                                <c:choose>
                                    <c:when test="${empty user.avatarUrl}">
                                        <i class="glyphicons glyphicons-basic-user"></i>
                                    </c:when>

                                    <c:otherwise>
                                        <img src="${user.avatarUrl}" alt="" class="avatar">
                                    </c:otherwise>
                                </c:choose>
                            </div>

                            <div class="d-flex flex-grow-1 flex-column">
                                <%--Display name--%>
                                <a href="javascript:" class="stretched-link no-ajax-link" data-id="${user.id}">
                                    <span>${user.displayName}</span>
                                </a>

                                <%--Extra--%>
                                <c:if test="${not empty user.extra}">
                                    <small class="text-muted">${user.extra}</small>
                                </c:if>
                            </div>
                        </div>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </c:if>
</table>
