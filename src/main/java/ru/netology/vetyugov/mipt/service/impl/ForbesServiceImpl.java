package ru.netology.vetyugov.mipt.service.impl;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.netology.vetyugov.mipt.model.Forbes;
import ru.netology.vetyugov.mipt.service.ForbesService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class ForbesServiceImpl implements ForbesService {
    private static final String RESOURCE_FILE_NAME = "/Forbes.csv";
    private List<Forbes> forbesList = new ArrayList<>();

    @PostConstruct
    public void init() {
        CompletableFuture<Void> asyncOp = CompletableFuture.runAsync(
                () -> {
                    List<String> strings = readLines();
                    for (String line : strings) {
                        //Специальная задержка, чтобы было видно в консоли, что приложение запустилось в середине обработки файла
                        try {
                            Thread.sleep(5);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        log.info(line);
                    }
                    forbesList.addAll(parseLines(strings));
                }
        );
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

    private List<Forbes> parseLines(List<String> lines) {
        List<Forbes> list = new ArrayList<>();
        for (int i = 1; i < lines.size(); i++) {
            try {
                String[] split = lines.get(i).split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                list.add(Forbes
                        .builder()
                        .rank(Integer.parseInt(split[0].trim()))
                        .name(split[1].trim())
                        .netWorth(Float.parseFloat(split[2].trim()))
                        .age(Integer.parseInt(split[3].trim()))
                        .country(split[4].trim())
                        .source(split[5].trim())
                        .industry(split[6].trim())
                        .build());
            } catch (NumberFormatException nfe) {
                log.warn("Не удалось преобразовать одно из значений к числу в строке " + lines.get(i), nfe);
            }
        }
        return list;
    }

    private Forbes getForbesByName(String name) {
        return forbesList
                .stream()
                .filter(forbes -> forbes.getName().equals(name))
                .findFirst()
                .orElseThrow();
    }

    private void recalculateForbesRank(){
        float lastWorth = forbesList.stream().max(Comparator.comparing(Forbes::getNetWorth)).orElseThrow().getNetWorth();
        int lastRank = 1;
        int countSpace = 0;
        //Сортировка в обратном порядке
        List<Forbes> sorted = forbesList
                .stream()
                .sorted((f1, f2) -> Float.compare(f2.getNetWorth(), f1.getNetWorth()))
                .toList();
        for (Forbes current : sorted) {
            if (lastWorth == current.getNetWorth()) {
                countSpace++;
                current.setRank(lastRank);
            } else {
                lastRank = lastRank + countSpace;
                current.setRank(lastRank);
                countSpace = 0;
            }
            lastWorth = current.getNetWorth();
        }
        forbesList = sorted;
    }

    @Override
    public String getTheYoungest(String country) {
        return forbesList
                .stream()
                .min(Comparator.comparing(Forbes::getAge))
                .orElseThrow()
                .getName();
    }

    @Override
    public List<String> getCompanyNamesByCountryAndIndustry(String country, String industry) {
        return forbesList
                .stream()
                .filter(forbes -> forbes.getIndustry().equals(industry) && forbes.getCountry().equals(country))
                .map(Forbes::getSource)
                .toList();
    }

    @Override
    public String getCompanyByName(String name) {
        return forbesList
                .stream()
                .filter(forbes -> forbes.getName().equals(name))
                .findFirst()
                .orElseThrow()
                .getSource();
    }

    @Override
    public void updateNetWorthByName(String name, float newNetWorth) {
        Forbes forbesByName = getForbesByName(name);
        forbesByName.setNetWorth(newNetWorth);
        recalculateForbesRank();
    }

    @Override
    public void deleteByName(String name) {
        int rankToDel = getForbesByName(name).getRank();
        int indexToDelete = rankToDel - 1;

        forbesList.remove(indexToDelete);

        recalculateForbesRank();
    }

    @Override
    public List<String> getForbesList() {
        return forbesList.stream().map(Forbes::toString).toList();
    }
}
