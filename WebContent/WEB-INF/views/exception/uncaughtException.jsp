<%@ include file="/WEB-INF/views/include/header/headerAndMenu.jsp"%>

<h3>We're sorry, an unexpected error occurred</h3>

<p>
  Your changes were not saved to your account. Please try again later, or contact <a href="https://account.webonthego.com/support">Web on the Go Support</a>.
</p>

<sec:authorize access="hasRole('ROLE_SU')">
  <div>
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
  </div>
</sec:authorize>

<%@ include file="/WEB-INF/views/include/footer/footerAndMenu.jsp"%>