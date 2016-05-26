<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
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
        <fieldset>
            <legend><op:translate key="ADD_MEMBERS_LEGEND" /></legend>
        
            <div class="row">
                <div class="col-sm-8">
                    <!-- Member names selector -->
                    <spring:bind path="names">
                        <div class="form-group ${status.error ? 'has-error' : ''}">
                            <form:label path="names" cssClass="control-label"><op:translate key="ADD_MEMBERS_LABEL" /></form:label>
                            <form:select path="names" cssClass="form-control select2" multiple="multiple" data-placeholder="${placeholder}" data-url="${searchUrl}" data-input-too-short="${inputTooShort}" data-searching="${searching}" data-no-results="${noResults}">
                            </form:select>
                            <form:errors path="names" cssClass="help-block" />
                        </div>
                    </spring:bind>
                </div>
                
                <div class="col-sm-4">
                    <!-- Role -->
                    <div class="form-group">
                        <form:label path="role" cssClass="control-label"><op:translate key="ROLE" /></form:label>
                        <form:select path="role" cssClass="form-control">
                            <c:forEach var="role" items="${roles}">
                                <form:option value="${role}"><op:translate key="${role.key}" /></form:option>
                            </c:forEach>
                        </form:select>
                    </div>
                </div>
            </div>
            
            <!-- Buttons -->
            <spring:bind path="*">
                <div class="collapse ${status.error ? 'in' : ''}">
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
            </spring:bind>
        </fieldset>
    </form:form>
</div>
