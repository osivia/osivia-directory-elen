<%@ include file="/WEB-INF/jsp/include.jsp"%>
<script type="text/javascript">
    var $JQry = jQuery.noConflict();

    $JQry(function() {
        $JQry('.password').pstrength();
    });
</script>


<portlet:defineObjects />

<portlet:actionURL name="edit" var="editUrl">
    <portlet:param name="controller" value="chgPwd" />
</portlet:actionURL>



<form:form  method="post" action="${editUrl}" cssClass="form-horizontal" modelAttribute="formChgPwd" role="form">


    <c:if test="${card.levelChgPwd eq 'ALLOW'}">
        <div class="form-group">
            <form:label path="currentPwd" cssClass="col-md-2 control-label">
                <op:translate  key="label.pwd.current" />
            </form:label>

            <div class="col-md-10"><form:input path="currentPwd" type="password" cssClass="form-control"/>
                <form:errors path="currentPwd" />
            </div>
        </div>
    </c:if>


    <div class="form-group">
        <form:label path="newPwd" cssClass="col-md-2 control-label">
            <op:translate  key="label.pwd.new" />
        </form:label>

        <div class="col-md-10"><form:input path="newPwd" type="password" cssClass="form-control password"/>
            <form:errors path="newPwd" />
        </div>
    </div>

    <div class="form-group">
        <form:label path="confirmPwd" cssClass="col-md-2 control-label">
            <op:translate  key="label.pwd.confirm" />
        </form:label>

        <div class="col-md-10"><form:input path="confirmPwd" type="password" cssClass="form-control"/>
            <form:errors path="confirmPwd" />
        </div>
    </div>

    <!-- Buttons -->
    <div class="row">
        <div class="col-sm-offset-3 col-sm-9 col-lg-offset-2 col-lg-10">
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
    </div>


</form:form>


