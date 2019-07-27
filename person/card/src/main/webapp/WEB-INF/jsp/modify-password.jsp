<%@ include file="/WEB-INF/jsp/include.jsp" %>


<portlet:defineObjects/>

<portlet:actionURL name="edit" var="editUrl">
    <portlet:param name="controller" value="chgPwd"/>
</portlet:actionURL>

<portlet:resourceURL id="password-information" var="passwordInformationUrl"/>


<div class="person-card-password-edition">
    <form:form method="post" action="${editUrl}" cssClass="form-horizontal" modelAttribute="formChgPwd" role="form">
        <c:if test="${card.levelChgPwd eq 'ALLOW'}">
            <spring:bind path="currentPwd">
                <div class="form-group required ${status.error ? 'has-error has-feedback' : ''}">
                    <form:label path="currentPwd" cssClass="col-md-3 control-label"><op:translate
                            key="label.pwd.current"/></form:label>
                    <div class="col-md-9">
                        <form:input path="currentPwd" type="password" cssClass="form-control"/>
                        <c:if test="${status.error}">
                            <span class="form-control-feedback">
                                <i class="glyphicons glyphicons-remove"></i>
                            </span>
                        </c:if>
                        <form:errors path="currentPwd" cssClass="help-block"/>
                    </div>
                </div>
            </spring:bind>
        </c:if>

        <spring:bind path="newPwd">
            <div class="form-group required ${status.error ? 'has-error has-feedback' : ''}">
                <form:label path="newPwd" cssClass="col-md-3 control-label"><op:translate
                        key="label.pwd.new"/></form:label>
                <div class="col-md-9">
                    <form:input path="newPwd" type="password" cssClass="form-control password"/>
                    <c:if test="${status.error}">
                        <span class="form-control-feedback">
                            <i class="glyphicons glyphicons-remove"></i>
                        </span>
                    </c:if>
                    <form:errors path="newPwd" cssClass="help-block"/>
                </div>
            </div>

            <%--Password rules information--%>
            <div class="row">
                <div class="col-md-offset-3 col-md-9">
                    <div class="panel panel-default">
                        <div class="panel-body">
                            <p><op:translate key="PASSWORD_INFORMATION_TITLE"/></p>
                            <div data-password-information-placeholder data-url="${passwordInformationUrl}"></div>
                        </div>
                    </div>
                </div>
            </div>
        </spring:bind>

        <spring:bind path="confirmPwd">
            <div class="form-group required ${status.error ? 'has-error has-feedback' : ''}">
                <form:label path="confirmPwd" cssClass="col-md-3 control-label"><op:translate
                        key="label.pwd.confirm"/></form:label>
                <div class="col-md-9">
                    <form:input path="confirmPwd" type="password" cssClass="form-control"/>
                    <c:if test="${status.error}">
                    <span class="form-control-feedback">
                        <i class="glyphicons glyphicons-remove"></i>
                    </span>
                    </c:if>
                    <form:errors path="confirmPwd" cssClass="help-block"/>
                </div>
            </div>
        </spring:bind>

        <!-- Buttons -->
        <div class="row">
            <div class="col-md-offset-3 col-md-9">
                <!-- Save -->
                <button type="submit" name="save" class="btn btn-primary">
                    <i class="glyphicons glyphicons-floppy-disk"></i>
                    <span><op:translate key="SAVE"/></span>
                </button>

                <!-- Cancel -->
                <button type="submit" name="cancel" class="btn btn-default">
                    <span><op:translate key="CANCEL"/></span>
                </button>
            </div>
        </div>
    </form:form>
</div>
