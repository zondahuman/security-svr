package com.abin.lee.security.service.rsa.test;


import com.abin.lee.security.common.util.HttpClientUtil;
import com.abin.lee.security.service.rsa.RSAUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.http.Consts;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
*
*/
public class XiaoDaoTest {
//    private static final String httpURL = "https://172.16.2.133:443/load/platform";
    private static final String httpURL = "https://36.110.112.103/load/platform";
//    private static final String httpURL = "https://172.16.2.134/load/platform";
//    private static final String httpURL = "http://10.10.7.246:8080/heika-xiaodai-web/load/platform";

    @Test
    public void test_AssertParameter_LoanInfoReport() throws Exception {
        String apiName="LoanInfoReport";

        assertCommonParameters(apiName);

        String[] parameters = new String[]{"contractNo","contractName","customerName","certificateNo","contractSignDate",
                "contractBeginDate","contractEndDate","contractAmount","intRate","priPltyRate",
                "linkman","telephone","ddAmt","ddDate","matureDate","signDate","term"
        };

        for(String param : parameters){
            assertNormalParameters(apiName, param);
        }


        assertStringParameter(apiName,"contractNo",50);
        assertStringParameter(apiName,"contractName",100);
        assertStringParameter(apiName,"customerName",100);
        assertStringParameter(apiName,"certificateNo",50);

        assertDoubleParameter(apiName,"contractAmount",16,2);
        assertDoubleParameter(apiName,"intRate",14,8);
        assertDoubleParameter(apiName,"priPltyRate",14,8);
        assertDoubleParameter(apiName,"ddAmt",16,2);
        assertDoubleParameter(apiName,"term",6,0);

        assertStringParameter(apiName,"linkman",100);
        assertStringParameter(apiName,"telephone",20);

        assertRepaymentPlan(apiName);

        // ID number
        assertCertficateNo(apiName);
    }

    @Test
    public void test_AssertParameter_RepaymentPlanUpdate() throws Exception {
        String apiName="RepaymentPlanUpdate";

        JSONObject content ;
        JSONObject response ;

        assertNormalParameters(apiName,"contractName");
        assertNormalParameters(apiName,"contractNo");
        assertRepaymentPlan(apiName);


        // 上报合同
        content = getLoanInfoReportParameters();
        response = sendRequest(content.toString());
        Assert.assertEquals("请求失败","000000",response.get("errorCode"));
        Assert.assertTrue("合同上报失败", isSuccess(content.get("reportedId").toString()));

        String contractNo = content.get("contractNo").toString();
        String contractName = content.get("contractNo").toString();

        // 错误的合同名字
        content = getPlanUpdateParameters();
        content.put("contractName",contractName);
        content.put("contractNo",contractNo);
        content.put("contractName","错误名字");
        response = sendRequest(content.toString());
        Assert.assertEquals("请求返回值错误","000002",response.get("errorCode"));
        Assert.assertTrue(response.toString().contains("contractNo或者contractName不存在"));

        //错误的合同号
        content = getPlanUpdateParameters();
        content.put("contractName",contractName);
        content.put("contractNo",contractNo);
        content.put("contractNo","错误No");
        response = sendRequest(content.toString());
        Assert.assertEquals("请求返回值错误","000002",response.get("errorCode"));
        Assert.assertTrue(response.toString().contains("contractNo或者contractName不存在"));

    }

