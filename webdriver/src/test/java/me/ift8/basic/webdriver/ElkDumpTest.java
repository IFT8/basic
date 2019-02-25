package me.ift8.basic.webdriver;

import me.ift8.basic.utils.JsonUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;

/**
 * Created by IFT8 on 2019-01-29.
 */
public class ElkDumpTest {

    public static void main(String[] args) throws Exception {
        List<String> strings = IOUtils.readLines(new FileInputStream(new File("/Users/IFT8/out.txt")), "UTF-8");
        String s = strings.get(0);
        ElkDump elkDump = JsonUtils.fromJson(s, ElkDump.class);
        List<ElkDump.ResponsesResponse> responses = elkDump.getResponses();
        List<ElkDump.ResponsesResponse.HitsResponseX.HitsResponse> hits = responses.get(0).getHits().getHits();
        for (ElkDump.ResponsesResponse.HitsResponseX.HitsResponse hit : hits) {
            String msg = hit.getSource().getTime() + ": " + hit.getSource().getMsg() + "\n";
            IOUtils.write(msg, new FileOutputStream(new File("/Users/IFT8/out_format.txt"), true));

        }

    }
}
