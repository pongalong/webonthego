<%@ include file="/WEB-INF/views/include/header.jsp"%>

<div class="span-18 colborder">
  <h3>View Users</h3>

  <form:form id="viewUsers" cssClass="validatedForm" commandName="newInternalUser" method="post">

    <div class="row clearfix">
      <label class="required">Role</label> <select name="user_role" class="span-8" style="width: 312px;">
        <c:forEach var="availableRole" items="${availableRoles}">
          <c:if test="${availableRole != 'ROLE_ANONYMOUS' }">
            <option value="${availableRole}">${availableRole.name}</option>
          </c:if>
        </c:forEach>
      </select>
    </div>

    <div class="buttons">
      <input type="submit" value="View" />
    </div>

  </form:form>
</div>


<div class="span-6 last accountNav">
  <%@ include file="/WEB-INF/views/include/navigation/accountNav.jsp"%>
</div>

<%@ include file="/WEB-INF/views/include/footer.jsp"%>