<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<%@ page contentType="text/html" isELIgnored="false"%>


<portlet:defineObjects />

<portlet:actionURL name="submit" var="submitUrl" />

<portlet:resourceURL id="search" var="searchUrl" />


<div class="group-management">
    <form:form action="${submitUrl}" method="post" modelAttribute="form" role="form" data-url="${searchUrl}">
        <form:hidden path="selected" />
        <input type="submit" name="select" class="hidden">
    
        <!-- Filter -->
        <%@ include file="filter.jspf" %>
        
        <!-- Results -->
        <%@ include file="results.jspf" %>
    </form:form>
</div>
