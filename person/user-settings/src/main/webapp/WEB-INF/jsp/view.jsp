<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>

<%@ page contentType="text/html" isELIgnored="false"%>


<portlet:defineObjects />

<portlet:actionURL name="save" var="saveUrl" />


<div class="user-settings">
    <form:form action="${saveUrl}" method="post" modelAttribute="form" cssClass="form-horizontal" role="form">
        <fieldset>
            <legend>
                <i class="glyphicons glyphicons-cogwheel"></i>
                <span><op:translate key="USER_SETTINGS" /></span>
            </legend>
        
            <div class="portlet-filler container-fluid">
                <!-- Are notifications enabled? -->
                <div class="form-group">
                    <form:label path="notificationsEnabled" cssClass="col-form-label col-sm-3 col-lg-2"><op:translate key="USER_SETTINGS_NOTIFICATIONS_ENABLED" /></form:label>
                    <div class="col-sm-9 col-lg-10">
                        <div class="checkbox">
                            <label>
                                <form:checkbox path="notificationsEnabled" />
                                <span><op:translate key="USER_SETTINGS_NOTIFICATIONS_ENABLED_CHECKBOX" /></span>
                            </label>
                        </div>
                    </div>
                </div>
                
                <!-- Workspace notifications -->
                <div class="form-group">
                    <label class="col-form-label col-sm-3 col-lg-2"><op:translate key="USER_SETTINGS_WORKSPACE_NOTIFICATIONS" /></label>
                    <div class="col-sm-9 col-lg-10">
                        <c:forEach items="${form.workspaceNotifications}" var="notification" varStatus="status">
                            <div class="panel panel-default">
                                <div class="panel-body">
                                    <div class="row">
                                        <div class="col-sm-6">
                                            <div class="media">
                                                <!-- Vignette -->
                                                <c:if test="${not empty notification.vignetteUrl}">
                                                    <div class="media-left media-middle">
                                                        <img src="${notification.vignetteUrl}" alt="" class="media-object">
                                                    </div>
                                                </c:if>
                                                
                                                <div class="media-body media-middle">
                                                    <!-- Title -->
                                                    <h3 class="media-heading h4">${notification.title}</h3>
                                                    
                                                    <!-- Description -->
                                                    <c:if test="${not empty notification.description}">
                                                        <p>${notification.description}</p>
                                                    </c:if>
                                                </div>
                                            </div>
                                        </div>
                                        
                                        <div class="col-sm-6">
                                            <!-- Periodicity -->
                                            <form:select path="workspaceNotifications[${status.index}].periodicity" cssClass="form-control">
                                                <c:forEach items="${form.workspaceNotificationPeriodicities}" var="periodicity">
                                                    <form:option value="${periodicity}"><op:translate key="${periodicity.key}" /></form:option>
                                                </c:forEach>
                                            </form:select>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </div>
            </div>
        
            <!-- Buttons -->
            <div class="portlet-toolbar adapt-scrollbar">
                <div class="row">
                    <div class="col-sm-offset-3 col-sm-9 col-lg-offset-2 col-lg-10">
                        <!-- Save -->
                        <button type="submit" class="btn btn-primary">
                            <i class="glyphicons glyphicons-floppy-disk"></i>
                            <span><op:translate key="SAVE" /></span>
                        </button>
                        
                        <!-- Cancel -->
                        <button type="reset" class="btn btn-secondary">
                            <span><op:translate key="CANCEL" /></span>
                        </button>
                    </div>
                </div>
            </div>
        </fieldset>
    </form:form>
</div>
