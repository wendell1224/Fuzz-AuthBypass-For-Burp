package burp;

import GUI.Tags;
import tools.dic;
import tools.filterPath;

import javax.swing.*;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class BurpExtender implements IBurpExtender, IHttpListener,IScannerCheck{
    public static String name = "Fuzz Auth";
    private PrintWriter stdout;
    private PrintWriter stderr;
    private IBurpExtenderCallbacks callbacks;
    private IExtensionHelpers helpers;
    private Tags tags;
    private String[] payloads = dic.dic();
//    private String[] testpayloads = {"%3b",";"};

    @Override
    public void registerExtenderCallbacks(IBurpExtenderCallbacks callbacks) {
        this.callbacks = callbacks;
        this.helpers = callbacks.getHelpers();
        this.stdout = new PrintWriter(callbacks.getStdout(),true);
        callbacks.setExtensionName(name);
        callbacks.registerHttpListener(this);
        callbacks.registerScannerCheck(this);

        this.tags = new Tags(callbacks,name);
        stdout.println("load success!");
    }

    @Override
    public void processHttpMessage(int toolFlag, boolean messageIsRequest, IHttpRequestResponse messageInfo) {
        if(toolFlag == 4 && !messageIsRequest){   //proxy flag是4 爬虫的8
            String res = helpers.bytesToString(messageInfo.getResponse());

        }
    }


    @Override
    public List<IScanIssue> doPassiveScan(IHttpRequestResponse baseRequestResponse) {
        String urlpath = helpers.analyzeRequest(baseRequestResponse).getUrl().getPath();
        URL baseurl = helpers.analyzeRequest(baseRequestResponse).getUrl();

        String filteredPath = filterPath.filter(urlpath);
        if(filterPath.filter(urlpath) != null &&
                !filterPath.filter(urlpath).contains("js") &&
                !filterPath.filter(urlpath).contains("css") &&
                !filterPath.filter(urlpath).contains("static") &&
                !filterPath.filter(urlpath).contains("images")){
                    if(filterPath.filter(urlpath) != "/"){
                        for (int i=0;i<payloads.length;i=i+1) {
                            String passurl = helpers.bytesToString(baseRequestResponse.getRequest()).replace(urlpath, "/" + payloads[i]  + filteredPath + "/");
                            stdout.println(passurl);
                            IHttpRequestResponse res = callbacks.makeHttpRequest(baseRequestResponse.getHttpService(),helpers.stringToBytes(passurl));
                            String url = helpers.analyzeRequest(baseRequestResponse).getUrl().toString();
            //                stdout.println(url);
                            int statuscode = helpers.analyzeResponse(res.getResponse()).getStatusCode();
            //                stdout.println(statuscode);
                            if(statuscode == 200) {
                                String issue = "可能存在权限绕过！";
                                tags.add(url, statuscode, issue, res,res.getResponse().length);
                            }
                        }
        }
        }
        return null;
    }

    @Override
    public List<IScanIssue> doActiveScan(IHttpRequestResponse baseRequestResponse, IScannerInsertionPoint insertionPoint) {
        return null;
    }

    @Override
    public int consolidateDuplicateIssues(IScanIssue existingIssue, IScanIssue newIssue) {
        return 0;
    }
}

