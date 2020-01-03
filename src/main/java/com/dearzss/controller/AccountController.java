package com.dearzss.controller;

import com.dearzss.domain.Account;
import com.dearzss.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
public class AccountController {
    @Autowired
    private AccountService accountService;

    @RequestMapping("/account/findAll")
    public ModelAndView findAll(ModelAndView mv){
        System.out.println("Controller表现层：查询所有账户...");

        List<Account> accounts = accountService.findAll();
        mv.addObject("list",accounts);
        mv.setViewName("list");
        return mv;
    }

    @RequestMapping("account/save")
    public void saveAccount(Account account, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception{
        accountService.saveAccount(account);
        httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/account/findAll");
    }
}
