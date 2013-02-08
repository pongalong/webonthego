<%@ include file="/WEB-INF/views/include/taglibs.jsp"%>
<%@ include file="/WEB-INF/views/include/doctype.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>Web on the Go &#8480; Account Management</title>
<%@ include file="/WEB-INF/views/include/headTags.jsp"%>
</head>
<body>
  <%@ include file="/WEB-INF/views/include/popups.jsp"%>


  <%@ include file="/WEB-INF/views/include/header.jsp"%>


  <div class="container">
    <div class="mainbody">
      <div class="span-18 colborder">
        <h3 style="margin-bottom: 10px; padding-bottom: 0px; border-bottom: 1px #ccc dotted;">Our Apologies, There was a problem</h3>
        <p style="font-size: 1.3em;">
          Your request could not be completed at this time and no changes were made to your account. Please try again later or contact Web on the Go &#8480;
          Customer Support <a href="https://account.webonthego.com/support">http://account.webonthego.com/support</a>.
        </p>
        <sec:authorize access="hasRole('ROLE_ADMIN')">
          <hr />
          <%
          	try {
          			// The Servlet spec guarantees this attribute will be available
          			Throwable exception = (Throwable) request.getAttribute("javax.servlet.error.exception");
          			if (exception != null) {
          				if (exception instanceof ServletException) {
          					// It's a ServletException: we should extract the root cause
          					ServletException sex = (ServletException) exception;
          					Throwable rootCause = sex.getRootCause();
          					if (rootCause == null)
          						rootCause = sex;
          					out.println("** Root cause is: " + rootCause.getMessage());
          					rootCause.printStackTrace(new java.io.PrintWriter(out));
          				} else {
          					// It's not a ServletException, so we'll just show it
          					exception.printStackTrace(new java.io.PrintWriter(out));
          				}
          			} else {
          				out.println("No error information available");
          			}
          			// Display cookies
          			out.println("\nCookies:\n");
          			Cookie[] cookies = request.getCookies();
          			if (cookies != null) {
          				for (int i = 0; i < cookies.length; i++) {
          					out.println(cookies[i].getName() + "=[" + cookies[i].getValue() + "]");
          				}
          			}
          		} catch (Exception ex) {
          			ex.printStackTrace(new java.io.PrintWriter(out));
          		}
          %>
        </sec:authorize>
      </div>

      <div class="span-6 last accountNav">
        <%@ include file="/WEB-INF/views/include/navigation/accountNav.jsp"%>
      </div>

      <div class="clear"></div>
    </div>
    <!-- Close main-content -->

  </div>
  <!-- Close container -->
  <%@ include file="/WEB-INF/views/include/footer_nolinks.jsp"%>
</body>
</html>