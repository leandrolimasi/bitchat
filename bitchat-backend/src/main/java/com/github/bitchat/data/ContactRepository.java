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
package com.github.bitchat.data;

import com.github.bitchat.model.Contact;
import com.github.bitchat.model.User;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

/** Repository for Contact
 * 
 * @author leandrolimadasilva
 *
 */
@ApplicationScoped
public class ContactRepository {

    @Inject
    private EntityManager em;

    /** find contact by id
     * 
     * @param id
     * @return
     */
    public Contact findById(Long id) {
        return em.find(Contact.class, id);
    }

    /** find contact by user
     * 
     * @param user
     * @return
     */
    public List<Contact> findByUser(User user) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Contact> criteria = cb.createQuery(Contact.class);
        Root<Contact> contact = criteria.from(Contact.class);
        criteria.select(contact).where(cb.equal(contact.get("user"), user));
        return em.createQuery(criteria).getResultList();
    }

}
