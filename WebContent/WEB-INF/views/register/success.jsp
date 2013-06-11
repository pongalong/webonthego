<%@ include file="/WEB-INF/views/include/header/headerNoMenu.jsp"%>

<h3>Your account has been created!</h3>

<div class="well">
  <p>You can activate your device now, or the next time you login.</p>
</div>

<a href="#activateLater" role="button" class="button" data-toggle="modal">Later</a>
<a href="<spring:url value='/activate' />" class="button">Activate Now</a>

<!-- Modal -->
<div id="activateLater" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="activateLaterLabel" aria-hidden="true">
  <div class="modal-header">
    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
    <h3 id="activateLaterLabel">Activate Later?</h3>
  </div>
  <div class="modal-body">
    <p>Are you sure you want to activate later? You won't be able to use your device until you activate.</p>
  </div>
  <div class="modal-footer">
    <button class="btn" data-dismiss="modal" aria-hidden="true">Cancel</button>
    <button class="btn btn-primary" onclick="location.href='<spring:url value="/"/>'">Later</button>
  </div>
</div>

<%@ include file="/WEB-INF/views/include/footer/footerNoMenu.jsp"%>