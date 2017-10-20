<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>

<%@ page contentType="text/html" isELIgnored="false" %>


<portlet:actionURL name="save" var="saveUrl" copyCurrentRenderParameters="true" />


<form:form action="${saveUrl}" method="post" modelAttribute="passwordEditionForm" enctype="multipart/form-data" cssClass="form-horizontal" role="form">
    <fieldset>
        <legend><op:translate key="PERSON_CARD_PASSWORD_EDITION_LEGEND"/></legend>
    
        <c:if test="${not passwordEditionForm.overwrite}">
            <!-- Current password -->
            <spring:bind path="current">
                <div class="form-group required ${status.error ? 'has-error has-feedback' : ''}">
                    <form:label path="current" cssClass="col-sm-3 col-lg-2 control-label"><op:translate key="PERSON_CARD_PASSWORD_CURRENT" /></form:label>
                    <div class="col-sm-9 col-lg-10">
                        <form:password path="current" cssClass="form-control" />
                        <c:if test="${status.error}">
                            <span class="form-control-feedback">
                                <i class="glyphicons glyphicons-remove"></i>
                            </span>
                        </c:if>
                        <form:errors path="current" cssClass="help-block" />
                    </div>
                </div>
            </spring:bind>
        </c:if>
        
        <!-- Update password value -->
        <spring:bind path="update">
            <div class="form-group required ${status.error ? 'has-error has-feedback' : ''}">
                <form:label path="update" cssClass="col-sm-3 col-lg-2 control-label"><op:translate key="PERSON_CARD_PASSWORD_UPDATE" /></form:label>
                <div class="col-sm-9 col-lg-10">
                    <form:password path="update" cssClass="form-control" />
                    <c:if test="${status.error}">
                        <span class="form-control-feedback">
                            <i class="glyphicons glyphicons-remove"></i>
                        </span>
                    </c:if>
                    <form:errors path="update" cssClass="help-block" />
                </div>
            </div>
        </spring:bind>
        
        <!-- Update password confirmation -->
        <spring:bind path="confirmation">
            <div class="form-group required ${status.error ? 'has-error has-feedback' : ''}">
                <form:label path="confirmation" cssClass="col-sm-3 col-lg-2 control-label"><op:translate key="PERSON_CARD_PASSWORD_CONFIRMATION" /></form:label>
                <div class="col-sm-9 col-lg-10">
                    <form:password path="confirmation" cssClass="form-control" />
                    <c:if test="${status.error}">
                        <span class="form-control-feedback">
                            <i class="glyphicons glyphicons-remove"></i>
                        </span>
                    </c:if>
                    <form:errors path="confirmation" cssClass="help-block" />
                </div>
            </div>
        </spring:bind>
        
        <!-- Buttons -->
        <%@ include file="edition/buttons.jspf" %>
    </fieldset>
</form:form>
