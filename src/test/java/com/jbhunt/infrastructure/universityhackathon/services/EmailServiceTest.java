package com.jbhunt.infrastructure.universityhackathon.services;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.HashMap;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EmailServiceTest {

    @Mock
    private SendGrid mockSendGrid;

    @InjectMocks
    private EmailService emailService;

    @Test
    public void testSendEmail() throws IOException, JSONException {
        //ARRANGE
        ArgumentCaptor<Request> request = ArgumentCaptor.forClass(Request.class);
        HashMap<String, String> mockTemplateData = new HashMap<>();
        mockTemplateData.put("first_name", "FirstName");

        when(mockSendGrid.api(request.capture())).thenReturn(new Response());

        //ACT
        emailService.sendEmail("testemail@test.com", "testtemplate", mockTemplateData);

        //ASSERT
        JSONObject requestBody = new JSONObject(request.getValue().getBody());

        Assert.assertNotNull(request.getValue());
        Assert.assertEquals(Method.POST, request.getValue().getMethod());
        Assert.assertEquals("mail/send", request.getValue().getEndpoint());
        Assert.assertEquals("FirstName",
                requestBody.getJSONArray("personalizations")
                        .getJSONObject(0).getJSONObject("dynamic_template_data")
                        .get("first_name").toString()
        );
        Assert.assertEquals("testtemplate",
                requestBody.get("template_id").toString()
        );
        Assert.assertEquals("testemail@test.com",
                requestBody.getJSONArray("personalizations")
                        .getJSONObject(0).getJSONArray("to")
                        .getJSONObject(0).get("email")
        );
    }

    @Test
    public void testSendEmailException() throws IOException, JSONException {
        //ARRANGE
        ArgumentCaptor<Request> request = ArgumentCaptor.forClass(Request.class);
        HashMap<String, String> mockTemplateData = new HashMap<>();
        mockTemplateData.put("first_name", "FirstName");

        when(mockSendGrid.api(request.capture())).thenThrow(new IOException());

        //ACT
        emailService.sendEmail("testemail@test.com", "testtemplate", mockTemplateData);

        //ASSERT
        JSONObject requestBody = new JSONObject(request.getValue().getBody());

        Assert.assertNotNull(request.getValue());
        Assert.assertEquals(Method.POST, request.getValue().getMethod());
        Assert.assertEquals("mail/send", request.getValue().getEndpoint());
        Assert.assertEquals("FirstName",
                requestBody.getJSONArray("personalizations")
                        .getJSONObject(0).getJSONObject("dynamic_template_data")
                        .get("first_name").toString()
        );
        Assert.assertEquals("testtemplate",
                requestBody.get("template_id").toString()
        );
        Assert.assertEquals("testemail@test.com",
                requestBody.getJSONArray("personalizations")
                        .getJSONObject(0).getJSONArray("to")
                        .getJSONObject(0).get("email")
        );
    }
}