    @Test
    public void test_AssertParameter_Repayment() throws Exception {
        String apiName="Repayment";

        assertCommonParameters(apiName);

        String[] parameters = new String[]{
                "operatorType","contractNo","repayDate","counter","customerName",
                "certificateNo","repayPriAmt","repayIntAmt","delayAmt","delayInterest",
                "delayFee","priPltyRate","delayDays","startDate","endDate","receiptType"
        };

        for(String param : parameters){
            assertNormalParameters(apiName, param);
        }


        assertStringParameter(apiName,"operatorType",100);
        assertStringParameter(apiName,"contractNo",50);
        assertDoubleParameter(apiName,"counter",5,0);
        assertStringParameter(apiName,"customerName",100);

        assertStringParameter(apiName,"customerName",100);
        assertStringParameter(apiName,"certificateNo",50);

        assertDoubleParameter(apiName,"repayPriAmt",16,2);
        assertDoubleParameter(apiName,"repayIntAmt",16,2);
        assertDoubleParameter(apiName,"delayAmt",16,2);
        assertDoubleParameter(apiName,"delayInterest",16,2);
        assertDoubleParameter(apiName,"delayFee",16,2);

        assertDoubleParameter(apiName,"priPltyRate",14,8);
        assertDoubleParameter(apiName,"delayDays",5,0);

        // ID number
        assertCertficateNo(apiName);
    }

    @Test
    public void test_AssertParameter_LoanInfoDelete() throws Exception {
        String apiName="LoanInfoDelete";

        assertCommonParameters(apiName);

        String[] parameters = new String[]{
                "contractNo","contractName"
        };

        for(String param : parameters){
            assertNormalParameters(apiName, param);
        }


        assertStringParameter(apiName,"contractNo",50);
        assertStringParameter(apiName,"contractName",100);
    }

    @Test
    public void test_AssertParameter_QueryReportedInfo() throws Exception {
        String apiName="QueryReportedInfo";

        assertCommonParameters(apiName, false);

        String[] parameters = new String[]{
                "searchId"
        };

        for(String param : parameters){
            assertNormalParameters(apiName, param);
        }

        assertStringParameter(apiName,"searchId",100);
    }


    private JSONObject getParametersByAPI(String apiName){
        return  getParametersByAPI(apiName, null);
    }

    private JSONObject getParametersByAPI(String apiName, String contractNo){
        if(apiName.equalsIgnoreCase("LoanInfoReport")){
            return getLoanInfoReportParameters();
        }

        if(apiName.equalsIgnoreCase("RepaymentPlanUpdate")){
            JSONObject content = getPlanUpdateParameters();
            if(contractNo != null){
                content.put("contractNo",contractNo);
            }
            return content;
        }

        if(apiName.equalsIgnoreCase("Repayment")){
            JSONObject content =  getRepaymentParameters();
            if(contractNo != null){
                content.put("contractNo",contractNo);
            }
            return content;
        }

        if(apiName.equalsIgnoreCase("QueryReportedInfo")){
            JSONObject content =  getQueryReportedInfoParameters();
            if(contractNo != null){
                content.put("contractNo",contractNo);
            }
            return content;
        }

        return  null;
    }

    private void assertCommonParameters(String apiName) throws Exception {
        assertCommonParameters(apiName,true);
    }
    private void assertCommonParameters(String apiName, boolean isReportIdIncluded) throws Exception {
        JSONObject content;
        JSONObject response;
        String[] parameters = new String[]{"reportedId","service","serviceVersion","partner","businessLine"};
        for(String param : parameters) {

            if(! isReportIdIncluded && param.equals("reportedId")){
                continue;
            }

            System.out.println("########################### validate " + param + " ###########################");

            content = getParametersByAPI(apiName);
            content.remove(param);
            response = sendRequest(content.toString());
            Assert.assertEquals("请求返回值错误","000002",response.get("errorCode"));
            Assert.assertTrue(response.toString().contains(param + "不能为空"));

            content = getParametersByAPI(apiName);
            content.put(param,"");
            response = sendRequest(content.toString());
            Assert.assertEquals("请求返回值错误","000002",response.get("errorCode"));
            Assert.assertTrue(response.toString().contains(param + "不能为空"));
        }

        content = getParametersByAPI(apiName);
        content.put("partner", "WrongValue");
        response = sendRequest(content.toString());
        Assert.assertEquals("请求返回值错误","000002",response.get("errorCode"));
        Assert.assertTrue(response.toString().contains("businessLine或者partner的输入不合法"));

    }

