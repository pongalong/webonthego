<%@ include file="/WEB-INF/views/include/header/headerNoMenu.jsp"%>

<form:form commandName="registrationTerms" method="post">
  <fieldset>
    <legend>Terms and Conditions</legend>

    <!-- Begin Errors -->
    <c:if test="${not empty requestScope['org.springframework.validation.BindingResult.registrationTerms'].allErrors}">
      <div class="alert alert-error">
        <button type="button" class="close" data-dismiss="alert">&times;</button>
        <h4>Please correct the following problems</h4>
        <form:errors path="acceptTerms" />
        <!-- Global Errors -->
        <spring:bind path="registrationTerms">
          <c:forEach items="${status.errorMessages}" var="error" varStatus="status">
            <span id="global.${status.index}.errors"><c:out value="${error}" /></span>
          </c:forEach>
        </spring:bind>
      </div>
    </c:if>

    <!-- Display Plans -->
    <div class="registrationTerms"><%@ include file="/WEB-INF/views/terms/registration_tos.jsp"%></div>

    <!-- Term Acceptance -->
    <form:label path="acceptTerms" cssClass="checkbox">
      <form:checkbox path="acceptTerms" />
      <span onclick="$('#acceptTerms1').click()">I have read and understand the terms and conditions</span>
    </form:label>

    <button type="submit" class="button" name="_eventId_submit" style="margin-top: 20px;">Continue</button>

  </fieldset>
</form:form>

<%@ include file="/WEB-INF/views/include/footer/footerNoMenu.jsp"%>