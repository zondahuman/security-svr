package com.abin.lee.security.service.rsa.test;


import com.abin.lee.security.common.json.JsonUtil;
import com.abin.lee.security.common.util.HttpClientUtil;
import com.abin.lee.security.service.rsa.RSAUtil;
import com.google.common.collect.Maps;
import org.apache.http.Consts;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

/**
*
*/
public class PayPlanInfoUpdateTest {
    private static final String httpURL = "http://localhost:7200/load/platformTest";
//    private static final String httpURL = "http://localhost:7200/load/platform";
//    private static final String httpURL = "http://172.16.2.133:9000/load/platform";
//    private static final String httpURL = "https://172.16.2.133:443/load/platform";

    @Test
    public void testLoanInfoDelete() throws IOException {
        CloseableHttpClient httpclient = HttpClientUtil.getHttpsClient();
        try {
            HttpPost httpPost = new HttpPost(httpURL);
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            Map<String, Object> request = Maps.newHashMap();
            request.put("reportedId", UUID.randomUUID().toString());
            request.put("service", "payPlanInfoUpdate");
            request.put("serviceVersion", "1.0");
            request.put("partner", "YOUXIN");
            request.put("businessLine", "YOUXINAAA");
            request.put("contractNo", "YOUXINAAABB");
            request.put("contractName", "YOUXIN");

            Params params = new Params();
            params.setCounter("5");
            params.setRepayDate("20160928");
            params.setRepayPriAmt("1.0");
            params.setRepayIntAmt("15.23");
            params.setStartDate("20150128");
            params.setEndDate("201601284");
            request.put("repaymentPlan", params);

            for (Iterator<Map.Entry<String, Object>> iterator = request.entrySet().iterator(); iterator.hasNext(); ) {
                Map.Entry<String, Object> nav = (Map.Entry<String, Object>) iterator.next();
                request.put(nav.getKey(), nav.getValue());
            }

//            String content = RSAUtil.encrypt(JsonUtil.toJson(request));
//            String sign = RSAUtil.signWithMD5(JsonUtil.toJson(request));

            String content = JsonUtil.toJson(request);
            String sign = JsonUtil.toJson(request);

            nvps.add(new BasicNameValuePair("content", content));
            nvps.add(new BasicNameValuePair("sign", sign));
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8));
            System.out.println("Executing request: " + httpPost.getRequestLine());
            CloseableHttpResponse response = httpclient.execute(httpPost);
            System.out.println("----------------------------------------");
            System.out.println(response.getStatusLine());
            System.out.println(EntityUtils.toString(response.getEntity()));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            httpclient.close();
        }
    }





    public static class Params{
        private String counter;
        private String repayDate;
        private String repayPriAmt;
        private String repayIntAmt;
        private String startDate;
        private String endDate;

        public String getCounter() {
            return counter;
        }

        public void setCounter(String counter) {
            this.counter = counter;
        }

        public String getRepayDate() {
            return repayDate;
        }

        public void setRepayDate(String repayDate) {
            this.repayDate = repayDate;
        }

        public String getRepayPriAmt() {
            return repayPriAmt;
        }

        public void setRepayPriAmt(String repayPriAmt) {
            this.repayPriAmt = repayPriAmt;
        }

        public String getRepayIntAmt() {
            return repayIntAmt;
        }

        public void setRepayIntAmt(String repayIntAmt) {
            this.repayIntAmt = repayIntAmt;
        }

        public String getStartDate() {
            return startDate;
        }

        public void setStartDate(String startDate) {
            this.startDate = startDate;
        }

        public String getEndDate() {
            return endDate;
        }

        public void setEndDate(String endDate) {
            this.endDate = endDate;
        }
    }

}


