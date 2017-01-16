<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>

<%@ page contentType="text/html" isELIgnored="false"%>


<portlet:defineObjects />

<portlet:actionURL name="select" var="selectUrl" />

<portlet:resourceURL id="search" var="searchUrl" />


<c:set var="filterPlaceholder"><op:translate key="PERSON_MANAGEMENT_FILTER_PLACEHOLDER" /></c:set>


<div class="person-management">
    <form:form action="${selectUrl}" method="post" modelAttribute="form" role="form" data-url="${searchUrl}">
        <form:hidden path="selectedUserId" />
        
        <!-- Filter -->
        <div class="form-group">
            <form:label path="filter" cssClass="sr-only"><op:translate key="PERSON_MANAGEMENT_FILTER"/></form:label>
            <div class="input-group">
                <span class="input-group-addon">
                    <i class="glyphicons glyphicons-search"></i>
                </span>
                <form:input path="filter" cssClass="form-control" placeholder="${filterPlaceholder}" />
            </div>
            <p class="help-block">
                <span><op:translate key="PERSON_MANAGEMENT_FILTER_HELP" /></span>
            </p>
            
            <div class="hidden-script">
                <button type="submit" name="apply-filter" class="btn btn-default">
                    <span><op:translate key="SEARCH" /></span>
                </button>
            </div>
        </div>
    
        <!-- Results -->
        <div class="form-group results">
            <jsp:include page="results.jsp" />
        </div>
        
        <!-- Submit -->
        <input type="submit" name="select" class="hidden">
    </form:form>
</div>
