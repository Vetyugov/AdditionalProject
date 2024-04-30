package ru.netology.vetyugov.mipt.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.netology.vetyugov.mipt.service.ForbesService;

import java.util.List;

@RestController
@RequestMapping("/forbes")
@RequiredArgsConstructor
public class ForbesController {
    private final ForbesService forbesServiceImpl;

    @GetMapping("/getTheYoungest/{country}")
    public String getTheYoungest(@PathVariable String country){
        return forbesServiceImpl.getTheYoungest(country);
    }

    @GetMapping("/getCompanyNamesByCountryAndIndustry/{country}&{industry}")
    public List<String> getCompanyNamesByCountryAndIndustry(@PathVariable String country, @PathVariable String industry){
        return forbesServiceImpl.getCompanyNamesByCountryAndIndustry(country, industry);
    }

    @GetMapping("/getCompanyByName/{name}")
    public String getCompanyByName(@PathVariable String name){
        return forbesServiceImpl.getCompanyByName(name);
    }


    @PutMapping("/updateNetWorthByName/{name}&{newNetWorth}")
    public void updateNetWorthByName(@PathVariable String name,@PathVariable float newNetWorth){
        forbesServiceImpl.updateNetWorthByName(name, newNetWorth);
    }

    @DeleteMapping("/deleteByName/{name}")
    public void deleteByName(@PathVariable String name){
        forbesServiceImpl.deleteByName(name);
    }

    /**
     * Для отладки
     * @return текущий вид списка (строковое представление)
     */
    @GetMapping
    public List<String> getForbesList(){
        return forbesServiceImpl.getForbesList();
    }
}
