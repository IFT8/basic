import me.ift8.basic.http.client.HttpUtils;
import me.ift8.basic.http.client.PoolingHttpClientManager;
import org.junit.Test;

/**
 * Created by IFT8 on 2018/9/7.
 */
public class TestJH {
    private PoolingHttpClientManager poolingHttpClientManager = new PoolingHttpClientManager(30, 10_000, 10_000, true);

    String url="https://lottery.dwtv.tv/kz/buyLottery?follow_id=76222&game_amount=1&game_id=18&point_cost=10&token=96eacb5e266c479fadf0fa149174d00a&ts=1536291675&user_ip=180.163.108.210&sign=09fdeb68f66fc4d2a6083dac21051625";

    @Test
    public void aaa(){
        String body = HttpUtils.doGetWithRequestParams(poolingHttpClientManager.getHttpClient(), url, null);
    }

    @Test
    public void aaaa(){
        String body = HttpUtils.doGetWithRequestParams(poolingHttpClientManager.getHttpClient(), url, null);
    }
}
