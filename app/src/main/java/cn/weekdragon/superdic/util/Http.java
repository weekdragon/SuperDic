package cn.weekdragon.superdic.util;


import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import cn.weekdragon.superdic.bean.Word;

public class Http {
    private static InputStream inputStream;
    private static byte[] buffer = new byte[1024];
    private static ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private static Word word;

    public static Word getHttp(String path) throws Exception {
        URL url = new URL(path);
        HttpURLConnection response = (HttpURLConnection) url.openConnection();
        response.setRequestMethod("GET");
        response.setConnectTimeout(20 * 1000);
        response.connect();
        if (response.getResponseCode() == 200) {
            inputStream = response.getInputStream();
            word = SolverXml.jiexi2(inputStream);
        }
        return word;
    }
}
