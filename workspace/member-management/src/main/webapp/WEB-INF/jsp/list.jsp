<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>

<%@ page contentType="text/html" isELIgnored="false"%>


<portlet:renderURL var="sortMemberUrl">
    <portlet:param name="sort" value="name" />
    <portlet:param name="alt" value="${sort eq 'name' and not alt}"/>
</portlet:renderURL>
<portlet:renderURL var="sortRoleUrl">
    <portlet:param name="sort" value="role" />
    <portlet:param name="alt" value="${sort ne 'role' or not alt}"/>
</portlet:renderURL>

<portlet:actionURL name="update" var="updateUrl">
    <portlet:param name="sort" value="${sort}" />
    <portlet:param name="alt" value="${alt}" />
</portlet:actionURL>


<form:form action="${updateUrl}" method="post" modelAttribute="container">
    <div class="table">
        <!-- Header -->
        <div class="table-header table-row">
            <div class="row">
                <div class="col-sm-8">
                    <!-- Member -->
                    <div class="table-cell">
                        <a href="${sortMemberUrl}"><op:translate key="MEMBER"/></a>
                        
                        <c:if test="${sort eq 'name'}">
                            <small class="text-muted">
                                <c:choose>
                                    <c:when test="${alt}"><i class="halflings halflings-sort-by-attributes-alt"></i></c:when>
                                    <c:otherwise><i class="halflings halflings-sort-by-attributes"></i></c:otherwise>
                                </c:choose>
                            </small>
                        </c:if>
                    </div>
                </div>
                
                <div class="col-sm-3">
                    <!-- Role -->
                    <div class="table-cell">
                        <a href="${sortRoleUrl}"><op:translate key="ROLE"/></a>
                        
                        <c:if test="${sort eq 'role'}">
                            <small class="text-muted">
                                <c:choose>
                                    <c:when test="${alt}"><i class="halflings halflings-sort-by-attributes-alt"></i></c:when>
                                    <c:otherwise><i class="halflings halflings-sort-by-attributes"></i></c:otherwise>
                                </c:choose>
                            </small>
                        </c:if>
                    </div>
                </div>
            </div>
        </div>
        
        <!-- Body -->
        <c:forEach var="member" items="${container.members}" varStatus="status">
            <div class="table-row">
                <div class="row">
                    <div class="col-sm-8">
                        <!-- Member -->
                        <div class="table-cell">
                            <div class="workspace-member-result">
                                <div class="media">
                                    <!-- Avatar -->
                                    <div class="media-left media-middle">
                                        <div class="media-object">
                                            <img src="${member.avatar}" alt="">
                                        </div>
                                    </div>
                                    
                                    <div class="media-body">
                                        <!-- Display name -->
                                        <div>${member.displayName}</div>
                                        
                                        <!-- Mail -->
                                        <c:if test="${not empty member.mail}">
                                            <div>
                                                <small class="text-muted">${member.mail}</small>
                                            </div>
                                        </c:if>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    
                    <div class="col-xs-10 col-sm-3">
                        <!-- Role -->
                        <div class="table-cell">
                            <form:label path="members[${status.index}].role" cssClass="sr-only"><op:translate key="ROLE" /></form:label>
                            <form:select path="members[${status.index}].role" cssClass="form-control">
                                <c:forEach var="role" items="${roles}">
                                    <form:option value="${role}"><op:translate key="${role.key}" /></form:option>
                                </c:forEach>
                            </form:select>
                        </div>
                    </div>
                    
                    <div class="col-xs-2 col-sm-1">
                        <!-- Deletion -->
                        <portlet:actionURL name="delete" var="deleteUrl">
                            <portlet:param name="sort" value="${sort}" />
                            <portlet:param name="alt" value="${alt}" />
                            <portlet:param name="name" value="${member.name}" />
                        </portlet:actionURL>
                        
                        <div class="table-cell">
                            <a href="${deleteUrl}" class="btn btn-link"><i class="glyphicons glyphicons-remove"></i></a>
                        </div>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>
    
    <div class="form-group collapse">
        <!-- Save -->
        <button type="submit" name="save" class="btn btn-primary">
            <i class="glyphicons glyphicons-floppy-disk"></i>
            <span><op:translate key="SAVE" /></span>
        </button>
        
        <!-- Cancel -->
        <button type="reset" name="cancel" class="btn btn-default">
            <span><op:translate key="CANCEL" /></span>
        </button>
    </div>
</form:form>
