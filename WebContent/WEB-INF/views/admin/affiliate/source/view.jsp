<%@ include file="/WEB-INF/views/include/header/headerAndMenu.jsp"%>

<h3>${sourceCode.name}</h3>

<table>
  <tr>
    <th>ID</th>
    <th>Name</th>
    <th>Code</th>
  </tr>

  <tr>
    <td>${sourceCode.id}</a></td>
    <td>${sourceCode.name}</a></td>
    <td>${sourceCode.code}</td>
  </tr>

</table>

<%@ include file="/WEB-INF/views/include/footer/footerAndMenu.jsp"%>