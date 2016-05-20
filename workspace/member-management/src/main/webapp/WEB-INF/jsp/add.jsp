<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>

<%@ page contentType="text/html" isELIgnored="false"%>


<portlet:actionURL name="add" var="addUrl">
    <portlet:param name="sort" value="${sort}" />
    <portlet:param name="alt" value="${alt}" />
</portlet:actionURL>

<portlet:resourceURL id="search" var="searchUrl" />

<c:set var="namespace"><portlet:namespace /></c:set>

<c:set var="placeholder"><op:translate key="SELECT2_PLACEHOLDER" /></c:set>
<c:set var="inputTooShort"><op:translate key="SELECT2_INPUT_TOO_SHORT" /></c:set>
<c:set var="searching"><op:translate key="SELECT2_SEARCHING" /></c:set>
<c:set var="noResults"><op:translate key="SELECT2_NO_RESULTS" /></c:set>


<div class="well">
    <form:form action="${addUrl}" method="post" modelAttribute="addForm" role="form">
        <div class="form-group">
            <!-- Label -->
            <form:label path="names"><op:translate key="ADD_MEMBERS_LABEL" /></form:label>
        
            <div class="row">
                <div class="col-sm-8">
                    <!-- Member names selector -->
                    <form:select path="names" cssClass="form-control select2" multiple="multiple" data-placeholder="${placeholder}" data-url="${searchUrl}" data-input-too-short="${inputTooShort}" data-searching="${searching}" data-no-results="${noResults}">
                    </form:select>
                </div>
                
                <div class="col-sm-4">
                    <!-- Role -->
                    <form:label path="role" cssClass="sr-only"><op:translate key="ROLE" /></form:label>
                    <form:select path="role" cssClass="form-control">
                        <c:forEach var="role" items="${roles}">
                            <form:option value="${role}"><op:translate key="${role.key}" /></form:option>
                        </c:forEach>
                    </form:select>
                </div>
            </div>
        </div>
        
        <div class="collapse">
            <!-- Save -->
            <button type="submit" name="save" class="btn btn-primary">
                <i class="glyphicons glyphicons-floppy-disk"></i>
                <span><op:translate key="SAVE" /></span>
            </button>
            
            <!-- Cancel -->
            <button type="submit" name="cancel" class="btn btn-default">
                <span><op:translate key="CANCEL" /></span>
            </button>
        </div>
    </form:form>
</div>
