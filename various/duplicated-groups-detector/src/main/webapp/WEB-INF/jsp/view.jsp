<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%@ page contentType="text/html" isELIgnored="false" %>

<dl>
    <c:forEach var="entry" items="${duplicatedGroups.map}">
        <c:set var="groups" value="${entry.value}" />
        
        <c:if test="${fn:length(groups) > 1}">
            <dt>${entry.key.workspaceId}</dt>
            <dd>
                <ol>
                    <c:forEach var="group" items="${groups}">
                        <li>
                            <span>${group.displayName}</span>
                            
                            <c:if test="${not empty group.description}">
                                <br>
                                <small class="text-muted">${group.description}</small>    
                            </c:if>
                        </li>
                    </c:forEach>
                </ol>
            </dd>
        </c:if>
    </c:forEach>
</dl>
