<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>

<%@ page contentType="text/html" isELIgnored="false"%>


<portlet:defineObjects />


<div class="workspace-member-management">
    <!-- Member list -->
    <jsp:include page="list.jsp" />
    
    <!-- Add members -->
    <jsp:include page="add.jsp" />
</div>
