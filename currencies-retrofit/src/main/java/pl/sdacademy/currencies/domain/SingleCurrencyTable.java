package pl.sdacademy.currencies.domain;

import java.util.List;
import java.util.Random;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SingleCurrencyTable {



    private String table;


    private String currency;

    private String code;

    private List<SingleCurrencyRate> rates;

}
