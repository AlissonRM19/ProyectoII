package com.example.ProyectoII;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.io.PrintWriter;

@Path("/hello-world")
public class HelloResource{
    @GET
    @Produces("text/plain")
    public String hello() {return "Hello";}
}