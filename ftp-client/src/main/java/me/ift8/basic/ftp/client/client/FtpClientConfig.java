package me.ift8.basic.ftp.client.client;

import lombok.Data;
import lombok.ToString;

/**
 * Created by IFT8 on 2017/5/19.
 */
@Data
@ToString(exclude = "password")
public class FtpClientConfig {
    private String host;
    private Integer port = 21;
    private String username;
    private String password;
    //上传的根目录(相对于FTP服务的绝对路径)
    private String workingDir;
    private Integer bufferSize = 1024 * 2;
    //连接超时
    private Integer connectTimeout = 3000;
    //数据超时
    private Integer dataTimeout = 30 * 1000;
    //是否使用SSL
    private Boolean useSecure = false;
}
