package com.trc.service;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.WebServiceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trc.exception.service.RefundServiceException;
import com.trc.service.gateway.WebserviceGateway;
import com.trc.util.Formatter;
import com.tscp.mvne.Account;
import com.tscp.mvne.CreditCard;
import com.tscp.mvne.KenanPayment;
import com.tscp.mvne.PaymentTransaction;
import com.tscp.mvne.TSCPMVNA;

@Service
public class RefundService implements RefundServiceModel {
  private TSCPMVNA port;
  
  @Autowired
  public void init(WebserviceGateway gateway) {
    this.port = gateway.getPort();
  }

  public void applyCredit(CreditCard creditCard, Double amount) throws RefundServiceException {
    try {
      String stringAmount = Formatter.formatDollarAmount(amount);
      port.applyChargeCredit(creditCard, stringAmount);
    } catch (WebServiceException e) {
      throw new RefundServiceException(e.getMessage(), e.getCause());
    }
  }

  @Deprecated
  public void reversePayment(Account account, Double amount, Date date, String trackingId)
      throws RefundServiceException {
    try {
      GregorianCalendar calendar = new GregorianCalendar();
      calendar.setTime(date);
      XMLGregorianCalendar xmlCal = DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar);
      port.reverseKenanPayment(account, Double.toString(amount), xmlCal, trackingId);
    } catch (DatatypeConfigurationException dce) {
      throw new RefundServiceException("Error reversing payment: could not create XMLGregorianCalendar");
    } catch (WebServiceException e) {
      throw new RefundServiceException(e.getMessage(), e.getCause());
    }
  }
  
  public void refundPayment(int accountNo, int transId, String amount, int trackingId, String refundBy, int refundCode, String notes) throws RefundServiceException {
	    try {
	        port.refundPayment(accountNo, transId, amount, trackingId, refundBy, refundCode, notes);
	    }
        catch (WebServiceException we) {
    	       throw new RefundServiceException("WebServiceException occured: " + we.getMessage(), we.getCause());
	    }
        catch (Exception e) {
               throw new RefundServiceException(e.getMessage(), e.getCause());
      }
  }
  
  public PaymentTransaction getPaymentTransaction(int custId, int transId) throws RefundServiceException {
	    try {
	      return port.getPaymentTransaction(custId, transId);
	    } catch (Exception e) {
	      throw new RefundServiceException(e);
	    }
	  }
}
