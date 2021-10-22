package com.example.ProyectoII;

import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet(name = "helloServlet", value = "/hello-servlet")
public class HelloServlet extends HttpServlet {
    private String message;


    public void init() {
        message = "El historial esta vacio";
    }

    public void crearExpresion(){

    }

    public int calculoExpresion(){
        // Prueba de resultado
       int resultado = 0;
       int ValorI = 2;
       int ValorII = 3;
       resultado = ValorI + ValorII;
       return resultado;

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");

        // Hello
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h1>" + message + "</h1>");
        out.println("</body></html>");

    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String Expresion = request.getParameter("expresionrecibida");

        response.setContentType("text/html;charset=UTF-8");

        try(PrintWriter out = response.getWriter()) {

            out.println("La expresión es "+ Expresion);
            out.println("<br/>");
            out.println("El resultado es "+ calculoExpresion());

        }

    }

    public void destroy() {
    }


}