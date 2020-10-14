<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="op" uri="http://www.osivia.org/jsp/taglib/osivia-portal" %>


<c:set var="namespace"><portlet:namespace/></c:set>
<c:set var="renameModalId" value="${namespace}-rename"/>
<c:set var="deleteConfirmationModalId" value="${namespace}-delete-confirmation"/>

<portlet:actionURL name="save" var="saveUrl"/>
<portlet:actionURL name="rename" var="renameUrl"/>
<portlet:actionURL name="delete" var="deleteUrl"/>


<%--@elvariable id="windowSettings" type="org.osivia.services.user.savedsearches.administration.portlet.model.UserSavedSearchesAdministrationWindowSettings"--%>
<%--@elvariable id="form" type="org.osivia.services.user.savedsearches.administration.portlet.model.UserSavedSearchesAdministrationForm"--%>
<div class="user-saved-searches-administration">
    <c:choose>
        <c:when test="${empty form.categorizedSavedSearches or (not windowSettings.allCategories and empty form.categorizedSavedSearches[windowSettings.categoryId])}">
            <p class="text-secondary"><op:translate key="USER_SAVED_SEARCHES_ADMINISTRATION_EMPTY"/></p>
        </c:when>

        <c:otherwise>
            <%--@elvariable id="form" type="org.osivia.services.user.savedsearches.administration.portlet.model.UserSavedSearchesAdministrationForm"--%>
            <form:form action="${saveUrl}" method="post" modelAttribute="form">
                <c:choose>
                    <c:when test="${windowSettings.allCategories}">
                        <c:forEach var="entry" items="${form.categorizedSavedSearches}">
                            <c:set var="categoryId" value="${entry.key}" scope="request"/>
                            <c:set var="categorySearches" value="${entry.value}" scope="request"/>

                            <div class="card mb-3">
                                <div class="card-body">
                                    <h3 class="h5 card-title">
                                        <c:choose>
                                            <c:when test="${empty categoryId}"><op:translate
                                                    key="USER_SAVED_SEARCHES_ADMINISTRATION_DEFAULT_CATEGORY"/></c:when>
                                            <c:otherwise><op:translate key="USER_SAVED_SEARCHES_ADMINISTRATION_CATEGORY"
                                                                       args="${categoryId}"/></c:otherwise>
                                        </c:choose>
                                    </h3>

                                    <%@ include file="user-saved-searches.jspf" %>
                                </div>
                            </div>
                        </c:forEach>
                    </c:when>

                    <c:otherwise>
                        <c:set var="categoryId" value="${windowSettings.categoryId}" scope="request"/>
                        <c:set var="categorySearches" value="${form.categorizedSavedSearches[categoryId]}"
                               scope="request"/>
                        <%@ include file="user-saved-searches.jspf" %>
                    </c:otherwise>
                </c:choose>

                <input type="submit" class="d-none">
            </form:form>
        </c:otherwise>
    </c:choose>


    <%--Rename modal--%>
    <div id="${renameModalId}" class="modal fade" role="dialog">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title"><op:translate
                            key="USER_SAVED_SEARCHES_ADMINISTRATION_RENAME_MODAL_TITLE"/></h5>
                    <button type="button" class="close" data-dismiss="modal">
                        <span>&times;</span>
                    </button>
                </div>

                <div class="modal-body">
                    <form action="${renameUrl}" method="post">
                        <input type="hidden" name="id">
                        <div class="form-group">
                            <label for="${namespace}-display-name"><op:translate
                                    key="USER_SAVED_SEARCHES_ADMINISTRATION_RENAME_DISPLAY_NAME"/></label>
                            <input id="${namespace}-display-name" type="text" name="displayName" class="form-control">
                        </div>
                        <input type="submit" class="d-none">
                    </form>
                </div>

                <div class="modal-footer">
                    <%--Cancel--%>
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">
                        <span><op:translate key="CANCEL"/></span>
                    </button>

                    <%--Rename--%>
                    <button type="button" class="btn btn-primary" data-submit>
                        <span><op:translate key="USER_SAVED_SEARCHES_ADMINISTRATION_RENAME"/></span>
                    </button>
                </div>
            </div>
        </div>
    </div>


    <%--Delete modal confirmation--%>
    <div id="${deleteConfirmationModalId}" class="modal fade" role="dialog">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title"><op:translate
                            key="USER_SAVED_SEARCHES_ADMINISTRATION_DELETE_CONFIRMATION_MODAL_TITLE"/></h5>
                    <button type="button" class="close" data-dismiss="modal">
                        <span>&times;</span>
                    </button>
                </div>

                <div class="modal-body">
                    <p><op:translate key="USER_SAVED_SEARCHES_ADMINISTRATION_DELETE_CONFIRMATION_MODAL_BODY"/></p>
                    <form action="${deleteUrl}" method="post">
                        <input type="hidden" name="id">
                        <input type="submit" class="d-none">
                    </form>
                </div>

                <div class="modal-footer">
                    <%--Cancel--%>
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">
                        <span><op:translate key="CANCEL"/></span>
                    </button>

                    <%--Delete--%>
                    <button type="button" class="btn btn-primary" data-submit>
                        <span><op:translate key="USER_SAVED_SEARCHES_ADMINISTRATION_DELETE"/></span>
                    </button>
                </div>
            </div>
        </div>
    </div>
</div>
