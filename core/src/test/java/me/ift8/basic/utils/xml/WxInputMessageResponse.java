package me.ift8.basic.utils.xml;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlCData;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import lombok.ToString;

/**
 * POST的XML数据包转换为消息接受对象
 *
 * <p>
 * 由于POST的是XML数据包，所以不确定为哪种接受消息，<br/>
 * 所以直接将所有字段都进行转换，最后根据<tt>MsgType</tt>字段来判断取何种数据
 * </p>
 */
@Data
@ToString
@JacksonXmlRootElement(localName = "xml")
public class WxInputMessageResponse {
    @JacksonXmlCData
    @JacksonXmlProperty(localName = "ToUserName")
    private String toUserName;
    @JacksonXmlCData
    @JacksonXmlProperty(localName = "FromUserName")
    private String fromUserName;
    @JacksonXmlCData
    @JacksonXmlProperty(localName = "CreateTime")
    private Long createTime;
    @JacksonXmlProperty(localName = "MsgType")
    private String msgType = "text";
    @JacksonXmlCData
    @JacksonXmlProperty(localName = "MsgId")
    private Long msgId;
    // 文本消息  
    @JacksonXmlCData
    @JacksonXmlProperty(localName = "Content")
    private String content;
    // 图片消息  
    @JacksonXmlCData
    @JacksonXmlProperty(localName = "PicUrl")
    private String picUrl;
    // 位置消息  
    @JacksonXmlCData
    @JacksonXmlProperty(localName = "LocationX")
    private String locationX;
    @JacksonXmlCData
    @JacksonXmlProperty(localName = "LocationY")
    private String locationY;
    @JacksonXmlCData
    @JacksonXmlProperty(localName = "Scale")
    private Long scale;
    @JacksonXmlCData
    @JacksonXmlProperty(localName = "Label")
    private String label;
    // 链接消息  
    @JacksonXmlCData
    @JacksonXmlProperty(localName = "Title")
    private String title;
    @JacksonXmlCData
    @JacksonXmlProperty(localName = "Description")
    private String description;
    @JacksonXmlCData
    @JacksonXmlProperty(localName = "Url")
    private String url;
    // 语音信息  
    @JacksonXmlCData
    @JacksonXmlProperty(localName = "MediaId")
    private String mediaId;
    @JacksonXmlCData
    @JacksonXmlProperty(localName = "Format")
    private String format;
    @JacksonXmlCData
    @JacksonXmlProperty(localName = "Recognition")
    private String recognition;
    // 事件  
    @JacksonXmlCData
    @JacksonXmlProperty(localName = "Event")
    private String event;
    @JacksonXmlCData
    @JacksonXmlProperty(localName = "EventKey")
    private String eventKey;
    @JacksonXmlCData
    @JacksonXmlProperty(localName = "Ticket")
    private String ticket;

}
