<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>

<%@ page contentType="text/html" isELIgnored="false"%>


<portlet:defineObjects />


<portlet:actionURL name="edit" var="editUrl">
    <portlet:param name="view" value="edit" />
    <portlet:param name="id" value="${localGroup.id}" />
</portlet:actionURL>

<portlet:actionURL name="delete" var="deleteUrl">
    <portlet:param name="view" value="edit" />
    <portlet:param name="id" value="${localGroup.id}" />
</portlet:actionURL>

<c:set var="namespace"><portlet:namespace /></c:set>


<div class="workspace-local-group-management">
    <form:form action="${editUrl}" method="post" modelAttribute="localGroup" cssClass="form-horizontal">
        <fieldset>
            <legend>
                <i class="glyphicons glyphicons-pencil"></i>
                <span><op:translate key="EDIT_LOCAL_GROUP_LEGEND" /></span>
            </legend>
    
            <!-- Display name -->
            <spring:bind path="displayName">
                <div class="form-group ${status.error ? 'has-error has-feedback' : ''}">
                    <form:label path="displayName" cssClass="col-sm-3 col-lg-2 control-label"><op:translate key="LOCAL_GROUP_DISPLAY_NAME" /></form:label>
                    <div class="col-sm-9 col-lg-10">
                        <form:input path="displayName" cssClass="form-control" />
                        <c:if test="${status.error}">
                            <span class="form-control-feedback">
                                <i class="glyphicons glyphicons-remove"></i>
                            </span>
                        </c:if>
                        <form:errors path="displayName" cssClass="help-block" />
                    </div>
                </div>
            </spring:bind>
            
            <!-- Members -->
            <div class="form-group">
                <label class="col-sm-3 col-lg-2 control-label"><op:translate key="LOCAL_GROUP_MEMBERS" /></label>
                <div class="col-sm-9 col-lg-10">
                    <c:choose>
                        <c:when test="${empty localGroup.members}">
                            <p class="form-control-static text-muted"><op:translate key="LOCAL_GROUP_NO_MEMBER" /></p>
                        </c:when>
                        
                        <c:otherwise>
                            <ul class="list-unstyled">
                                <c:forEach var="member" items="${localGroup.members}" varStatus="status">
                                    <li>
                                        <fieldset
                                            <c:if test="${member.deleted}">disabled="disabled"</c:if>
                                        >                                        
                                            <form:hidden path="members[${status.index}].deleted" />
                                        
                                            <p class="form-control-static">
                                                <c:if test="${not empty member.avatar}">
                                                    <img src="${member.avatar}" alt="" class="avatar">
                                                </c:if>
                                                
                                                <span>${member.displayName}</span>
                                                
                                                <button type="button" class="btn btn-link btn-xs" data-type="delete-member">
                                                    <i class="glyphicons glyphicons-remove"></i>
                                                    <span class="sr-only"><op:translate key="DELETE" /></span>
                                                </button>
                                            </p>
                                        </fieldset>
                                    </li>
                                </c:forEach>
                            </ul>
                        </c:otherwise>
                    </c:choose>
                    
                    <!-- Add members -->
                    <c:set var="placeholder"><op:translate key="ADD_MEMBERS_LABEL" /></c:set>
                    <form:label path="addedMembers" cssClass="sr-only">${placeholder}</form:label>
                    <form:select path="addedMembers" cssClass="form-control select2" data-placeholder="${placeholder}">
                        <c:forEach var="member" items="${members}">
                            <option value="${member.id}" data-displayname="${member.displayName}" data-avatar="${member.avatar}" data-mail="${member.mail}">${member.displayName} - ${member.id} - ${member.mail}</option>
                        </c:forEach>
                    </form:select>
                    <input type="submit" name="add" class="hidden">
                </div>
            </div>
        
            <!-- Buttons -->
            <div class="form-group">
                <div class="col-sm-offset-3 col-sm-9 col-lg-offset-2 col-lg-10">
                    <!-- Save -->
                    <button type="submit" name="save" class="btn btn-primary">
                        <i class="glyphicons glyphicons-floppy-disk"></i>
                        <span><op:translate key="SAVE" /></span>
                    </button>
                    
                    <!-- Delete -->
                    <button type="button" class="btn btn-danger" data-toggle="modal" data-target="#${namespace}-delete-modal">
                        <i class="glyphicons glyphicons-bin"></i>
                        <span><op:translate key="DELETE" /></span>
                    </button>
                    
                    <!-- Cancel -->
                    <button type="submit" name="cancel" class="btn btn-default">
                        <span><op:translate key="CANCEL" /></span>
                    </button>
                </div>
            </div>
        </fieldset>
    </form:form>
    
    
    <div id="${namespace}-delete-modal" class="modal fade" role="dialog">
        <div class="modal-dialog modal-sm">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">
                        <i class="glyphicons glyphicons-remove"></i>
                        <span class="sr-only"><op:translate key="CLOSE" /></span>
                    </button>
                    
                    <h4 class="modal-title"><op:translate key="DELETE_LOCAL_GROUP_MODAL_TITLE" /></h4>
                </div>
                
                <div class="modal-body">
                    <p><op:translate key="DELETE_LOCAL_GROUP_MODAL_MESSAGE" args="${localGroup.displayName}" /></p>
                </div>
                
                <div class="modal-footer">
                    <a href="${deleteUrl}" class="btn btn-danger" data-dismiss="modal">
                        <i class="glyphicons glyphicons-bin"></i>
                        <span><op:translate key="DELETE" /></span>
                    </a>
                    
                    <button type="button" class="btn btn-default" data-dismiss="modal">
                        <span><op:translate key="CANCEL" /></span>
                    </button>
                </div>
            </div>
        </div>
    </div>
</div>
