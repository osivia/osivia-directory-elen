<%@page import="javax.portlet.WindowState"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>
<%@ taglib prefix="display" uri="http://displaytag.sf.net" %>

<%@ page contentType="text/plain; charset=UTF-8"%>


<portlet:defineObjects />

<portlet:actionURL name="edit" var="editUrl">
	<portlet:param name="controller" value="edit" />
</portlet:actionURL>



<c:set var="namespace"><portlet:namespace /></c:set>


<div class="person-card-edition">


	<form:form  method="post" action="${editUrl}" enctype="multipart/form-data" cssClass="form-horizontal" modelAttribute="formEdition" role="form">
		
		<!-- Avatar -->
	    <div class="form-group">
	        <form:label path="avatar.upload" cssClass="col-sm-3 col-lg-6 control-label"><op:translate key="label.avatar" /></form:label>
	        <div class="col-sm-9 col-lg-6">
	            <!-- Preview -->
	            <c:choose>
	                <c:when test="${formEdition.avatar.updated}">
	                    <!-- Preview -->
	                    <portlet:resourceURL id="avatarPreview" var="previewUrl">
	                        <portlet:param name="ts" value="${currentDate.time}" />
	                    </portlet:resourceURL>
	                    <p>
	                        <img src="${previewUrl}" alt="" class="img-responsive">
	                    </p>
	                </c:when>
	                
	                <c:when test="${formEdition.avatar.deleted}">
	                    <!-- Deleted avatar -->
	                    <p class="form-control-static text-muted">
	                        <span><op:translate key="label.avatar.delete" /></span>
	                    </p>
	                </c:when>
	            
	                <c:when test="${empty formEdition.avatar.url}">
	                    <!-- No avatar -->
	                    <p class="form-control-static text-muted">
	                        <span><op:translate key="WORKSPACE_NO_VIGNETTE" /></span>
	                    </p>
	                </c:when>
	                
	                <c:otherwise>
	                    <!-- avatar -->
	                    <p>
	                        <img src="${formEdition.avatar.url}" alt="" class="img-responsive">
	                    </p>
	                </c:otherwise>
	            </c:choose>
	        
	            <div>
	                <!-- Upload -->
	                <label class="btn btn-sm btn-default btn-file">
	                    <i class="halflings halflings-folder-open"></i>
	                    <span><op:translate key="label.avatar.upload" /></span>
	                    <form:input type="file" path="avatar.upload" />
	                </label>
	                <input type="submit" name="upload-avatar" class="hidden">
	                
	                <!-- Delete -->
	                <button type="submit" name="delete-avatar" class="btn btn-sm btn-default">
	                    <i class="halflings halflings-trash"></i>
	                    <span class="sr-only"><op:translate key="label.avatar.delete" /></span>
	                </button>
	            </div>
	        </div>
	    </div>
	
		<div class="form-group">
			<form:label path="title" cssClass="col-md-2 control-label">
				<op:translate  key="label.title" />	
			</form:label>	
		
			<div class="col-md-10">
				<form:select path="title" cssClass="form-control">
					<form:option value=""></form:option>
					<!-- TODO traduction -->
					
					<form:option value="M.">M.</form:option>
					<form:option value="Mme">Mme</form:option>
				</form:select>
					
			</div>
		</div>
	
	
	
		<div class="form-group">
			<form:label path="sn" cssClass="col-md-2 control-label">
				<op:translate  key="label.sn" />
			
			</form:label>	
		
			<div class="col-md-10">
				<form:input path="sn" cssClass="form-control"/>
				<font style="color: #C11B17;"><form:errors path="sn"/></font>
					
			</div>
		</div>
		<div class="form-group">
			<form:label path="givenName" cssClass="col-md-2 control-label">
				<op:translate  key="label.givenName" />
				
		
			</form:label>	
		
			<div class="col-md-10">
				<form:input path="givenName" name="givenName" cssClass="form-control"/>
				<form:errors path="givenName"/>
					
			</div>
		</div>	
	
		<div class="form-group">
			<form:label path="mail" cssClass="col-md-2 control-label">
				<op:translate  key="label.mail" />
	
			</form:label>
			
			<div class="col-md-10">
				<form:input path="mail" cssClass="form-control"/>
				<form:errors path="mail" />
			</div>
		
		</div>
	
	    <div class="form-group">
	        <form:label path="occupation" cssClass="col-md-2 control-label"><op:translate key="label.occupation" /></form:label>
	        <div class="col-md-10">
	            <form:input path="occupation" cssClass="form-control" />
	        </div>
	    </div>
	    
	    <div class="form-group">
	        <form:label path="institution" cssClass="col-md-2 control-label"><op:translate key="label.institution" /></form:label>
	        <div class="col-md-10">
	            <form:input path="institution" cssClass="form-control" />
	        </div>
	    </div>	    
	
		<div class="form-group"> 	
			<form:label path="phone" cssClass="col-md-2 control-label">
				<op:translate  key="label.phone" />
		</form:label>
		
			<div class="col-md-10">
				<form:input path="phone" cssClass="form-control"/>
				<form:errors path="phone"/>
			</div>
		</div>
		<div class="form-group"> 	
			<form:label path="mobilePhone" cssClass="col-md-2 control-label">
				<op:translate  key="label.mobilePhone" />
			
			</form:label>
		
			<div class="col-md-10">
				<form:input path="mobilePhone" cssClass="form-control"/>
				<form:errors path="mobilePhone"/>
			</div>
		</div>
		
		<div class="form-group"> 	
			<form:label path="shownInSearch" cssClass="col-md-2 control-label">
				<op:translate  key="label.showninsearch" />
			</form:label>
		
			<div class="col-md-10">
				<div class="checkbox">
					<label>
						<form:checkbox path="shownInSearch" />
						<op:translate  key="label.showninsearch.description" />
					</label>
				</div>
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


</div>	