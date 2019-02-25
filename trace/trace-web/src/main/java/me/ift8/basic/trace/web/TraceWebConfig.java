package me.ift8.basic.trace.web;

import com.dianping.cat.servlet.CatFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;

/**
 * Created by IFT8 on 2019-02-13.
 */
@Slf4j
@Configuration
public class TraceWebConfig {

    @Value("${trace.web.excludedUrls:}")
    private String excludedUrls;

    @PostConstruct
    private void init() {
        log.info("[aop] TraceWebConfig 加载完成");
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean(CatFilter catFilter) {
        FilterRegistrationBean<CatFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(catFilter);
        filterRegistrationBean.setEnabled(true);
        filterRegistrationBean.addUrlPatterns("/*");
        //排除url 不进行收集  （可选）
        if (StringUtils.isEmpty(excludedUrls)) {
            excludedUrls = ".js,.png";
        }
        filterRegistrationBean.addInitParameter("excludedUrls", excludedUrls);
        return filterRegistrationBean;
    }

    @Bean
    public CatFilter catFilter() {
        return new CatFilter();
    }
}
