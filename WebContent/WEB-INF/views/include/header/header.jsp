<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>
<title>Web on the Go &#8480; Account Management</title>

<meta http-equiv='Content-Type' content='text/html; charset=utf-8' />
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

<link rel="stylesheet" type="text/css" href="<spring:url value='/static/styles/structure.css' />" />
<link rel="stylesheet" type="text/css" href="<spring:url value='/static/styles/imported/bootstrap/bootstrap.css' />" />
<link rel="stylesheet" type="text/css" href="<spring:url value='/static/styles/bootstrap_wotg.css' />" />
<link rel="stylesheet" type="text/css" href="<spring:url value='/static/styles/wotg.css' />" />
<link rel="stylesheet" type="text/css" href="<spring:url value='/static/styles/dropdown.css' />" />
<link rel="stylesheet" type="text/css" href="<spring:url value='/static/styles/forms.css' />" />

<!--[if !IE 7]>
  <style type="text/css">
    #wrapper {display:table;height:100%}
  </style>
<![endif]-->

<script type="text/javascript" src="<spring:url value='/static/javascript/imported/jquery/jquery.min.js' />"></script>
<script type="text/javascript" src="<spring:url value='/static/javascript/imported/jquery/jqueryEasing.js' />"></script>
</head>

<body>

  <div id="wrapper">

    <c:if test="${not empty CONTROLLING_USER}">
      <sec:authorize ifAnyGranted="ROLE_ADMIN, ROLE_MANAGER, ROLE_AGENT, ROLE_SU">
        <%@ include file="/WEB-INF/views/include/admin/control_bar.jsp"%>
      </sec:authorize>
    </c:if>

    <div id="header">
      <div class="container contentContainer" style="position: relative;">

        <ul id="menutop">
          <li class="button" onclick="location.href='http://www.webonthego.com/'">Home</li>
          <li class="button" onclick="location.href='http://store.webonthego.com/'">Store</li>
          <li class="button" onclick="location.href='<spring:url value="/support" />'">Support</li>
        </ul>

        <div id="loginMenu">
          <span style="color: #ec7958;">TechSupport</span> <span style="color: black;">855-932-6646</span>
          <c:if test="${USER.userId > 0 && CONTROLLING_USER.userId <= 0}">
            <div>Welcome ${USER.contactInfo.firstName} ${USER.contactInfo.lastName} | <a href="<spring:url value='/logout' />"><strong>LOGOUT</strong></a></div>
          </c:if>
        </div>

        <img id="logo" src="<spring:url value='/static/images/logo.png' />" alt="Web on The Go" onclick="location.href='/'" />
      </div>
    </div>

    <div id="main">
      <div class="container contentContainer content">
        <div class="row">