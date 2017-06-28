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
package com.github.bitchat.rest;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import javax.validation.Validator;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.github.bitchat.model.Contact;
import com.github.bitchat.model.User;
import com.github.bitchat.service.ContactService;
import com.github.bitchat.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * JAX-RS Example
 * <p/>
 * This class produces a RESTful service to read/write the contents of the Users table.
 */
@Path("/user")
@RequestScoped
@Api( value = "/user" )
public class UserREST {

    @Inject
    private Logger log;

    @Inject
    private Validator validator;

    @Inject
    private UserService userService;

    @Inject
    private ContactService contactService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> listAllUsers() {
        return userService.findAllOrderedByName();
    }

    
    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Verifica se o usuário e senha existem", httpMethod = "POST", notes = "Verifica se o usuário e senha existem", response = User.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "login encontrado"),
            @ApiResponse(code = 404, message = "login não encontrado"),
            @ApiResponse(code = 500, message = "Erro interno em função da decodificação dos dados"),
            @ApiResponse(code = 400, message = "Erro no request em função da decodificação dos dados"),
            @ApiResponse(code = 412, message = "Dados obrigatórios não encontrados") })
    public User login(User user){
        User u = userService.findByLoginAndPassword(user.getLogin(), user.getPassword());
        if (u == null) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        u.setPassword(null);
        return u;
    }

    @GET
    @Path("/{id:[0-9][0-9]*}")
    @Produces(MediaType.APPLICATION_JSON)
    public User lookupUserById(@PathParam("id") long id) {
        User user = userService.findById(id);
        if (user == null) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        user.setPassword(null);
        return user;
    }

    @GET
    @Path("/contacts/{login}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Recupera os contatos de um usuários", httpMethod = "POST", notes = "lista de contatos do usuário", response = User.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "lista de usuários carregada com sucesso"),
            @ApiResponse(code = 500, message = "Erro interno em função da decodificação dos dados"),
            @ApiResponse(code = 400, message = "Erro no request em função da decodificação dos dados"),
            @ApiResponse(code = 412, message = "Dados obrigatórios não encontrados") })
    public List<Contact> getContactsByLogin(@PathParam("login") String login) {
        User user = userService.findByLogin(login);
        if (user == null) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        user.setPassword(null);
        return contactService.findByUser(user);
    }
    
    @GET
    @Path("/{login}")
    @Produces(MediaType.APPLICATION_JSON)
    public User getByLogin(@PathParam("login") String login) {
        User user = userService.findByLogin(login);
        if (user == null) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        user.setPassword(null);
        return user;
    }

  
    @POST
    @Path("/addContact")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Adicina o novo contato ao usuário", httpMethod = "POST", notes = "adiciona um contato pelo login", response = User.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "contato adicionado com sucesso"),
            @ApiResponse(code = 500, message = "Erro interno em função da decodificação dos dados"),
            @ApiResponse(code = 400, message = "Erro no request em função da decodificação dos dados"),
            @ApiResponse(code = 412, message = "Dados obrigatórios não encontrados") })
    public Response addContactUser(Contact contact) {

        Response.ResponseBuilder builder = null;

        try {
            contactService.addContact(contact);

            // Create an "ok" response
            builder = Response.ok();
        } catch (ConstraintViolationException ce) {
            // Handle bean validation issues
            builder = createViolationResponse(ce.getConstraintViolations());
            log.severe(ce.getLocalizedMessage());
        } catch (ValidationException e) {
            // Handle the unique constrain violation
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("login", "Email taken");
            builder = Response.status(Response.Status.CONFLICT).entity(responseObj);
            log.severe(e.getLocalizedMessage());
        } catch (Exception e) {
            // Handle generic exceptions
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("error", e.getMessage());
            builder = Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
            log.severe(e.getLocalizedMessage());
        }

        return builder.build();
    }

    /**
     * Creates a new User from the values provided. Performs validation, and will return a JAX-RS response with either 200 ok,
     * or with a map of fields, and related errors.
     */
    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Registra um novo usuário", httpMethod = "POST", notes = "cria um novo usuário", response = User.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "usuario criado com sucesso"),
            @ApiResponse(code = 500, message = "Erro interno em função da decodificação dos dados"),
            @ApiResponse(code = 400, message = "Erro no request em função da decodificação dos dados"),
            @ApiResponse(code = 412, message = "Dados obrigatórios não encontrados") })
    public Response createUser(User user) {

        Response.ResponseBuilder builder = null;

        try {
            // Validates User using bean validation
            validateUser(user);

            userService.register(user);

            // Create an "ok" response
            builder = Response.ok();
        } catch (ConstraintViolationException ce) {
            // Handle bean validation issues
            builder = createViolationResponse(ce.getConstraintViolations());
            log.severe(ce.getLocalizedMessage());
        } catch (ValidationException e) {
            // Handle the unique constrain violation
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("login", "Email taken");
            builder = Response.status(Response.Status.CONFLICT).entity(responseObj);
            log.severe(e.getLocalizedMessage());
        } catch (Exception e) {
            // Handle generic exceptions
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("error", e.getMessage());
            builder = Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
            log.severe(e.getLocalizedMessage());
        }

        return builder.build();
    }

    /**
     * <p>
     * Validates the given User variable and throws validation exceptions based on the type of error. If the error is standard
     * bean validation errors then it will throw a ConstraintValidationException with the set of the constraints violated.
     * </p>
     * <p>
     * If the error is caused because an existing User with the same email is registered it throws a regular validation
     * exception so that it can be interpreted separately.
     * </p>
     * 
     * @param user User to be validated
     * @throws ConstraintViolationException If Bean Validation errors exist
     * @throws ValidationException If User with the same email already exists
     */
    private void validateUser(User user) throws ConstraintViolationException, ValidationException {
        // Create a bean validator and check for issues.
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
        }

        // Check the uniqueness of the login
        if (loginAlreadyExists(user.getLogin())) {
            throw new ValidationException("Unique Email Violation");
        }
    }

    /**
     * Creates a JAX-RS "Bad Request" response including a map of all violation fields, and their message. This can then be used
     * by clients to show violations.
     * 
     * @param violations A set of violations that needs to be reported
     * @return JAX-RS response containing all violations
     */
    private Response.ResponseBuilder createViolationResponse(Set<ConstraintViolation<?>> violations) {
        log.fine("Validation completed. violations found: " + violations.size());

        Map<String, String> responseObj = new HashMap<>();

        for (ConstraintViolation<?> violation : violations) {
            responseObj.put(violation.getPropertyPath().toString(), violation.getMessage());
        }

        return Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
    }

    /**
     * Checks if a User with the same email address is already registered. This is the only way to easily capture the
     * "@UniqueConstraint(columnNames = "email")" constraint from the User class.
     * 
     * @param email The email to check
     * @return True if the email already exists, and false otherwise
     */
    public boolean loginAlreadyExists(String email) {
        User user = null;
        try {
        	user = userService.findByLogin(email);
        } catch (NoResultException e) {
        	log.warning(e.getLocalizedMessage());
        }
        return user != null;
    }
}
