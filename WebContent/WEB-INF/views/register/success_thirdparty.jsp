<%@ include file="/WEB-INF/views/include/header/headerNoMenu.jsp"%>

<h3>Account ${user.email} Has Been Created!</h3>

<div class="well">
  <p>${user.email} has been successfully registered.</p>
  <p>Upon logging in, user will be prompted to complete their account by activating a device. This will require that they have:</p>
  <ul>
    <li>A new or unregistered WebOnTheGo device</li>
    <li>ESN located on the device and box</li>
    <li>Valid credit card for activation and subsequent topups</li>
  </ul>
</div>

<a href="<spring:url value="/" />" class="button">Done</a>

<%@ include file="/WEB-INF/views/include/footer/footerNoMenu.jsp"%>