    private void assertCertficateNo(String apiName) throws Exception {

        System.out.println("########################### validate certificateNo ###########################");

        JSONObject content ;
        JSONObject response ;

        content = getParametersByAPI(apiName);
        content.put("certificateNo","33078119861018070X");
        response = sendRequest(content.toString());
        Assert.assertEquals("请求返回值错误","000002",response.get("errorCode"));
        Assert.assertTrue(response.get("errorMsg").toString().contains("参数校验失败")
                || response.get("errorMsg").toString().contains("certificateNo")
        );
    }

    private void assertNormalParameters(String apiName, String param) throws Exception {
        assertNormalParameters(apiName, param, false);
    }

    private void assertNormalParameters(String apiName, String param, boolean isRepaymentPlan) throws Exception {

        System.out.println("########################### validate " + param + " ###########################");

        JSONObject content;
        JSONObject response;
        JSONObject targetJson;

        content = getParametersByAPI(apiName);
        if(isRepaymentPlan){
            targetJson = (JSONObject) ((JSONArray)content.get("repaymentPlan")).get(0);
        } else {
            targetJson = content;
        }

        targetJson.remove(param);
        response = sendRequest(content.toString());
        Assert.assertEquals("请求返回值错误","000002",response.get("errorCode"));
        Assert.assertTrue(response.get("errorMsg").toString().contains("参数校验失败")
                || response.get("errorMsg").toString().contains(param)
        );

        content = getParametersByAPI(apiName);
        if(isRepaymentPlan){
            targetJson = (JSONObject) ((JSONArray)content.get("repaymentPlan")).get(0);
        } else {
            targetJson = content;
        }

        targetJson.put(param,"");
        response = sendRequest(content.toString());
        Assert.assertEquals("请求返回值错误","000002",response.get("errorCode"));
        Assert.assertTrue(response.get("errorMsg").toString().contains("参数校验失败")
                || response.get("errorMsg").toString().contains(param)
        );

        if(param.contains("Date")){
            content = getParametersByAPI(apiName);
            if(isRepaymentPlan){
                targetJson = (JSONObject) ((JSONArray)content.get("repaymentPlan")).get(0);
            } else {
                targetJson = content;
            }

            System.out.println("**************** : 203003030");
            targetJson.put(param,"203003030");
            response = sendRequest(content.toString());
            Assert.assertEquals("请求返回值错误","000002",response.get("errorCode"));
            Assert.assertTrue(response.get("errorMsg").toString().contains("参数校验失败")
                    || response.get("errorMsg").toString().contains(param)
            );

            content = getParametersByAPI(apiName);
            if(isRepaymentPlan){
                targetJson = (JSONObject) ((JSONArray)content.get("repaymentPlan")).get(0);
            } else {
                targetJson = content;
            }

            System.out.println("**************** : 99099090");
            targetJson.put(param,"99099090");
            response = sendRequest(content.toString());
            Assert.assertEquals("请求返回值错误","000002",response.get("errorCode"));
            Assert.assertTrue(response.get("errorMsg").toString().contains("参数校验失败")
                    || response.get("errorMsg").toString().contains(param)
            );
        }

    }

    private void assertStringParameter(String apiName,String param, int length) throws Exception {
        assertStringParameter(apiName, param, length, false);
    }

    private void assertStringParameter(String apiName,String param, int length, boolean isRepaymentPlan) throws Exception {

        System.out.println("########################### validate " + param + " ###########################");

        JSONObject content;
        JSONObject response;

        StringBuffer sb = new StringBuffer();
        for(int i = 0; i <= length; i++){
            sb.append("1");
        }

        content = getParametersByAPI(apiName);
        content.put(param,sb.toString());
        response = sendRequest(content.toString());
        Assert.assertEquals("请求返回值错误","000002",response.get("errorCode"));
        Assert.assertTrue(response.get("errorMsg").toString().contains("参数校验失败")
            || response.get("errorMsg").toString().contains(param)
        );

    }

