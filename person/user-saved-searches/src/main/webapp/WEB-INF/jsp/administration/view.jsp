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


<div class="user-saved-searches-administration">
    <c:choose>
        <c:when test="${empty form.savedSearches}">
            <p class="text-secondary"><op:translate key="USER_SAVED_SEARCHES_ADMINISTRATION_EMPTY"/></p>
        </c:when>

        <c:otherwise>
            <form:form action="${saveUrl}" method="post" modelAttribute="form">
                <ul class="list-unstyled user-saved-searches-administration-sortable">
                    <c:forEach var="savedSearch" items="${form.savedSearches}" varStatus="status">
                        <li>
                            <form:hidden path="savedSearches[${status.index}].order"/>

                            <div class="card mb-2">
                                <div class="card-body p-2">
                                    <div class="d-flex align-items-center">
                                        <div class="">
                                            <a href="javascript:"
                                               class="d-block p-2 text-secondary no-ajax-link user-saved-searches-administration-sortable-handle">
                                                <i class="glyphicons glyphicons-basic-sort"></i>
                                                <span class="sr-only"><op:translate
                                                        key="USER_SAVED_SEARCHES_ADMINISTRATION_MOVE"/></span>
                                            </a>
                                        </div>

                                        <div class="flex-grow-1 my-1 mx-2">
                                            <h5 class="card-title mb-1">${savedSearch.displayName}</h5>
                                            <div>
                                                    <%--Rename--%>
                                                <a href="javascript:" class="card-link no-ajax-link" data-toggle="modal"
                                                   data-target="#${renameModalId}" data-id="${savedSearch.id}"
                                                   data-display-name="${savedSearch.displayName}">
                                                    <span><op:translate
                                                            key="USER_SAVED_SEARCHES_ADMINISTRATION_RENAME"/></span>
                                                </a>

                                                    <%--Delete--%>
                                                <a href="javascript:" class="card-link no-ajax-link" data-toggle="modal"
                                                   data-target="#${deleteConfirmationModalId}"
                                                   data-id="${savedSearch.id}">
                                                    <span><op:translate
                                                            key="USER_SAVED_SEARCHES_ADMINISTRATION_DELETE"/></span>
                                                </a>
                                            </div>
                                        </div>

                                        <div class="align-self-stretch">
                                            <div class="d-flex flex-column justify-content-between h-100">
                                                <div>
                                                    <c:if test="${not status.first}">
                                                        <portlet:actionURL name="move" var="moveUpUrl">
                                                            <portlet:param name="id" value="${savedSearch.id}"/>
                                                            <portlet:param name="direction" value="up"/>
                                                        </portlet:actionURL>
                                                        <a href="${moveUpUrl}" class="btn btn-link btn-sm">
                                                            <i class="glyphicons glyphicons-basic-arrow-up"></i>
                                                            <span class="sr-only"><op:translate
                                                                    key="USER_SAVED_SEARCHES_ADMINISTRATION_MOVE_UP"/></span>
                                                        </a>
                                                    </c:if>
                                                </div>

                                                <div>
                                                    <c:if test="${not status.last}">
                                                        <portlet:actionURL name="move" var="moveDownUrl">
                                                            <portlet:param name="id" value="${savedSearch.id}"/>
                                                            <portlet:param name="direction" value="down"/>
                                                        </portlet:actionURL>
                                                        <a href="${moveDownUrl}" class="btn btn-link btn-sm">
                                                            <i class="glyphicons glyphicons-basic-arrow-down"></i>
                                                            <span class="sr-only"><op:translate
                                                                    key="USER_SAVED_SEARCHES_ADMINISTRATION_MOVE_DOWN"/></span>
                                                        </a>
                                                    </c:if>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </li>
                    </c:forEach>
                </ul>

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
