<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<%@ page contentType="text/html" isELIgnored="false" %>


<portlet:actionURL name="save" var="saveUrl" />


<form:form modelAttribute="settings" action="${saveUrl}" method="post" cssClass="form-horizontal" role="form">

    <!-- Resource loader stub -->
    <%@ include file="admin/stub.jspf" %>
        
    <!-- Buttons -->
    <%@ include file="admin/buttons.jspf" %>

</form:form>
