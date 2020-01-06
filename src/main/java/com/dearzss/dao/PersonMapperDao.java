package com.dearzss.dao;

import com.dearzss.domain.Person;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonMapperDao {
     public List<Person> findAllPerson();
}
