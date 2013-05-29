<%@ include file="/WEB-INF/views/include/header/header.jsp"%>
<div>

  <h3>Your device has been activated!</h3>

  <p>You can now connect to the internet!</p>
  <p>Please follow the instructions that came in the box with your device to connect. Or try one of our set up guides.</p>

  <div>
    <ul>
      <li><a href="http://webonthego.com/wp-content/uploads/2013/02/USB-Set-Up-Guide.pdf">USB Quick Start Guide</a></li>
      <li><a href="http://webonthego.com/wp-content/uploads/2013/02/Mobile-Hotspot-Set-Up-Guide.pdf">MiFi Quick Start Guide</a></li>
    </ul>
  </div>

  <div class="buttons">
    <a href="<spring:url value='/' />" class="mBtn">Home </a>
  </div>
</div>

<%@ include file="/WEB-INF/views/include/footer/footer.jsp"%>