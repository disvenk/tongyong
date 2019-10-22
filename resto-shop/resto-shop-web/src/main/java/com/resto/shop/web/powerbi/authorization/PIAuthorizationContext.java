package com.resto.shop.web.powerbi.authorization;

import com.alibaba.fastjson.JSON;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PIAuthorizationContext implements Serializable {
    private String refreshToken;

    public PIAuthorizationContext() {
    }

    public String getAccessToken() {
        if (null == refreshToken) {
            return getAccessTokenFromPassword("password");
        } else {
            return getAccessTokenFromRefreshToken("refresh_token");
        }
    }

    private String getAccessTokenFromPassword(String grantType) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair(PIUrlTemplate.GRANT_TYPE, grantType));
        params.add(new BasicNameValuePair(PIUrlTemplate.USERNAME, PIUrlTemplate.username));
        params.add(new BasicNameValuePair(PIUrlTemplate.PASSWORD, PIUrlTemplate.password));
        return getAccessTokenHandler(params);
    }

    private String getAccessTokenFromRefreshToken(String grantType) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair(PIUrlTemplate.REFRESH_TOKEN, refreshToken));
        params.add(new BasicNameValuePair(PIUrlTemplate.GRANT_TYPE, grantType));
        return getAccessTokenHandler(params);
    }

    private String getAccessTokenHandler(List<NameValuePair> params) {
        try {
            HttpClient httpclient = HttpClients.createDefault();
            HttpPost httppost = new HttpPost(String.format(PIUrlTemplate.ACCESS_TOKEN_URL, PIUrlTemplate.tenant));
            params.add(new BasicNameValuePair(PIUrlTemplate.RESOURCE, PIUrlTemplate.resourceURI));
            params.add(new BasicNameValuePair(PIUrlTemplate.CLIENT_ID, PIUrlTemplate.client_id));

            httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();

            StringBuilder sb = new StringBuilder();

            BufferedReader rd = new BufferedReader(new InputStreamReader(entity.getContent()));
            String line = "";
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            Map map = JSON.parseObject(sb.toString(), Map.class);
            this.refreshToken = map.get("refresh_token").toString();
            String accessToken = map.get("access_token").toString();
            return accessToken;
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(PIAuthorizationContext.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PIAuthorizationContext.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(PIAuthorizationContext.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }


}
