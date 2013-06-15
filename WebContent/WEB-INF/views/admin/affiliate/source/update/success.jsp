<%@ include file="/WEB-INF/views/include/header/headerAndMenu.jsp"%>

<div class="alert alert-success">
  <p>Source code created!</p>
  <ul class="info">
    <li><span>Name:</span>${sourceCode.name}</li>
    <li><span>Code:</span>${sourceCode.code}</li>
    <li><span>Parent:</span>${sourceCode.parent.name}</li>
  </ul>
</div>

<%@ include file="/WEB-INF/views/include/footer/footerAndMenu.jsp"%>