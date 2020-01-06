package com.dearzss.service.impl;

import com.dearzss.dao.PersonMapperDao;
import com.dearzss.domain.Person;
import com.dearzss.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("personService")
public class PersonServiceImpl implements PersonService {

    @Autowired
    private PersonMapperDao personMapperDao;

    @Override
    public List<Person> findAllPerson() {
        return personMapperDao.findAllPerson();
    }
}
