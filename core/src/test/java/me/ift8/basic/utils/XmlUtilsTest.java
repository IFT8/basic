package me.ift8.basic.utils;

import me.ift8.basic.utils.xml.WxInputMessageResponse;
import org.junit.Test;

import java.util.Date;

/**
 * Created by IFT8 on 2018/8/6.
 */
public class XmlUtilsTest {

    @Test
    public void toXml() {
        WxInputMessageResponse wxInputMessageResponse = new WxInputMessageResponse();
        wxInputMessageResponse.setFromUserName("addjskaljdskl");
        wxInputMessageResponse.setCreateTime(new Date().getTime());

        String s = XmlUtils.toXml(wxInputMessageResponse);
        System.out.println(s);
    }

    @Test
    public void toXml1() {
        WxInputMessageResponse wxInputMessageResponse = new WxInputMessageResponse();
        wxInputMessageResponse.setFromUserName("addjskaljdskl");
        wxInputMessageResponse.setCreateTime(new Date().getTime());

        String s = XmlUtils.toXml(wxInputMessageResponse, true);
        System.out.println(s);
    }

    @Test
    public void fromXml() {
        String xml = "<xml><FromUserName><![CDATA[addjskaljdskl]]></FromUserName><CreateTime>1533555521643</CreateTime><MsgType>text</MsgType></xml>";
        WxInputMessageResponse wxInputMessageResponse = XmlUtils.fromXml(xml, WxInputMessageResponse.class);
        System.out.println(wxInputMessageResponse);
    }

    @Test
    public void fromXml1() {
    }

    @Test
    public void fromXml2() {
    }

}