package com.demo;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet(urlPatterns = "/js/vars.js")
public class vars extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        // these are calculated each time
        out.println("let site_key = \""+System.getenv("WEBV3KEY")+"\";");
        out.println("let api_key = \""+System.getenv("APPXAPIKEYWEB")+"\";");
        out.println("let lastBuild = \""+System.getenv("LASTBUILD")+"\";");
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }
}