    private void assertRepaymentPlan(String apiName) throws Exception {
        String planKey = "repaymentPlan";

        JSONObject content;
        JSONObject response;
        JSONArray plans;

        System.out.println("########################### validate repaymentPlan ###########################");

        content = getParametersByAPI(apiName);
        plans = (JSONArray) content.get(planKey);
        plans.clear();
        content.put(planKey,plans);
        response = sendRequest(content.toString());
        Assert.assertEquals("请求返回值错误","000002",response.get("errorCode"));
        Assert.assertTrue(response.get("errorMsg").toString().contains("参数校验失败")
                || response.get("errorMsg").toString().contains("repaymentPlan")
        );

        content = getParametersByAPI(apiName);
        content.remove(planKey);
        response = sendRequest(content.toString());
        Assert.assertEquals("请求返回值错误","000002",response.get("errorCode"));
        Assert.assertTrue(response.get("errorMsg").toString().contains("参数校验失败")
                || response.get("errorMsg").toString().contains("repaymentPlan")
        );


        String[] parameters = new String[]{
            "counter","repayDate","repayPriAmt","repayIntAmt","startDate", "endDate"
        };

        for(String param : parameters){
            assertNormalParameters("LoanInfoReport", param, true);
        }

        assertDoubleParameter(apiName,"counter",5,0,true);
        assertDoubleParameter(apiName,"repayPriAmt",16,2,true);
        assertDoubleParameter(apiName,"repayIntAmt",16,2,true);

    }

    private void assertDoubleParameter(String apiName,String param, int length, int decimalLength) throws Exception {
        assertDoubleParameter(apiName,param,length,decimalLength,false);
    }

    private void assertDoubleParameter(String apiName,String param, int length, int decimalLength, boolean isRepaymentPlan) throws Exception {

        System.out.println("########################### validate " + param + " ###########################");

        JSONObject content;
        JSONObject response;
        JSONObject targetJson;

        // not number
        content = getParametersByAPI(apiName);
        if(isRepaymentPlan){
            targetJson = (JSONObject) ((JSONArray)content.get("repaymentPlan")).get(0);
        } else {
            targetJson = content;
        }

        targetJson.put(param,"x");
        response = sendRequest(content.toString());
        Assert.assertEquals("请求返回值错误","000002",response.get("errorCode"));
        Assert.assertTrue(response.get("errorMsg").toString().contains("参数校验失败")
                || response.get("errorMsg").toString().contains(param)
        );


        StringBuffer sb = new StringBuffer();
        for(int i = 0; i <= length ; i++){
            sb.append("1");
        }
        content = getParametersByAPI(apiName);
        if(isRepaymentPlan){
            targetJson = (JSONObject) ((JSONArray)content.get("repaymentPlan")).get(0);
        } else {
            targetJson = content;
        }

        targetJson.put(param,sb.toString());
        response = sendRequest(content.toString());
        Assert.assertEquals("请求返回值错误","000002",response.get("errorCode"));
        Assert.assertTrue(response.get("errorMsg").toString().contains("参数校验失败")
                || response.get("errorMsg").toString().contains(param)
        );


        if(decimalLength > 0 && apiName.equalsIgnoreCase("LoanInfoReport")){
            sb = new StringBuffer();
            for(int i = 0; i <= decimalLength ; i++){
                sb.append("1");
            }
            content = getParametersByAPI(apiName);
            if(isRepaymentPlan){
                targetJson = (JSONObject) ((JSONArray)content.get("repaymentPlan")).get(0);
            } else {
                targetJson = content;
            }

            targetJson.put(param,"1." + sb.toString());
            response = sendRequest(content.toString());
            Assert.assertEquals("请求返回值错误","000002",response.get("errorCode"));
            Assert.assertTrue(response.get("errorMsg").toString().contains("参数校验失败")
                    || response.get("errorMsg").toString().contains(param)
            );

            //正常请求，边界值
            sb = new StringBuffer();
            for(int integerLength = length - decimalLength, i =0; i< integerLength; i++){
                sb.append("1");
            }
            sb.append(".");
            for(int i = 0; i < decimalLength ; i++){
                sb.append("1");
            }
            content = getParametersByAPI(apiName);
            if(isRepaymentPlan){
                targetJson = (JSONObject) ((JSONArray)content.get("repaymentPlan")).get(0);
            } else {
                targetJson = content;
            }

            targetJson.put(param, sb.toString());
            response = sendRequest(content.toString());
            Assert.assertEquals("请求返回值错误","000000",response.get("errorCode"));
            Assert.assertTrue("上报失败", isSuccess(content.get("reportedId").toString()));
        }

    }


