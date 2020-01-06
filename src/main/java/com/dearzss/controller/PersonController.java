package com.dearzss.controller;

import com.dearzss.domain.Person;
import com.dearzss.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/person")
public class PersonController {

    @Autowired
    private PersonService personService;

    @RequestMapping("/findAllPerson")
    public ModelAndView selectPerson(ModelAndView mv,HttpServletRequest request, HttpServletResponse response) throws IOException {

        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");

        List<Person> personList =personService.findAllPerson();
        mv.addObject("list",personList);
        mv.setViewName("listPerson");
        return mv;
    }
}
