<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="op" uri="http://www.osivia.org/jsp/taglib/osivia-portal" %>

<%@ page isELIgnored="false" %>


<portlet:defineObjects/>

<c:set var="namespace"><portlet:namespace/></c:set>

<portlet:actionURL name="save" var="url"/>


<%--@elvariable id="windowSettings" type="org.osivia.services.user.savedsearches.administration.portlet.model.UserSavedSearchesAdministrationWindowSettings"--%>
<form:form action="${url}" method="post" modelAttribute="windowSettings">
    <%--All categories indicator--%>
    <div class="form-group">
        <div class="form-check">
            <form:checkbox id="${namespace}-all-categories" path="allCategories" cssClass="form-check-input"/>
            <form:label for="${namespace}-all-categories" path="allCategories" cssClass="form-check-label"><op:translate
                    key="USER_SAVED_SEARCHES_ADMINISTRATION_FORM_ALL_CATEGORIES_CHECKBOX"/></form:label>
        </div>
    </div>

    <%--Category identifier--%>
    <div class="form-group">
        <form:label path="categoryId"><op:translate
                key="USER_SAVED_SEARCHES_ADMINISTRATION_FORM_CATEGORY_ID_LABEL"/></form:label>
        <form:input path="categoryId" cssClass="form-control"/>
        <small class="form-text text-muted"><op:translate key="USER_SAVED_SEARCHES_ADMINISTRATION_FORM_CATEGORY_ID_HELP"/></small>
    </div>

    <%--Buttons--%>
    <div>
        <button type="submit" class="btn btn-primary"><op:translate key="SAVE"/></button>
        <button type="button" onclick="closeFancybox()" class="btn btn-secondary"><op:translate key="CANCEL"/></button>
    </div>
</form:form>
