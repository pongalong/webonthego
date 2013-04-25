package com.tscp.mvna.domain.controller;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.trc.exception.management.AccountManagementException;
import com.trc.exception.management.PaymentManagementException;
import com.trc.exception.management.RefundManagementException;
import com.trc.manager.AccountManager;
import com.trc.manager.PaymentManager;
import com.trc.manager.RefundManager;
import com.trc.manager.UserManager;
import com.trc.domain.refund.RefundCode;
import com.trc.domain.refund.RefundRequest;
import com.trc.user.User;
import com.trc.user.account.PaymentHistory;
import com.trc.web.model.ResultModel;
import com.trc.web.session.cache.CacheKey;
import com.trc.web.session.cache.CacheManager;
import com.trc.web.validation.JCaptchaValidator;
import com.trc.web.validation.RefundRequestValidator;
import com.tscp.mvne.CreditCard;
import com.tscp.mvne.PaymentTransaction;
import com.tscp.util.logger.DevLogger;

@Controller
@RequestMapping("/account/payment/refund")
public class RefundController {
  @Autowired
  private UserManager userManager;
  @Autowired
  private PaymentManager paymentManager;
  @Autowired
  private RefundManager refundManager;
  @Autowired
  private RefundRequestValidator refundRequestValidator;
  @Autowired
  private AccountManager accountManager;

  @ModelAttribute
  protected void refundReferenceData(ModelMap modelMap) {
    modelMap.addAttribute("refundCodes", Arrays.asList(RefundCode.values()));
  }
  
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @RequestMapping(value = "{transId}", method = RequestMethod.GET)
  public ModelAndView showRefund(@PathVariable int transId) {
    ResultModel resultModel = new ResultModel("/account/payment/refund/confirm");
    User user = userManager.getCurrentUser();
    try {
      PaymentTransaction paymentTransaction = refundManager.getPaymentTransaction(user.getUserId(), transId);
      RefundRequest refundRequest = new RefundRequest();
      refundRequest.setPaymentTransaction(paymentTransaction);
      resultModel.addAttribute("refundRequest", refundRequest);
      return resultModel.getSuccess();
    } catch (RefundManagementException e) {
      return resultModel.getAccessException();
    }
  }

  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @RequestMapping(value = "{transId}", method = RequestMethod.POST)
  public ModelAndView processRefund(HttpServletRequest request, @ModelAttribute RefundRequest refundRequest, BindingResult result, @PathVariable int transId) {
      ResultModel resultModel = new ResultModel("redirect:/account/payment/history", "/account/payment/refund/confirm");
      User user = userManager.getCurrentUser(); 
      JCaptchaValidator.validate(request, result);
      refundRequestValidator.validate(refundRequest, result);
      if (result.hasErrors()) {
         DevLogger.log("refundController.processRefund() has errors " + result.getAllErrors().toString());
         return resultModel.getError();
      } 
      else {
         try {
        	 if(isRefundable(refundRequest.getPaymentTransaction())) {
                refundManager.refundPayment(user, refundRequest, transId);
        	 }   
             PaymentHistory paymentHistory = new PaymentHistory(accountManager.getPaymentRecords(user), user);
             new CacheManager().set(CacheKey.PAYMENT_HISTORY, paymentHistory);  //need to refresh "PAYMENT_HISTORY" in cache              
         } catch (RefundManagementException e) {
        	 return resultModel.getAccessException();
         } catch (AccountManagementException e) {
      	     return resultModel.getAccessException();
         }
         return resultModel.getSuccess();
      }    
  }  
  
  private boolean isRefundable(PaymentTransaction paymentTransaction) throws RefundManagementException {
	  CreditCard creditCard = null;
	  if (paymentTransaction.getTransId() < 0)
	  	  throw new RefundManagementException("Unable to refund because no valid Transaction ID found.");
	  
	  if (paymentTransaction.getBillingTrackingId() < 0)
		  throw new RefundManagementException("Unable to refund because no valid Billing Tracking ID found.");
	  try{
		  paymentManager.getCreditCard(paymentTransaction.getPmtId());
	  } catch(PaymentManagementException pe){
		  throw new RefundManagementException("Unable to refund because error occured while getting credit card information: " + pe.getMessage());
 	  }
	  return creditCard != null;
  }

}
