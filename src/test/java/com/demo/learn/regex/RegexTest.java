package com.demo.learn.regex;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * java正则表达式，匹配时，必须使用matcher对象来接收，然后再执行matcher.find、matcher.group 等操作，
 * 否则会报错：No match found
 * @author luwt
 * @date 2021/8/23
 */
public class RegexTest {


    public static void main(String[] args) {
        String httpUrlWithPort = "http://test.local.org:8080/user/test";
        String httpUrl = "http://test.local.org/user/test";
        String httpsUrl = "https://test.local.org:8080/user/test";
        String httpSecondLevelUrl = "http://local.org/user/test";
        String httpMultiLevelUrl = "http://test.dev.host.local.org:8080/user/test";
        String jOne = "http://j-one.jd.com/";
        String httpIp = "http://127.0.0.1:8080/user/test";
        String localhost = "http://localhost:8080/user/test";
        print(httpUrlWithPort, getSecondLevelDomain(httpUrlWithPort));
        print(httpUrl, getSecondLevelDomain(httpUrl));
        print(httpsUrl, getSecondLevelDomain(httpsUrl));
        print(httpSecondLevelUrl, getSecondLevelDomain(httpSecondLevelUrl));
        print(httpMultiLevelUrl, getSecondLevelDomain(httpMultiLevelUrl));
        print(jOne, getSecondLevelDomain(jOne));
        print(httpIp, getSecondLevelDomain(httpIp));
        print(localhost, getSecondLevelDomain(localhost));
    }

    /**
     * 获取二级域名
     * @param url url
     * @return 二级域名
     */
    public static String getSecondLevelDomain(String url) {
        Pattern pattern = Pattern.compile("(?<=(http|https)://)([a-zA-Z0-9-]+\\.)*(?<result>([a-zA-Z0-9-]+\\.[a-zA-Z0-9-]+))(?=(:\\d+)*?/.*)");
        Matcher matcher = pattern.matcher(url);
        if (matcher.find()) {
            return matcher.group("result");
        }
        return StringUtils.EMPTY;
    }


    /**
     * 打印结果
     * @param url 原url
     * @param secondLevelDomain 二级域名
     */
    public static void print(String url, String secondLevelDomain){
        String message = String.format("原url：[%s], 获取到的二级域名为：[%s]", url, secondLevelDomain);
        System.out.println(message);
    }

}
