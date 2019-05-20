<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<%@ page contentType="text/html" isELIgnored="false"%>


<portlet:defineObjects />

<portlet:actionURL name="select" var="selectUrl" />

<portlet:resourceURL id="search" var="searchUrl" />


<div class="person-management">
    <form:form action="${selectUrl}" method="post" modelAttribute="form" role="form" data-url="${searchUrl}">
        <form:hidden path="selectedUserId" />
        
        <!-- Filter -->
        <%@ include file="filter.jspf" %>
    
        <!-- Results -->
        <div class="form-group results">
            <jsp:include page="results.jsp" />
        </div>
        
        <!-- Submit -->
        <input type="submit" name="select" class="d-none">
    </form:form>
</div>
