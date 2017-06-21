<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>

<%@ page contentType="text/html" isELIgnored="false" %>


<portlet:defineObjects />

<portlet:actionURL name="modify" var="modifyUrl" />
<portlet:actionURL name="chgPwd" var="chgPwdUrl" />
<portlet:actionURL name="delete" var="deleteUrl">
    <portlet:param name="controller" value="edit" />
</portlet:actionURL>


<c:set var="namespace"><portlet:namespace /></c:set>



<div class="form-horizontal">
    <div class="form-group">
        <!-- Avatar -->
        <div class="col-sm-3 col-md-2">
            <img src="${card.avatar.url}" alt="" class="avatar avatar-big pull-right">
        </div>
        
        <!-- Display name -->
        <div class="col-sm-9 col-md-10">
            <h1>
                <c:if test="${not empty card.userConsulte.title}">
                    <span>${card.userConsulte.title}</span>
                </c:if>
                
                <span>${card.userConsulte.displayName}</span>
            </h1>
        </div>
    </div>
    
    <!-- Title -->
    <div class="form-group">
        <label class="col-sm-3 col-md-2 control-label"><op:translate key="label.title" /></label>
        <div class="col-sm-9 col-md-10">
            <p class="form-control-static">${card.userConsulte.title}</p>
        </div>
    </div>

    <!-- First name -->
    <div class="form-group">
        <label class="col-sm-3 col-md-2 control-label"><op:translate key="label.givenName" /></label>
        <div class="col-sm-9 col-md-10">
            <p class="form-control-static">${card.userConsulte.givenName}</p>
        </div>
    </div>

    <!-- Last name -->
    <div class="form-group">
        <label class="col-sm-3 col-md-2 control-label"><op:translate key="label.sn" /></label>
        <div class="col-sm-9 col-md-10">
            <p class="form-control-static">${card.userConsulte.sn}</p>
        </div>
    </div>
    
    <!-- Occupation -->
    <div class="form-group">
        <label class="col-sm-3 col-md-2 control-label"><op:translate key="label.occupation" /></label>
        <div class="col-sm-9 col-md-10">
            <p class="form-control-static">${card.nxProfile.occupation}</p>
        </div>
    </div>
    
    <!-- Institution -->
    <div class="form-group">
        <label class="col-sm-3 col-md-2 control-label"><op:translate key="label.institution" /></label>
        <div class="col-sm-9 col-md-10">
            <p class="form-control-static">${card.nxProfile.institution}</p>
        </div>
    </div>    
    
    <!-- Phone -->
    <div class="form-group">
        <label class="col-sm-3 col-md-2 control-label"><op:translate key="label.phone" /></label>
        <div class="col-sm-9 col-md-10">
            <p class="form-control-static">${card.nxProfile.phone}</p>
        </div>
    </div>

    <!-- Mobile phone -->
    <div class="form-group">
        <label class="col-sm-3 col-md-2 control-label"><op:translate key="label.mobilePhone" /></label>
        <div class="col-sm-9 col-md-10">
            <p class="form-control-static">${card.nxProfile.mobilePhone}</p>
        </div>
    </div>
    
    <!-- Mail -->
    <div class="form-group">
        <label class="col-sm-3 col-md-2 control-label"><op:translate key="label.mail" /></label>
        <div class="col-sm-9 col-md-10">
            <p class="form-control-static">${card.userConsulte.mail}</p>
        </div>
    </div>
</div>


<!-- Buttons -->
<c:if test="${card.levelEdition eq 'ALLOW'}">
    <div class="row">
        <div class="col-sm-offset-3 col-sm-9 col-md-offset-2 col-md-10">
            <!-- Edit -->
            <a href="${modifyUrl}" class="btn btn-primary">
                <i class="glyphicons glyphicons-pencil"></i>
                <span><op:translate key="label.btn.edit" /></span>
            </a>
            
            <!-- Change password -->
            <c:if test="${card.levelChgPwd eq 'ALLOW' || card.levelChgPwd eq 'OVERWRITE'}">
	            <a href="${chgPwdUrl}" class="btn btn-default">
	                <i class="glyphicons glyphicons-keys"></i>
	                <span><op:translate key="label.btn.chgpwd" /></span>
	            </a>
            </c:if>
            
            <!-- Delete -->
            <c:if test="${card.levelDeletion eq 'ALLOW'}">
                <button type="button" class="btn btn-danger pull-right" data-toggle="modal" data-target="#${namespace}-delete-modal">
                    <i class="glyphicons glyphicons-bin"></i>
                    <span><op:translate key="DELETE" /></span>
                </button>
            </c:if>
        </div>
    </div>
</c:if>


<!-- Confirm delete modal -->
<c:if test="${card.levelDeletion eq 'ALLOW'}">
    <div id="${namespace}-delete-modal" class="modal fade" role="dialog">
        <div class="modal-dialog modal-sm">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">
                        <i class="glyphicons glyphicons-remove"></i>
                        <span class="sr-only"><op:translate key="CLOSE" /></span>
                    </button>

                    <h4 class="modal-title"><op:translate key="DELETE_PERSON_MODAL_TITLE" /></h4>
                </div>

                <div class="modal-body">
                    <p><op:translate key="DELETE_PERSON_MODAL_MESSAGE" /></p>
                </div>

                <div class="modal-footer">
                    <a href="${deleteUrl}" class="btn btn-danger" data-dismiss="modal">
                        <i class="glyphicons glyphicons-bin"></i>
                        <span><op:translate key="DELETE" /></span>
                    </a>

                    <button type="button" class="btn btn-default" data-dismiss="modal">
                        <span><op:translate key="CANCEL" /></span>
                    </button>
                </div>
            </div>
        </div>
    </div>
</c:if>
