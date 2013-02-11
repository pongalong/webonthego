<%@ include file="/WEB-INF/views/include/header.jsp"%>

<div>
  <h3>Your account has been created!</h3>
  <p>You can activate your device now, or the next time you login.</p>

  <div class="buttons">
    <a href="<spring:url value='/' />" class="mBtn" onClick="openPrompt('confirmActivate')">Later </a> <a href="<spring:url value='/activate' />" class="mBtn">Activate
      Now </a>
  </div>
</div>

<div id="confirmActivate" class="prompt">
  <img src="<spring:url value="/static/images/buttons/icons/error.png" />" class="closePrompt" />
  <p>Are you sure?</p>
  <p>You won't be able to use your device until you activate.</p>
  <div class="buttons" style="margin-top: 40px;">
    <a class="mBtn" href="<spring:url value="/" />">Logout</a> <a href="<spring:url value='/activate' />" class="mBtn">Activate Now </a>
  </div>
</div>

<%@ include file="/WEB-INF/views/include/footer.jsp"%>