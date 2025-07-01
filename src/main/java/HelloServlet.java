package com.demo.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

@WebServlet("/sayHello")
public class HelloServlet extends HttpServlet {
    
    private static final String DB_URL = "jdbc:mysql://localhost:3306/userdb";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "password";
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        String username = request.getParameter("username");
        
        if (username == null || username.trim().isEmpty()) {
            out.println("<h1>Error: Username parameter is required</h1>");
            return;
        }
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            Statement stmt = conn.createStatement();
            
                
            String query = "";

            // Build the SQL query to fetch user details from the USER table using the provided username
            query = "SELECT username, email, full_name, created_date FROM USER WHERE username = '" + username + "'";
            
            ResultSet rs = stmt.executeQuery(query);
            
            out.println("<html><body>");
            out.println("<h1>User Details</h1>");
            
            if (rs.next()) {
                out.println("<p><strong>Username:</strong> " + rs.getString("username") + "</p>");
                out.println("<p><strong>Email:</strong> " + rs.getString("email") + "</p>");
                out.println("<p><strong>Full Name:</strong> " + rs.getString("full_name") + "</p>");
                out.println("<p><strong>Created Date:</strong> " + rs.getString("created_date") + "</p>");
            } else {
                out.println("<p>No user found with username: " + username + "</p>");
            }
            
            out.println("</body></html>");
            
            rs.close();
            stmt.close();
            conn.close();
            
        } catch (Exception e) {
            out.println("<h1>Database Error</h1>");
            out.println("<p>" + e.getMessage() + "</p>");
            e.printStackTrace();
        }
    }
}