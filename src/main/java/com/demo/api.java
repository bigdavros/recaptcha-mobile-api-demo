package com.demo;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;

import java.util.Optional;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper; 
import com.fasterxml.jackson.databind.ObjectWriter;  

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

class Reply{
    private String data;
    private String result;

    public void setData (String data){
        this.data = data;
    }

    public void setResult (String result){
            this.result = result;

    }

    public String getData(){
        return data;
    }

    public String getResult(){
        return result;
    }

    public String asJSON() throws Exception{
        String json;
        try {
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            json = ow.writeValueAsString(this);
        }
        finally{}
        return json;
    }
}


class BasicEvent {
    private String Token = "";
    private String SiteKey = "";
    private String ExpectedAction = "";    
    
    public void setToken (String token){
        this.Token = token;
    }

    public String getToken(){
        return Token;
    }

    public void setSiteKey (String siteKey){
        this.SiteKey = siteKey;
    }

    public String getSiteKey(){
        return SiteKey;
    }

    public void setExpectedAction (String expectedAction){
        this.ExpectedAction = expectedAction;
    }

    public String getExpectedAction(){
        return ExpectedAction;
    }
}


class EventWrapper {
    private BasicEvent event;

    public void setEvent(BasicEvent event){
        this.event=event;
    }

    public BasicEvent getEvent(){
        return event;
    }
}

/**
 * API Servlet
 */
@WebServlet(urlPatterns = "/api")
public class api extends HttpServlet {

    private String apiKey = System.getenv("RECAPTCHAAPIKEY");
    private String v3key = System.getenv("WEBV3KEY");
    private String ioskey = System.getenv("IOSKEY");
    private String androidkey = System.getenv("ANDROIDKEY");
    private String projectId = System.getenv("PROJECTID");
    private String appApiKeyWeb = System.getenv("APPXAPIKEYWEB");
    private String appApiKeyAndroid = System.getenv("APPXAPIKEYANDROID");
    private String appApiKeyIos = System.getenv("APPXAPIKEYIOS");

    private Reply makeRecaptchaApiRequest(BasicEvent event) throws Exception{
        Reply reply = new Reply();
        try{
            HttpClient httpclient = HttpClients.createDefault();
            HttpPost httppost = new HttpPost("https://recaptchaenterprise.googleapis.com/v1/projects/"+projectId+"/assessments?key="+apiKey);
            httppost.setEntity(new StringEntity(""));
            EventWrapper wrapper = new EventWrapper();
            wrapper.setEvent(event);

            httppost.setEntity(new StringEntity(recaptchaRequest(wrapper)));

            //Execute and get the response.
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                try (InputStream instream = entity.getContent()) {
                    BufferedReader buf = new BufferedReader(new InputStreamReader(instream, "UTF-8"));
                    StringBuilder sb = new StringBuilder();
                    String s;
                    while(true)
                    {
                        s = buf.readLine();
                        if(s==null || s.length()==0)
                            break;
                        sb.append(s);
                    }
                    buf.close();
                    instream.close();
                    
                    JSONObject jsonObject = new JSONObject(sb.toString()); 
                    try{            
                        Double score = jsonObject.getJSONObject("riskAnalysis").getDouble("score");
                        String result = "failure, score was '"+score+"'";
                        if(score>0.5){
                            result = "success, score was '"+score+"'";
                        }
                        reply.setResult(result);
                    }   
                    catch(Exception e){
                        reply.setResult("Error with score: "+e+"\n\njsonObject="+jsonObject.toString());
                    }                        
                }
                catch (Exception e){
                    reply.setResult("Error with interpretting API response from recaptcha: "+e);
                }
            }
        }
        catch (Exception e){
            //crash and burn
            reply.setResult("Error with sending API call: "+e);
        }
        return reply;
    }

    private String recaptchaRequest(EventWrapper wrapper) throws Exception{
        String json = "{}";
        try{
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            json = ow.writeValueAsString(wrapper);
        }
        catch (Exception e){
            //crash and burn
            throw new IOException("Error converting event to JSON: "+e);
        }
        return json;
    } 

    private void error(PrintWriter out,String msg){
        out.print("{\n  \"data\":\"error\",\n  \"result\":\""+msg+"\"\n}");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter gOut = resp.getWriter();
        gOut.println("GET not supported");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        PrintWriter out = resp.getWriter();
        
        Optional<String> xapikey = Optional.ofNullable(req.getHeader("X-API-Key"));

        if(xapikey.toString().equals("Optional.empty")){
            resp.setStatus(401);
            error(out,"No API KEY");
        }
        else{            
            String key = req.getHeader("X-API-Key");
            if(key.equals(appApiKeyWeb)||key.equals(appApiKeyAndroid)||key.equals(appApiKeyIos)){
                StringBuffer jb = new StringBuffer();
                String line = null;
                try {
                    BufferedReader reader = req.getReader();
                    while ((line = reader.readLine()) != null)
                    jb.append(line);
                } 
                catch (Exception e) { 
                    error(out,"Error buffering JSON request string");
                }

                try {            
                    JSONObject jsonObject = new JSONObject(jb.toString());  
                    try{    
                        if(jsonObject.has("type")){
                            // if jsonObject type is recaptcha enterprise demo
                            String type = jsonObject.getString("type");
                            String recapToken = "";
                            if(jsonObject.has("recapToken")){
                                recapToken=jsonObject.getString("recapToken");
                            }

                            Random rand = new Random();
                            int upperbound = 100;
                            //generate random values from 0-24
                            int int_random = rand.nextInt(upperbound);                            

                            if(type.equals("web")||type.equals("android")||type.equals("ios")){
                                String siteKey = "";
                                String action = "";
                                if(type.equals("ios")){
                                    siteKey = ioskey;
                                    action="mobapidev_ios";
                                }
                                else if(type.equals("android")){
                                    siteKey = androidkey;
                                    action="mobapidev_android";
                                }
                                else if(type.equals("web")){
                                    siteKey=v3key;
                                    action="mobapidev_web";
                                }
                                if(recapToken.equals("")){
                                    out.println("{\n  \"data\":\""+int_random+"\",\n  \"result\":\"success\"\n}");
                                }
                                else {
                                    BasicEvent requestData = new BasicEvent();
                                    // extract post data and create new Event object
                                    try{
                                        requestData.setSiteKey(siteKey);
                                        requestData.setExpectedAction(action);
                                        requestData.setToken(recapToken);
                                    }
                                    catch(Exception e){
                                        error(out,"Error creating Request to reCAPTCHA");
                                    }                            
                                    try{
                                        // send Event object to recaptcha
                                        Reply recaptchaResponse = makeRecaptchaApiRequest(requestData);
                                        recaptchaResponse.setData(int_random+"");       
                                        out.print(recaptchaResponse.asJSON());
                                    }
                                    catch(Exception e){
                                        error(out,"Error creating Reply object");
                                    }
                                }                        
                            }
                            else{
                                error(out,"Nothing implemented for type '"+type+"'!");
                            }
                        }
                        else{ // if no type was set                            
                            error(out,"Input not complete: no type");
                        }
                    }
                    catch(Exception e){
                        error(out,"Error parsing POST JSON");
                    }            
                } catch (Exception e) {
                    // crash and burn, jb is the orginal POST body from the jequest
                    error(out,"No request body");
                }
            }
            else{
                resp.setStatus(403);
                error(out,"Incorrect API KEY");
            }
        }        
    }
}
