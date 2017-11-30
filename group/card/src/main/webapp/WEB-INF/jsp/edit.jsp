<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>

<%@ page contentType="text/html" isELIgnored="false" %>


<portlet:actionURL name="save" var="saveUrl" copyCurrentRenderParameters="true" />


<div class="edit-portal-group">
<form:form action="${saveUrl}" method="post" modelAttribute="editionForm" cssClass="form-horizontal" role="form">
    <legend><op:translate key="GROUP_CARD_EDITION_LEGEND" /></legend>
    
    <!-- Displayname- -->
    <%@ include file="edition/displayname.jspf" %>
    
    <!-- Description -->
    <%@ include file="edition/description.jspf" %>
    
    <!-- Member creation -->
    <%@ include file="edition/member-creation.jspf" %>
    
    <!-- Member list -->
    <%@ include file="edition/member-list.jspf" %>
    
    <!-- Buttons -->
    <%@ include file="edition/buttons.jspf" %>
</form:form>
</div>
