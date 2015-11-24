<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>


<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0" %>


<portlet:defineObjects/>


<portlet:actionURL var="setAdminProperty" portletMode="admin"><portlet:param name="action" value="setAdminProperty" /></portlet:actionURL>
<portlet:actionURL var="annuler" portletMode="admin"><portlet:param name="action" value="annuler" /></portlet:actionURL>


<div>
	<form:form method="post" modelAttribute="formAdmin"  action="${setAdminProperty}">  	
	
		
		<div class="ligne">
			Indiquez le path nuxeo du dossier parent : 

			<form:input path="nuxeopath" name="nuxeopath"/>
		</div>	
		
		<div class="ligne">
			<font style="font-style:italic;">Vous pouvez indiquer un path en dur, ou utiliser la variable &#36{basePath} pour les pages dynamiques</font>
		</div>
		<div class="ligne">
			<font style="font-style:italic;">ex: &#36{basePath}, ../&#36{basePath\},...</font>
		</div>
	
		<div class="ligne">
		
			<div class="bouton">
				<input type="submit" name="valider" value="Valider" />
			</div>
		
			<div class="bouton"> 
				<input type="button" name="annuler" id="annuler" value="Annuler" onclick="updatePortletContent(this,'${annuler}');" />
			</div>
			
		</div>
	
	</form:form>
	
</div>