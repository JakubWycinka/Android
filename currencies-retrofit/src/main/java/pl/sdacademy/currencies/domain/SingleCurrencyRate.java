package pl.sdacademy.currencies.domain;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SingleCurrencyRate {

    @SerializedName("no")
    private String number;

    private String effectiveDate;

    private Double mid;
}
