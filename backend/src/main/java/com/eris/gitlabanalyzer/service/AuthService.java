package com.eris.gitlabanalyzer.service;

import com.eris.gitlabanalyzer.model.User;
import com.eris.gitlabanalyzer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Optional;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

@Service
public class AuthService {

    public AuthService() {
    }

    @Autowired
    UserRepository userRepository;

    public String getXMLResponse(String ticket){
        try {
            String url = "https://cas.sfu.ca/cas/serviceValidate/" + "?ticket="+ ticket + "&service=http://localhost:8080/api/v1/auth";
            System.out.println(url); // This line can be removed. This is for debugging.
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            int responseCode = con.getResponseCode();
            System.out.println("Response Code : " + responseCode); // This line can be removed. This is for debugging.
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            System.out.println(response.toString()); // This line can be removed. This is for debugging.
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder()
                    .parse(new InputSource(new StringReader(response.toString())));
            NodeList success = doc.getElementsByTagName("cas:authenticationSuccess");
            NodeList failed = doc.getElementsByTagName("cas:authenticationFailure");
            if (failed.getLength() > 0) {
                System.out.println("failed"); // This line can be removed. This is for debugging.
                return "validation failed";
            }
            if (success.getLength() > 0) {
                Element xml = (Element)success.item(0);
                System.out.println(xml.getElementsByTagName("cas:user").item(0).getTextContent()); // This line can be removed. This is for debugging.
                return xml.getElementsByTagName("cas:user").item(0).getTextContent();
            }
        } catch (Exception e) {
            System.out.println("-Error occurred while verifying SSO authentication-");
            System.out.println(e);
        }
        return "validation failed";
    }

    public boolean checkUserExist(String userId){
        Optional<User> user = userRepository.findUserByUsername(userId);
        if (user.isPresent()) {
            return true;
        } else {
            return false;
        }
    }
}
