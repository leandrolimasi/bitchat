/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.bitchat.service;

import java.util.List;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.github.bitchat.data.UserRepository;
import com.github.bitchat.model.User;

// The @Stateless annotation eliminates the need for manual transaction demarcation
/**
 * Service EJB for User
 * 
 * @author leandrolimadasilva
 *
 */
@Stateless
public class UserService {

    @Inject
    private UserRepository repository;

    @Inject
    private Logger log;

    @Inject
    private EntityManager em;

    @Inject
    private Event<User> memberEventSrc;

    /**
     * 
     * @param user
     * @throws Exception
     */
    public void register(User user) throws Exception {
        log.info("Registering " + user.getName());
        em.persist(user);
        memberEventSrc.fire(user);
    }

    /**
     * 
     * @return
     */
    public List<User> findAllOrderedByName() {
        return repository.findAllOrderedByName();
    }

    /**
     * 
     * @param login
     * @param password
     * @return
     */
    public User findByLoginAndPassword(String login, String password) {
        return repository.findByLoginAndPassword(login, password);
    }

    /**
     * 
     * @param id
     * @return
     */
    public User findById(Long id) {
        return repository.findById(id);
    }

    
    /**
     * 
     * @param login
     * @return
     */
    public User findByLogin(String login) {
        return repository.findByLogin(login);
    }
}
