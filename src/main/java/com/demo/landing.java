package com.demo;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Date;

@WebServlet(urlPatterns = "/landing")
public class landing extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Date date = new Date();
        long nocache = date.getTime() / 1000L;
        resp.setHeader("Expires", "Tue, 03 Jul 2001 06:00:00 GMT");
        resp.setDateHeader("Last-Modified", new Date().getTime());
        resp.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0, post-check=0, pre-check=0");
        resp.setHeader("Pragma", "no-cache");
        PrintWriter out = resp.getWriter();
        out.println("<html>");
        out.println("<head>");
        out.println("<script src=\"https://www.google.com/recaptcha/enterprise.js?render="+System.getenv("WEBV3KEY")+"\"></script>");
        out.println("<title>reCAPTCHA Mobile API</title>");
        out.println("<script src=\"js/vars.js?nocache="+nocache+"\"></script>");
        out.println("<script src=\"js/jquery.min.js\"></script>");
        out.println("<script src=\"js/app.js?nocache="+nocache+"\"></script>");
        out.println("</head>");
        out.println("<body><input type='button' value='TEST EMPTY' onClick='javascript:sendJson(0);'><input type='button' value='TEST TOKEN' onClick='javascript:sendJson(1);'>");
        out.println("<p><small>Last build time: <a href=\"https://github.com/bigdavros/recaptcha-mobile-api-demo\">"+System.getenv("LASTBUILD")+"</a></small></p>");
        out.println("</body>");
        out.println("</html>");
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }
}
