package com.tscp.mvna.domain.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.trc.config.Config;
import com.trc.manager.UserManager;
import com.trc.user.User;
import com.trc.web.model.ResultModel;

@Controller
public abstract class MemberController extends SearchController {
  @Autowired
  protected UserManager userManager;
  @Autowired
  protected SessionRegistry sessionRegistry;

  @RequestMapping(value = { "", "/", "home" }, method = RequestMethod.GET)
  public abstract ModelAndView showHome();

  @RequestMapping(value = "/all", method = RequestMethod.GET)
  public abstract ModelAndView showMembers();

  protected ModelAndView showMembers(String title, List<User> users) {
    ResultModel model = new ResultModel("admin/members");
    if (Config.ADMIN) {
      model.addAttribute("members", users);
      model.addAttribute("memberType", title);
      return model.getSuccess();
    } else {
      return model.getAccessDenied();
    }
  }

}