    private JSONObject sendRequest(String content) throws Exception {

        JSONObject responseJson = null;

        CloseableHttpClient httpclient = HttpClientUtil.getHttpsClient();
        try {
            HttpPost httpPost = new HttpPost(httpURL);
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            System.out.println(content);
            String sign = RSAUtil.signWithMD5(content);
            content = RSAUtil.encrypt(content);
            nvps.add(new BasicNameValuePair("content", content));
            nvps.add(new BasicNameValuePair("sign", sign));
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8));
//            System.out.println("Executing request: " + httpPost.getRequestLine());
            CloseableHttpResponse response = httpclient.execute(httpPost);
//            System.out.println(response.getStatusLine());

            String responseStr = EntityUtils.toString(response.getEntity());
            System.out.println(responseStr);

            responseJson = JSONObject.fromObject(responseStr);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            httpclient.close();
        }

        return responseJson;
    }

    private static JSONObject getLoanInfoReportParameters (){
        String currentTime = "" + System.currentTimeMillis();
        System.out.println(currentTime);

        String content = "{" +
                "\"reportedId\":\"RPID" + System.currentTimeMillis() + "\"," +
                "\"service\":\"loanInfoReport\"," +
                "\"serviceVersion\":\"1.0\"," +
                "\"partner\":\"YY\"," +
                "\"businessLine\":\"yy\"," +

                "\"contractNo\":\"c" + currentTime + "\"," +
                "\"contractName\":\"消费合同\"," +
                "\"customerName\":\"肖恩小羊" + currentTime + "\"," +
                "\"certificateNo\":\"330781198610180707\"," +
                "\"contractSignDate\":\"20161212\"," +
                "\"contractBeginDate\":\"20161212\"," +
                "\"contractEndDate\":\"20181212\"," +
                "\"contractAmount\":\"2000\"," +
                "\"intRate\":\"10\"," +
                "\"priPltyRate\":\"10\"," +
                "\"linkman\":\"李四" + currentTime + "\"," +
                "\"telephone\":\"13814785214\"," +
                "\"ddAmt\":\"1200\"," +
                "\"ddDate\":\"20161213\"," +
                "\"matureDate\":\"20181212\"," +
                "\"signDate\":\"20161212\"," +
                "\"term\":\"24\"," +
                "\"guaranteeType\":\"240001\"," +
                "\"zone\":\"230001\"," +
                "\"purpose\":\"260001\"," +
                "\"industry\":\"290001\"," +

                "\"rateType\":\"520001\"," +
                "\"rateCalcMode\":\"270001\"," +
                "\"riskLevel\":\"510002\"," +
                "\"guarContractNo\":\"HK_ABCDEFGH\"," +
                "\"warrantNo\":\"QZH\"," +
                "\"collateralType\":\"370001\"," + //(可不填)
                "\"hypopledgeAmount\":\"1212345678912.34\"," +
                "\"logout\":\"740001\"," +
                "\"logoutDate\":\"20181115\"," +
                "\"remark\":\"这是备注，各单位\"," +

                "\"repaymentPlan\":" +
                "[" +
                "{" +
                "\"counter\":\"1\"," +
                "\"repayDate\":\"20181112\"," +
                "\"repayPriAmt\":\"200.2\"," +
                "\"repayIntAmt\":\"10.22\"," +
                "\"startDate\":\"20161112\"," +
                "\"endDate\":\"20161112\"" +
                "}," +
                "{" +
                "\"counter\":\"2\"," +
                "\"repayDate\":\"20181212\"," +
                "\"repayPriAmt\":\"200.2\"," +
                "\"repayIntAmt\":\"10.22\"," +
                "\"startDate\":\"20161212\"," +
                "\"endDate\":\"20161212\"" +
                "}" +
                "]" +
                "}";

        return JSONObject.fromObject(content);
    }


    private JSONObject getPlanUpdateParameters (){

        String content = "{" +
                "\"reportedId\":\"RPID" + System.currentTimeMillis() + "\"," +
                "\"service\":\"payPlanInfoUpdate\"," +
                "\"serviceVersion\":\"1.0\"," +
                "\"partner\":\"YY\"," +
                "\"businessLine\":\"yy\"," +

                "\"contractNo\":\"c" + "FakeValue" + "\"," +
                "\"contractName\":\"消费合同\"," +
                "\"repaymentPlan\":" +
                "[" +
                "{" +
                "\"counter\":\"1\"," +
                "\"repayDate\":\"20191112\"," +
                "\"repayPriAmt\":\"920.2\"," +
                "\"repayIntAmt\":\"90.22\"," +
                "\"startDate\":\"20191112\"," +
                "\"endDate\":\"20191112\"" +
                "}," +
                "{" +
                "\"counter\":\"2\"," +
                "\"repayDate\":\"20191212\"," +
                "\"repayPriAmt\":\"900.2\"," +
                "\"repayIntAmt\":\"90.22\"," +
                "\"startDate\":\"20191212\"," +
                "\"endDate\":\"20191212\"" +
                "}" +
                "]" +
                "}";

        return JSONObject.fromObject(content);
    }

    public JSONObject getRepaymentParameters (){
        String content = "{" +
                "\"reportedId\":\"RPID" + System.currentTimeMillis() + "\"," +
                "\"service\":\"repayment\"," +
                "\"serviceVersion\":\"1.0\"," +
                "\"partner\":\"YY\"," +
                "\"businessLine\":\"yy\"," +

                "\"operatorType\":\"ADD\"," +
                "\"contractNo\":\"c" + "FakeValue" + "\"," +
                "\"repayDate\":\"20160623\"," +
                "\"counter\":\"1000\"," +
                "\"customerName\":\"张三\"," +
                "\"certificateNo\":\"330781198610180707\"," +
                "\"repayPriAmt\":\"20000.2\"," +
                "\"repayIntAmt\":\"1000.3\"," +
                "\"delayAmt\":\"40000.5\"," +
                "\"delayInterest\":\"3000.1\"," +
                "\"delayFee\":\"20000\"," +
                "\"priPltyRate\":\"10\"," +
                "\"delayDays\":\"36\"," +
                "\"startDate\":\"20161213\"," +
                "\"endDate\":\"20181212\"," +
                "\"receiptType\":\"NROMAL\"," +
                "}";

        return JSONObject.fromObject(content);
    }

    public JSONObject getQueryReportedInfoParameters (){
        String content = "{" +
                "\"reportedId\":\"RPID" + System.currentTimeMillis() + "\"," +
                "\"service\":\"queryReportedInfo\"," +
                "\"serviceVersion\":\"1.0\"," +
                "\"partner\":\"YY\"," +
                "\"businessLine\":\"yy\"," +

                "\"searchId\":\"" + "FakeValue" + "\"," +
                "}";

        return JSONObject.fromObject(content);
    }

    public JSONObject getLoanInfoDeleteParameters (){
        String content = "{" +
                "\"reportedId\":\"RPID" + System.currentTimeMillis() + "\"," +
                "\"service\":\"loanInfoDelete\"," +
                "\"serviceVersion\":\"1.0\"," +
                "\"partner\":\"YY\"," +
                "\"businessLine\":\"yy\"," +

                "\"contractNo\":\"c" + "FakeValue" + "\"," +
                "\"contractName\":\"消费合同\"" +
                "}";

        return JSONObject.fromObject(content);
    }


    @Test
    public void testProcess() throws Exception {

        JSONObject response ;

        // 合同上报
        JSONObject content = runProcessLoanInfReport();
        String contractNo = content.get("contractNo").toString();
        // 修改还款计划
        runProcessRepaymentPlanUpdate(contractNo);
        // 还款添加
        runProcessRepaymentAdd(contractNo);
        // 重复提交，报错
        System.out.println("++++++++++++++++++++  重复提交，报错  ++++++++++++++++++++");
        content = getRepaymentParameters();
        content.put("contractNo",contractNo);
        response = sendRequest(content.toString());
        Assert.assertEquals("重复提交还款入库成功","000007",response.get("errorCode"));
        // 修改还款
        runProcessRepaymentUpdate(contractNo);
        // 删除还款
        runProcessRepaymentDelete(contractNo);
        // 还款再次添加还款
        runProcessRepaymentAdd(contractNo);
        // 删除合同
        runProcessLoanInfDelete(contractNo);

    }

    @Test
    public void testProcess_LoanAdd() throws Exception {
        runProcessLoanInfReport();
    }

    @Test
    public void testProcess_LoanAdd_Repeat() throws Exception {

        JSONObject content = runProcessLoanInfReport();
        JSONObject response;

        String reportedId = content.get("content").toString();
        String contractNo = content.get("contractNo").toString();

        content = getLoanInfoReportParameters();
        content.put("reportedId",reportedId);
        content.put("contractNo",contractNo);
        response = sendRequest(content.toString());
        Assert.assertEquals("请求失败","000001",response.get("errorCode"));
        Assert.assertTrue(response.toString().contains("reportedId重复"));


        content = getLoanInfoReportParameters();
        content.put("contractNo",contractNo);

        response = sendRequest(content.toString());
        Assert.assertEquals("请求失败","000003",response.get("errorCode"));
        Assert.assertTrue(response.toString().contains("重复上报"));
    }

    @Test
    public void testProcess_LoanAdd_LoanDel() throws Exception {
        JSONObject content = runProcessLoanInfReport();
        String contractNo = content.get("contractNo").toString();
        runProcessLoanInfDelete(contractNo);
    }

    @Test
    public void testProcess_LoanAdd_PlanUpdate_LoanDel() throws Exception {

        JSONObject content = runProcessLoanInfReport();
        String contractNo = content.get("contractNo").toString();
        runProcessRepaymentPlanUpdate(contractNo);
        runProcessLoanInfDelete(contractNo);

    }

    @Test
    public void testProcess_LoanAdd_PlanUpdate_PayAdd_LoanDel() throws Exception {

        JSONObject content = runProcessLoanInfReport();
        String contractNo = content.get("contractNo").toString();
        runProcessRepaymentPlanUpdate(contractNo);
        runProcessRepaymentAdd(contractNo);
        runProcessLoanInfDelete(contractNo);

    }

    @Test
    public void testProcess_LoanAdd_PlanUpdate_PayAdd_PayUpdate_LoanDel() throws Exception {

        JSONObject content = runProcessLoanInfReport();
        String contractNo = content.get("contractNo").toString();
        runProcessRepaymentPlanUpdate(contractNo);
        runProcessRepaymentAdd(contractNo);
        runProcessRepaymentUpdate(contractNo);
        runProcessLoanInfDelete(contractNo);
    }

    @Test
    public void testProcess_LoanAdd_PlanUpdate_PayAdd_PayUpdate_PayDel_LoanDel() throws Exception {
        JSONObject content = runProcessLoanInfReport();
        String contractNo = content.get("contractNo").toString();
        runProcessRepaymentPlanUpdate(contractNo);
        runProcessRepaymentAdd(contractNo);
        runProcessRepaymentUpdate(contractNo);
        runProcessRepaymentDelete(contractNo);
        runProcessLoanInfDelete(contractNo);
    }

    private JSONObject runProcessLoanInfReport() throws Exception {
        System.out.println("++++++++++++++++++++  合同上报  ++++++++++++++++++++");
        JSONObject content = getLoanInfoReportParameters();
        JSONObject response = sendRequest(content.toString());
        Assert.assertEquals("合同上报入库失败","000000",response.get("errorCode"));
        Assert.assertTrue("合同上报失败", isSuccess(content.get("reportedId").toString()));

        return content;
    }

    private void runProcessRepaymentPlanUpdate(String contractNo) throws Exception {

        System.out.println("++++++++++++++++++++  修改还款计划  ++++++++++++++++++++");
        JSONObject content = getPlanUpdateParameters();
        content.put("contractNo",contractNo);
        JSONObject response = sendRequest(content.toString());
        Assert.assertEquals("还款计划修改入库失败","000000",response.get("errorCode"));
        Assert.assertTrue("还款计划修改失败", isSuccess(content.get("reportedId").toString()));
    }

    private void runProcessRepaymentAdd(String contractNo) throws Exception {
        System.out.println("++++++++++++++++++++  还款  ++++++++++++++++++++");
        JSONObject content = getRepaymentParameters();
        content.put("contractNo",contractNo);
        JSONObject response = sendRequest(content.toString());
        Assert.assertEquals("还款入库失败","000000",response.get("errorCode"));
        Assert.assertTrue("还款失败", isSuccess(content.get("reportedId").toString()));
    }

    private void runProcessRepaymentUpdate(String contractNo) throws Exception {
        System.out.println("++++++++++++++++++++  修改还款  ++++++++++++++++++++");
        JSONObject content = getRepaymentParameters();
        content.put("operatorType","UPDATE");
        content.put("contractNo",contractNo);
        content.put("delayAmt","123");
        JSONObject response = sendRequest(content.toString());
        Assert.assertEquals("还款入库失败","000000",response.get("errorCode"));
        Assert.assertTrue("还款修改失败", isSuccess(content.get("reportedId").toString()));
    }

    private void runProcessRepaymentDelete(String contractNo) throws Exception {
        System.out.println("++++++++++++++++++++  删除还款  ++++++++++++++++++++");
        JSONObject content = getRepaymentParameters();
        content.put("operatorType","DELETE");
        content.put("contractNo",contractNo);
        JSONObject response = sendRequest(content.toString());
        Assert.assertEquals("还款入库失败","000000",response.get("errorCode"));
        Assert.assertTrue("还款删除失败", isSuccess(content.get("reportedId").toString()));
    }

    private void runProcessLoanInfDelete(String contractNo) throws Exception {
        System.out.println("++++++++++++++++++++  删除合同  ++++++++++++++++++++");
        JSONObject content = getLoanInfoDeleteParameters();
        content.put("contractNo",contractNo);
        JSONObject response = sendRequest(content.toString());
        Assert.assertEquals("合同删除入库失败","000000",response.get("errorCode"));
        Assert.assertTrue("合同删除失败", isSuccess(content.get("reportedId").toString()));
    }

    public boolean isSuccess(String searchId) throws Exception {

        for(int counter = 0; counter < 20; counter++) {
            JSONObject content = getQueryReportedInfoParameters();
            content.put("searchId",searchId);
            JSONObject response = sendRequest(content.toString());
            if (!response.get("errorCode").equals("000005")) {
                return response.get("errorCode").equals("000006");
            }
            Thread.sleep(15000);
        }

        return false;
    }

}

