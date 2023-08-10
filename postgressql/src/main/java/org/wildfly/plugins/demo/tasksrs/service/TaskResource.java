/*
 * JBoss, Home of Professional Open Source
 * Copyright 2015, Red Hat, Inc. and/or its affiliates, and individual
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
package org.wildfly.plugins.demo.tasksrs.service;

import java.net.URI;
import java.security.Principal;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.UserTransaction;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.*;
import org.wildfly.plugins.demo.tasksrs.model.Task;
import org.wildfly.plugins.demo.tasksrs.model.TaskDaoImpl;
import org.wildfly.plugins.demo.tasksrs.model.TaskUser;
import org.wildfly.plugins.demo.tasksrs.model.TaskUserDaoImpl;

/**
 * A JAX-RS resource for exposing REST endpoints for Task manipulation
 */
@RequestScoped
@Path("/")
public class TaskResource {

    @Inject
    private TaskUserDaoImpl userDao;

    @Inject
    private TaskDaoImpl taskDao;

    @Inject
    private UserTransaction tx;

    @POST
    @Path("tasks/title/{title}")
    public Response createTask(@Context UriInfo info, @Context SecurityContext context,
            @PathParam("title") @DefaultValue("task") String taskTitle) throws Exception {
        Task task = null;
        try {
            tx.begin();
            TaskUser user = getUser(context);
            task = new Task(taskTitle);

            taskDao.createTask(user, task);
            tx.commit();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        String rawPath = info.getAbsolutePath().getRawPath().replace("title/" + task.getTitle(), "id/" + task.getId().toString());
        UriBuilder uriBuilder = info.getAbsolutePathBuilder().replacePath(rawPath);
        URI uri = uriBuilder.build();

       return Response.created(uri).build();
    }

//    @GET
//    // JSON: include "application/json" in the @Produces annotation to include json support
//    // @Produces({ "application/xml", "application/json" })
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response getTasks(@Context SecurityContext context) {
//        List<Task> tasks= Collections.<Task>emptyList();
//        try {
//            tx.begin();
//            tasks = getTasks(getUser(context));
//            tx.commit();
//            // return tasks;
//        } catch (Exception ex) {
//            throw new RuntimeException(ex);
//        }
//        return Response.ok(tasks,MediaType.APPLICATION_JSON).build();
//    }

    @Produces({"application/xml"})
    public List<Task> getTasks(@Context SecurityContext context) {
        try {
            tx.begin();
            List<Task> tasks = getTasks(getUser(context));
            tx.commit();
            return tasks;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
    private List<Task> getTasks(TaskUser user) {
        return taskDao.getAll(user);
    }

    private TaskUser getUser(SecurityContext context) {
        Principal principal = null;
        String name = "Anonymous";
        if (context != null) {
            principal = context.getUserPrincipal();
        }

        if (principal != null) {
            name = principal.getName();
        }

        return getUser(name);
    }

    private TaskUser getUser(String username) {

        try {
            TaskUser user = userDao.getForUsername(username);

            if (user == null) {
                user = new TaskUser(username);

                userDao.createUser(user);
            }

            return user;
        } catch (Exception e) {
            throw new WebApplicationException(e);
        }
    }
}
