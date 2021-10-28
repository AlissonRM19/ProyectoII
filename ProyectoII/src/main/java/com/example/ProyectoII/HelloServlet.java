package com.example.ProyectoII;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.InetAddress;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Reader;


@WebServlet(name = "helloServlet", value = "/hello-servlet")
public class HelloServlet extends HttpServlet {
    private String message;
    static Pila operators = new Pila();
    // colocar la ruta de la computadora donde esta el archivo
    String NombreArchivo = "C:\\Users\\Greva\\IdeaProjects\\ProyectoII\\pruebas.csv";;
    public static final String SEPARADOR=";";

    public void init() {
        message = "Historial";
    }

    private static String toPostfix(String infix)
//converts an infix expression to postfix
    {
        char symbol;
        StringBuilder postfix = new StringBuilder();
        for (int i = 0; i < infix.length(); ++i)
//while there is input to be read
        {
            symbol = infix.charAt(i);
//if it's an operand, add it to the string
            if (Character.isLetter(symbol))
                postfix.append(symbol);
            else if (Character.isDigit(symbol))
                postfix.append(symbol);
            else if (symbol == '(')
//push (
            {
                operators.push(symbol);
            } else if (symbol == ')')
//push everything back to (
            {
                while (operators.peek() != '(') {
                    postfix.append(operators.pop());
                }
                operators.pop();        //remove '('
            } else
//print operators occurring before it that have greater precedence
            {
                while (!operators.isEmpty() && !(operators.peek() == '(') && prec(symbol) <= prec(operators.peek()))
                    postfix.append(operators.pop());
                operators.push(symbol);
            }
        }
        while (!operators.isEmpty())
            postfix.append(operators.pop());
        return postfix.toString();
    }

    static int prec(char x) {
        if (x == '+' || x == '-')
            return 1;
        if (x == '*' || x == '/' || x == '%')
            return 2;
        return 0;
    }

    // Method to evaluate value of a postfix expression
    static int evaluatePostfix(String postfix) {

        // Scan all characters one by one
        for (int i = 0; i < postfix.length(); i++) {
            char c = postfix.charAt(i);

            // If the scanned character is an operand (number here),
            // push it to the stack.
            if (Character.isDigit(c))
                operators.push((char) (c - '0'));

                //  If the scanned character is an operator, pop two
                // elements from stack apply the operator
            else {
                int val1 = operators.pop();
                int val2 = operators.pop();

                switch (c) {
                    case '+':
                        operators.push((char) (val2 + val1));
                        break;

                    case '-':
                        operators.push((char) (val2 - val1));
                        break;

                    case '/':
                        operators.push((char) (val2 / val1));
                        break;

                    case '*':
                        operators.push((char) (val2 * val1));
                        break;

                    case '%':
                        operators.push((char) (val2 % val1));
                        break;
                }
            }
        }
        return operators.pop();
    }

    // A utility function to check if 'c'
    // is an operator

    boolean isOperator(char c) {
        if (c == '+' || c == '-'
                || c == '*' || c == '/'
                || c == '%') {
            return true;
        }
        return false;
    }

    // Utility function to do inorder traversal
    void inorder(Node t) {
        if (t != null) {
            inorder(t.left);
            System.out.print(t.value + " ");
            inorder(t.right);
        }
    }
    // Returns root of constructed tree for given
    // postfix expression
    Node constructTree(char postfix[]) {
        Stack<Node> st = new Stack<Node>();
        Node t, t1, t2;

        // Traverse through every character of
        // input expression
        for (int i = 0; i < postfix.length; i++) {

            // If operand, simply push into stack
            if (!isOperator(postfix[i])) {
                t = new Node(postfix[i]);
                st.push(t);
            } else // operator
            {
                t = new Node(postfix[i]);

                // Pop two top nodes
                // Store top
                t1 = st.pop();      // Remove top
                t2 = st.pop();

                //  make them children
                t.right = t1;
                t.left = t2;

                // Add this subexpression to stack
                st.push(t);
            }
        }
        //  only element will be root of expression
        // tree
        t = st.peek();
        st.pop();

        return t;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        DateTimeFormatter Fecha = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
        InetAddress address = InetAddress.getLocalHost();
        System.out.println("IP Local :" + address.getHostAddress());
        String Datos = null;
        String[] dato = null;

        BufferedReader br = null;

        try {
            br =new BufferedReader(new FileReader(NombreArchivo));
            String line = br.readLine();
            while (null!=line) {
                String [] fields = line.split(SEPARADOR);
                Datos = Arrays.toString(fields);
                dato= fields;
                line = br.readLine();
            }

        }
        catch (Exception e) {

        } finally {
            if (null != br) {
                br.close();
            }
        }
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h1>"+message +"</h1>");
        out.println("<h2>"+"<tr>"+"<td>"+"Expresión, "+"</td>"+"<td>"+"Resultado, "+"</td>"+"<td>"+"Fecha, "+"</td>"+"<td>"+"Nombre"+"</td>"+"</tr>"+"</h2>");
        out.println("<h2>"+Arrays.toString(dato)+"</h2>");
        out.println("</body></html>");

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String Expresion = request.getParameter("expresionrecibida");
        String Nombre = request.getParameter("NombreCliente");
        String Postfix = toPostfix(Expresion);
        String Resultado = String.valueOf(evaluatePostfix(Postfix));
        DateTimeFormatter Fecha = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
        String[] dato = null;
        response.setContentType("text/html;charset=UTF-8");

        HelloServlet et = new HelloServlet();
        char[] charArray = Postfix.toCharArray();
        Node root = et.constructTree(charArray);
        System.out.println("La expresión del árbol es");
        et.inorder(root);

        try(PrintWriter out = response.getWriter()) {

            out.println("La expresión enviada es: " + Expresion);
            out.println("<br/>");
            out.println("El resultado de la expresión es: " + Resultado);
            out.println("<br/>");
            out.println("El nombre del cliente que envio la expresión es:" + Nombre);
        }

        try {
            FileWriter fw = new FileWriter(NombreArchivo,true);
            BufferedWriter bw= new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);

            pw.println(Expresion+","+Resultado+","+Fecha.format(LocalDateTime.now())+","+Nombre);
            if (Nombre == Arrays.toString(dato))
                return;
            pw.flush();
            pw.close();
        }
        catch (Exception E){
        }
    }

    public void destroy() {
    }

}
class Pila
{
    char a[]=new char[100];
    int top=-1;
    void push(char c)
    {
        try
        {
            a[++top]= c;
        }
        catch(StringIndexOutOfBoundsException e)
        {
            System.out.println("Stack full, no room to push, size=100");
            System.exit(0);
        }
    }
    char pop()
    {
        return a[top--];
    }
    boolean isEmpty()
    {
        return (top==-1)?true:false;
    }

    char peek()
    {
        return a[top];
    }
}

class Node {
    char value;
    Node left, right;

    Node(char item) {
        value = item;
        left = right = null;
    }
}
