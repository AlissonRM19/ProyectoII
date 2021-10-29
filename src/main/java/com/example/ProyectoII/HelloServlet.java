package com.example.ProyectoII;
/**
 * Estas son las importaciones necesarias para el funcionamiento del codigo.
 */
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
import java.util.Stack;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.regex.Pattern;

/**
 * La clase principal que donde se crean los metodos para la ejecucion del programa.
 *
 * @author Alisson Redondo, Greivin Carrillo.
 * @version 1.0
 * @since 29/10/2021
 */
@WebServlet(name = "helloServlet", value = "/hello-servlet")
public class HelloServlet extends HttpServlet {

    private String message;
    static Pila operators = new Pila();
    // Colocar la ruta de la computadora donde esta el archivo.
    String NombreArchivo = "C:\\Users\\Greva\\IdeaProjects\\ProyectoII\\Historial.csv";

    // Inicializa la clase y se le da una variable mensaje para utilizar en el historial brindado.
    public void init() {
        message = "Historial";
    }

    // Metodo que convierte la expresion infix dada por el cliente a postfix.
    private static String toPostfix(String infix) {

        char symbol;
        StringBuilder postfix = new StringBuilder();

        // For que evalua la expresion.
        for (int i = 0; i < infix.length(); ++i) {

            symbol = infix.charAt(i);

            // If que valora si el caracter es un digito.
            if (Character.isDigit(symbol))
                postfix.append(symbol);

            // Comprueba cuando los caracteres son parentesis.
            else if (symbol == '(') {
                operators.push(symbol);
            }

            // Si el caracter es un parentesis derecho, agrega los operadores de la pila hasta llegar a el parentesis izquierdo y elimina dichos parentesis.
            else if (symbol == ')') {

                while (operators.peek() != '(') {
                    postfix.append(operators.pop());
                }
                operators.pop();
            }

            // Comprueba que el array no este vacio y hace la comparacion de prioridad entre operadores.
            else {

                while (!operators.isEmpty() && !(operators.peek() == '(') && prec(symbol) <= prec(operators.peek()))
                    postfix.append(operators.pop());
                operators.push(symbol);
            }
        }

        // Agrega los caracteres al String final.
        while (!operators.isEmpty())
            postfix.append(operators.pop());

        // Retorna el String final con la expresion en postfix.
        return postfix.toString();
    }

    // Int que brinda la precedencia de los caracteres para su comparacion.
    static int prec(char x) {

        if (x == '+' || x == '-')
            return 1;

        if (x == '*' || x == '/' || x == '%')
            return 2;
        return 0;
    }

    // Metodo que evalua la expresion postfix.
    static int evaluatePostfix(String postfix) {

        // For que recorre el postfix.
        for (int i = 0; i < postfix.length(); i++) {
            char c = postfix.charAt(i);

            // Si es un operando se agrega a la pila
            if (Character.isDigit(c))
                operators.push((char) (c - '0'));

            // Si es un operador realiza dicha operacion con los dos ultimos elementos de la pila.
            else {
                int val1 = operators.pop();
                int val2 = operators.pop();

                // Switch para determinar cual operacion realizar.
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

    // Determina si el caracter es un operador.
    boolean isOperator(char c) {

        if (c == '+' || c == '-'
                || c == '*' || c == '/'
                || c == '%') {
            return true;
        }
        return false;
    }

    // Asigna los nodos hijos(izquierda y derecha) al nodo principal.
    void inorder(Node t) {

        if (t != null) {
            inorder(t.left);
            System.out.print(t.value + " ");
            inorder(t.right);
        }
    }

    // Construye el arbol de expresion a base de la expresion postfix.
    Node constructTree(char postfix[]) {

        Stack<Node> st = new Stack<Node>();
        Node t, t1, t2;

        // For que recorre la expresion postfix.
        for (int i = 0; i < postfix.length; i++) {

            // Si es un operando lo agrega a la pila.
            if (!isOperator(postfix[i])) {
                t = new Node(postfix[i]);
                st.push(t);
            }

            // Cuando es un operador agrega los ultimos elementos de la pila.
            else {
                t = new Node(postfix[i]);

                // Elimina los dos nodos anteriores.
                t1 = st.pop();
                t2 = st.pop();

                // Asigna esos nodos como los hijos al nodo principal.
                t.right = t1;
                t.left = t2;

                // Agrega la subexpresion a la pila.
                st.push(t);
            }
        }

        // Define la raiz del arbol.
        t = st.peek();
        st.pop();

        return t;
    }

    // Metodo para envair datos a la interfaz.
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        InetAddress address = InetAddress.getLocalHost();
        System.out.println("IP Local :" + address.getHostAddress());
        String Datos = null;

        try {

            // Variables para la lectura del archivo .csv.
            File file=new File(NombreArchivo);
            FileReader fr=new FileReader(file);
            BufferedReader br=new BufferedReader(fr);
            StringBuffer sb=new StringBuffer();
            String line;

            // Proceso recursivo para la lectura de las lineas del archivo .csv.
            while((line=br.readLine())!=null) {

                sb.append(line);
                sb.append("\n");
            }

            fr.close();
            System.out.println("Contents of File: ");
            System.out.println(sb.toString());
            Datos = sb.toString();
        }

        catch(IOException e) {
        }

        // Crea la ventana del historial de la interfaz para la visualizacion del cliente.
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h1>"+message +"</h1>");
        out.println("<h2>"+"<tr>"+"<td>"+"Expresión, "+"</td>"+"<td>"+"Resultado, "+"</td>"+"<td>"+"Fecha, "+"</td>"+"<td>"+"Nombre"+"</td>"+"</tr>"+"</h2>");
        out.println("<h2>"+Datos+"</h2>");
        out.println("</body></html>");

    }

    // Metodo para enviar datos a la interfaz.
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Variables recibidas del cliente.
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

            // Da los datos a la interfaz para la visualizacion del cliente.
            out.println("La expresión enviada es: " + Expresion);
            out.println("<br/>");
            out.println("El resultado de la expresión es: " + Resultado);
            out.println("<br/>");
            out.println("El nombre del cliente que envio la expresión es:" + Nombre);
        }

        try {

            // Variables para la escritura en el archivo .csv de las variables dadas y el resultado de la expresion.
            FileWriter fw = new FileWriter(NombreArchivo,true);
            BufferedWriter bw= new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);
            pw.println(Expresion+","+Resultado+","+Fecha.format(LocalDateTime.now())+"="+Nombre);

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

/**
 *  La clase pila crea un array de caracteres y tiene los metodos para la modificacion de dicho array.
 */
class Pila {

    char a[]=new char[100];
    int top=-1;

    // Extrae el ultimo elemento ingresado en la pila.
    void push(char c) {

        try {
            a[++top]= c;
        }

        catch(StringIndexOutOfBoundsException e) {
            System.out.println("Stack full, no room to push, size=100");
            System.exit(0);
        }
    }

    // Elimina la variable comparada.
    char pop() {

        return a[top--];
    }

    // Comprueba que el pila no este vacia.
    boolean isEmpty() {

        return (top==-1)?true:false;
    }

    // Regresa el dato de la pila.
    char peek() {

        return a[top];
    }
}

/**
 * La clase nodo crea un nodo con un caracter como valor y tambien crea sus nodos hijos, izquierda y derecha.
 */
class Node {

    char value;
    Node left, right;

    // Asigna los valores de los nodos creados.
    Node(char item) {
        value = item;
        left = right = null;
    }
}
