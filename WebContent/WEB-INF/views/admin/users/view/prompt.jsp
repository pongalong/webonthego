<%@ include file="/WEB-INF/views/include/header/headerAndMenu.jsp"%>

<form method="post" class="form-horizontal">
  <fieldset>

    <legend>View Internal Users</legend>

    <div class="control-group">
      <label class="control-label">Role</label>
      <div class="controls">
        <select name="user_role" class="span5">
          <c:forEach var="availableRole" items="${availableRoles}">
            <c:if test="${availableRole != 'ROLE_ANONYMOUS' }">
              <option value="${availableRole}">${availableRole.name}</option>
            </c:if>
          </c:forEach>
        </select>
      </div>
    </div>

    <div class="controls">
      <button type="submit" class="button">View Users</button>
    </div>

  </fieldset>
</form>

<%@ include file="/WEB-INF/views/include/footer/footerAndMenu.jsp"%>