<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>

<%@ page contentType="text/html" isELIgnored="false"%>
<portlet:actionURL name="selectPerson" var="selectPersonUrl"></portlet:actionURL>

<c:set var="namespace"><portlet:namespace /></c:set>


<portlet:resourceURL id="search" var="searchUrl" />

<form:form action="${selectPersonUrl}" modelAttribute="personManagementForm" method="post" role="form">

	<div class="row">
		<div class="col-sm-12 col-lg-12">
		
			<c:set var="placeholder"><op:translate key="PERSON_MANAGEMENT_SEARCH_PLACEHOLDER" /></c:set>
			<form:input cssClass="form-control filter" path="filter" placeholder="${placeholder}" data-url="${searchUrl}" />
		
		</div>
	</div>
	
	
    <div class="table table-hover">
        <!-- Header -->
        <div class="table-row table-header">
            <div class="row">
                <!-- Member -->
				<div class="col-sm-12 col-lg-12">
                	<op:translate key="PERSON_MANAGEMENT_MEMBER"/>
				</div>
            </div>
        </div>
        
		<div id="personResults">
		</div>

	</div>
	
	<form:hidden path="selectedPerson"/>
	<input type="submit" class="hidden">

</form:form>