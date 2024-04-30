package ru.netology.vetyugov.mipt.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import ru.netology.vetyugov.mipt.service.impl.ForbesServiceImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
@Slf4j
public class AsyncFileReader {

    private static final String RESOURCE_FILE_NAME = "/Forbes.csv";

    @Async
    public CompletableFuture<List<String>> getFileLines() {
        List<String> strings =
                readLines();
        for (String line : strings) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            log.info(line);
        }
        return new CompletableFuture<>();
    }

    private List<String> readLines() {
        List<String> list = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(ForbesServiceImpl.class.getResourceAsStream(RESOURCE_FILE_NAME), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                list.add(line);
            }
        } catch (IOException e) {
            throw new RuntimeException("Ошибка чтения файла " + RESOURCE_FILE_NAME, e);
        }
        return list;
    }
}
