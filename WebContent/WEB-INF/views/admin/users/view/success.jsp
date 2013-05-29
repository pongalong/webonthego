<%@ include file="/WEB-INF/views/include/header/headerAndMenu.jsp"%>

<h3>Users (${requestedRole.name})</h3>

<c:choose>
  <c:when test="${not empty members}">

    <table>
      <tr>
        <th>ID</th>
        <th>Username</th>
        <th>Email</th>
        <th>Enabled</th>
      </tr>

      <c:forEach var="member" items="${members}" varStatus="status">
        <tr>
          <td>${member.userId}</td>
          <td>${member.username}</td>
          <td>${member.email}</td>
          <td><c:choose>
              <c:when test="${member.enabled}">
                <a href="#disableUser${status.index}" role="button" class="btn" data-toggle="modal">Disable User</a>
              </c:when>
              <c:otherwise>
                <a href="#enableUser${status.index}" role="button" class="btn" data-toggle="modal">Enable User</a>
              </c:otherwise>
            </c:choose></td>
        </tr>

        <!-- Modal -->
        <div id="enableUser${status.index}" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="enableUserLabel${status.index}"
          aria-hidden="true">
          <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
            <h3 id="enableUserLabel">Enable user ${member.email}</h3>
          </div>
          <div class="modal-body">
            <p>Are you sure you want to enable user ${member.email}?</p>
            <p>This will allow the user to login with his given credentials. Their device is unaffected.</p>
          </div>
          <div class="modal-footer">
            <button class="btn" data-dismiss="modal" aria-hidden="true">Cancel</button>
            <button class="btn btn-primary" onclick="location.href='<spring:url value="/admin/user/toggle/${member.userId}"/>'">Enable</button>
          </div>
        </div>

        <!-- Modal -->
        <div id="disableUser${status.index}" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="disableUserLabel${status.index}"
          aria-hidden="true">
          <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
            <h3 id="disableUserLabel">Disable user ${member.email}</h3>
          </div>
          <div class="modal-body">
            <p>Are you sure you want to disable user ${member.email}?</p>
            <p>This will prevent the user from logging in. Their device is unaffected.</p>
          </div>
          <div class="modal-footer">
            <button class="btn" data-dismiss="modal" aria-hidden="true">Cancel</button>
            <button class="btn btn-primary" onclick="location.href='<spring:url value="/admin/user/toggle/${member.userId}"/>'">Disable</button>
          </div>
        </div>

      </c:forEach>
    </table>
  </c:when>
  <c:otherwise>
      No users.
    </c:otherwise>
</c:choose>

<%@ include file="/WEB-INF/views/include/footer/footerAndMenu.jsp"%>