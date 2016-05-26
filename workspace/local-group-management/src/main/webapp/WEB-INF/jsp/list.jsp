<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>

<%@ page contentType="text/html" isELIgnored="false"%>


<portlet:actionURL name="save" var="saveUrl" />


<c:set var="namespace"><portlet:namespace /></c:set>


<form:form action="${saveUrl}" method="post" modelAttribute="localGroups">
    <div class="table">
        <!-- Header -->
        <div class="table-header">
            <div class="row">
                <!-- Local group -->
                <div class="col-xs-12 col-sm-10">
                    <span><op:translate key="LOCAL_GROUP" /></span>
                </div>
            </div>
        </div>
        
        <!-- Body -->
        <c:forEach var="group" items="${localGroups.groups}" varStatus="status">
            <portlet:actionURL name="edit" var="editUrl">
                <portlet:param name="id" value="${group.id}"/>
            </portlet:actionURL>

            <c:set var="members" value="${fn:length(group.members)}" />

        
            <div class="row">
                <form:hidden path="groups[${status.index}].deleted" />
            
                <fieldset>
                    <!-- Local group -->
                    <div class="col-xs-12 col-sm-10">
                        <div>
                            <span>${group.displayName}</span>
                        </div>
                        
                        <div>
                            <small class="text-muted">
                                <c:choose>
                                    <c:when test="${members eq 0}"><op:translate key="LOCAL_GROUP_NO_MEMBER" /></c:when>
                                    <c:when test="${members eq 1}"><op:translate key="LOCAL_GROUP_ONE_MEMBER" /></c:when>
                                    <c:otherwise><op:translate key="LOCAL_GROUP_N_MEMBERS" args="${members}" /></c:otherwise>
                                </c:choose>
                            </small>
                        </div>
                    </div>
                    
                    <!-- Actions -->
                    <div class="col-xs-12 col-sm-2 actions">
                        <!-- Edit -->
                        <a href="${editUrl}" class="btn btn-default">
                            <i class="glyphicons glyphicons-pencil"></i>
                            <span class="sr-only"><op:translate key="EDIT" /></span>
                        </a>
                    
                        <!-- Delete -->
                        <button type="button" class="btn btn-default" data-type="delete-local-group">
                            <i class="glyphicons glyphicons-bin"></i>
                            <span class="sr-only"><op:translate key="DELETE" /></span>
                        </button>
                    </div>
                </fieldset>
            </div>
        </c:forEach>
        
        <!-- No results -->
        <c:if test="${empty localGroups.groups}">
            <div class="row">
                <div class="col-xs-12 text-center"><op:translate key="NO_LOCAL_GROUP" /></div>
            </div>
        </c:if>
    </div>
    
    
    <!-- Buttons -->
    <div id="${namespace}-buttons" class="form-group collapse">
        <!-- Save -->
        <button type="submit" name="save" class="btn btn-primary">
            <i class="glyphicons glyphicons-floppy-disk"></i>
            <span><op:translate key="SAVE" /></span>
        </button>
        
        <!-- Cancel -->
        <button type="reset" class="btn btn-default" data-toggle="collapse" data-target="#${namespace}-buttons">
            <span><op:translate key="CANCEL" /></span>
        </button>
    </div>
</form:form>
