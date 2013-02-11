<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>
  <title>Web on the Go &#8480; Account Management</title>
  <%@ include file="/WEB-INF/views/include/headTags/meta.jsp"%>
  <%@ include file="/WEB-INF/views/include/headTags/styles.jsp"%>
  <script type="text/javascript" src="<spring:url value="/static/javascript/jquery.min.js"/>"></script>
  <script type="text/javascript" src="<spring:url value="/static/javascript/jqueryEasing.js"/>"></script>
</head>

<body>

  <div id="wrapper">

    <c:if test="${not empty CONTROLLING_USER}">
      <sec:authorize ifAnyGranted="ROLE_ADMIN, ROLE_MANAGER, ROLE_AGENT, ROLE_SU">
        <%@ include file="/WEB-INF/views/include/admin/control_bar.jsp"%>
      </sec:authorize>
    </c:if>

    <div id="rt-header">
      <div style="position: relative; width: 950px; margin: 0 auto;">

        <ul class="menutop">
          <li class="mBtn" onclick="location.href='<spring:url value="/" />'">Home</li>
          <li class="mBtn" onclick="location.href='https://store.webonthego.com/'">Store</li>
          <li class="mBtn" onclick="location.href='<spring:url value="/support" />'">Support</li>
        </ul>

        <c:if test="${USER.userId > 0 && CONTROLLING_USER.userId <= 0}">
          <div style="position: absolute; right: 0; text-align: right;">
            Welcome ${USER.contactInfo.firstName} ${USER.contactInfo.lastName}<br /> <a href="<spring:url value='/logout' />">Logout</a>
          </div>
        </c:if>

        <div id="logo">
          <img src="<spring:url value="/static/images/logo.png" />" alt="Web on The Go" onClick="location.href='<spring:url value="/" />';" />
        </div>

      </div>
    </div>

    <div id="container">
      <div class="mainbody">