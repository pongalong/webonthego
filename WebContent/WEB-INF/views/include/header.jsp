<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>

<!DOCTYPE html>

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en-US" lang="en-US">

<head>
<title>Web on the Go &#8480; Account Management</title>

<meta charset="utf-8">
<meta http-equiv="Cache-control" content="Public" />
<meta http-equiv="Accept-Encoding" content="gzip, deflate" />
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<meta http-equiv="Content-Type" content="text/html" />
<meta http-equiv="Content-Style-Type" content="text/css" />
<meta name="language" content="ES" />
<meta name="robots" content="INDEX,FOLLOW" />
<meta name="revisit-after" content="1 days" />
<meta name="document-class" content="Completed" />
<meta name="document-classification" content="Communications" />
<meta name="document-rights" content="Copyrighted Work" />
<meta name="document-type" content="Public" />
<meta name="document-rating" content="General" />
<meta name="document-distribution" content="Global" />
<meta name="document-state" content="Dynamic" />
<meta http-equiv="Content-Language" content="en-us" />

<link rel="shortcut icon" href="<spring:url value='/static/images/favicon.ico' />" />
<link rel="stylesheet" type="text/css" href="<spring:url value='/static/styles/h5bp/normalize.min.css' />" />
<link rel="stylesheet" type="text/css" href="<spring:url value='/static/styles/h5bp/main.css' />" />
<link rel="stylesheet" type="text/css" href="<spring:url value='/static/styles/bootstrap/bootstrap.min.css' />" />
<link rel="stylesheet" type="text/css" href="<spring:url value='/static/styles/imported/grid.min.css' />" />

<link rel="stylesheet" type="text/css" href="<spring:url value='/static/styles/imported/forms.css' />" />

<link rel="stylesheet" type="text/css" href="<spring:url value='/static/styles/wotg/grid-12.css' />" />
<link rel="stylesheet" type="text/css" href="<spring:url value='/static/styles/wotg/gantry.css' />" />
<link rel="stylesheet" type="text/css" href="<spring:url value='/static/styles/wotg/wotg.min.css' />" />
<link rel="stylesheet" type="text/css" href="<spring:url value='/static/styles/wotg/style.min.css' />" />

<link rel="stylesheet" type="text/css" href="<spring:url value='/static/styles/dropdown.css' />" />

<link rel="stylesheet" type="text/css" href="<spring:url value='/static/styles/forms.css' />" />

<link rel="stylesheet" type="text/css" href="<spring:url value='/static/styles/curtain.min.css' />" />


<!--[if IE 6]>
    <link href="<spring:url value="/static/styles/imported/grid_ie.css" htmlEscape="true" />" rel="stylesheet" type="text/css" />
  <![endif]-->

<!--[if IE 7]>
    <link href="<spring:url value="/static/styles/imported/grid_ie.css" htmlEscape="true" />" rel="stylesheet" type="text/css" />
  <![endif]-->

<script type="text/javascript" src="<spring:url value='/static/javascript/jquery/jquery.min.js' />"></script>
<script type="text/javascript" src="<spring:url value='/static/javascript/jquery/jqueryEasing.js' />"></script>
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
          <li class="mBtn" onclick="location.href='http://www.webonthego.com/'">Home</li>
          <li class="mBtn" onclick="location.href='http://store.webonthego.com/'">Store</li>
          <li class="mBtn" onclick="location.href='<spring:url value="/support" />'">Support</li>
        </ul>

        <div style="position: absolute; right: 0; text-align: right;">
          <c:choose>
            <c:when test="${USER.userId > 0 && CONTROLLING_USER.userId <= 0}">
              <div>Welcome ${USER.contactInfo.firstName} ${USER.contactInfo.lastName}</div>
              <div>
                <span style="color: #ec7958;">TechSupport</span> 
                <span style="color: black;">855-932-6646</span> | 
                <a href="<spring:url value='/logout' />" style="font-weight: bold;">LOGOUT</a>
              </div>
            </c:when>
            <c:otherwise>
              <div>
                <span style="color: #ec7958;">TechSupport</span> 
                <span style="color: black;">855-932-6646</span>
              </div>
            </c:otherwise>
          </c:choose>
        </div>

        <div id="logo">
          <img src="<spring:url value='/static/images/logo.png' />" alt="Web on The Go" onClick="location.href='<spring:url value="/" />'" />
        </div>

      </div>
    </div>

    <div id="container">
      <div class="mainbody clearfix">