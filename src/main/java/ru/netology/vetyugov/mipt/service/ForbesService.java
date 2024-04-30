package ru.netology.vetyugov.mipt.service;

import java.util.List;

public interface ForbesService {

    /**
     * @param country страна
     * @return имя самого молодого участника списка Forbes из этой страны
     */
    String getTheYoungest(String country);

    /**
     *
     * @param country страна
     * @param industry индустрия
     * @return список компаний из этой страны, работающих в указанной индустрии
     */
    List<String> getCompanyNamesByCountryAndIndustry(String country, String industry);

    /**
     *
     * @param name имя
     * @return название компании по имени
     */
    String getCompanyByName(String name);

    /**
     * Обновить состояние участника по имени
     * @param name имя участника
     * @param newNetWorth новое состояние
     */
    void updateNetWorthByName(String name, float newNetWorth);

    /**
     * Удалить участника из списка по имени
     * @param name имя участника
     */
    void deleteByName(String name);

    /**
     * Для отладки
     * @return текущий вид списка (строковое представление)
     */
    List<String> getForbesList();
}
