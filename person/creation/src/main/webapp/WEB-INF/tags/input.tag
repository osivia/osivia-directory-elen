<%@tag
	description="Extended input tag to allow for sophisticated errors"
	pageEncoding="UTF-8"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="is" uri="internationalization"%>
<%@attribute name="path" required="true" type="java.lang.String"%>


<spring:bind path="${path}">

	<c:if test="${status.error}">
		<c:set var="formGroupClass" value="has-error has-feedback" />
	</c:if>

	<div class="form-group ${formGroupClass}">
		<form:label path="${path}" cssClass="col-md-4 control-label">
			<is:getProperty key="label.${path}" />
		</form:label>

		<div class="col-md-8">
			<form:input path="${path}" cssClass="form-control" />
			<c:if test="${status.error}">
				<span class="glyphicons halflings remove form-control-feedback"></span>
				<form:errors path="${path}" cssClass="help-block" />
			</c:if>

		</div>
	</div>

</spring:bind>