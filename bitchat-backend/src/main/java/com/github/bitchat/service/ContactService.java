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

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.github.bitchat.data.ContactRepository;
import com.github.bitchat.data.UserRepository;
import com.github.bitchat.model.Contact;
import com.github.bitchat.model.User;

// The @Stateless annotation eliminates the need for manual transaction demarcation
@Stateless
public class ContactService {

    @Inject
    private ContactRepository repository;

    @Inject
    private UserRepository userRepository;

    @Inject
    private EntityManager em;

    @Inject
    private Event<Contact> contactEventSrc;

    /** add contact for user
     * 
     * @param contact
     * @throws Exception
     */
    public void addContact(Contact contact) {
        contact.setContact(userRepository.findById(contact.getContact().getId()));
        contact.setUser(userRepository.findById(contact.getUser().getId()));
        em.persist(contact);
        contactEventSrc.fire(contact);
    }

    /** find contact by id
     * 
     * @param id
     * @return
     */
    public Contact findById(Long id) {
        return repository.findById(id);
    }

    
    /** find all contact by user
     * 
     * @param user
     * @return
     */
    public List<Contact> findByUser(User user) {
        return repository.findByUser(user);
    }
  
}
