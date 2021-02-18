package com.ds.component.locker.util;

import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;

/**
 * @Author onyx
 * on 2019/4/9.
 */
public class ResourceFileUtil {




    public static String readFile(String fileName) throws IOException {

            InputStream inputStream = Objects.requireNonNull(
                ResourceFileUtil.class.getClassLoader().getResourceAsStream(fileName));
            return readFile(inputStream);

    }



    private static String readFile(InputStream inputStream) throws IOException {
        try (
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))
        ) {
            String line;
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = br.readLine()) != null) {
                stringBuilder.append(line)
                    .append(System.lineSeparator());
            }

            return stringBuilder.toString();
        }
    }
}
