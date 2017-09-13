package pl.sdacademy.currencies.service;

import java.util.List;

import pl.sdacademy.currencies.domain.CurrencyTable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface NbpApiService {

    @GET("exchangerates/tables/{table}")
    Call<List<CurrencyTable>> getTable(@Path("table") String table, @Query("format") String format);

    @GET("exchangerates/tables/{table}/{date}")
    Call<List<CurrencyTable>> getTableForDate(
            @Path("table") String table,
            @Path("date") String date,
            @Query("format") String format
    );

    @GET("exchangerates/tables/{table}/{startDate}/{endDate}")
    Call<List<CurrencyTable>> getTableForDateRange(
            @Path("table") String table,
            @Path("startDate") String startDate,
            @Path("endDate") String endDate,
            @Query("format") String format
    );

    @GET("exchangerates/rates/{table}/{code}/{startDate}/{endDate}")
    Call<Void> getCurrencyRatesForDateRange(
            @Path("table") String table,
            @Path("code") String code,
            @Path("startDate") String startDate,
            @Path("endDate") String endDate,
            @Query("format") String format
    );

    @GET("exchangerates/rates/{table}/{code}/last/{topCount}")
    Call<Void> getLastCurrencyRates(
            @Path("table") String table,
            @Path("code") String code,
            @Path("topCount") Integer topCount,
            @Query("format") String format
    );

    @GET("cenyzlota/{startDate}/{endDate}")
    Call<Void> getGoldPricesForDateRange(
            @Path("startDate") String startDate,
            @Path("endDate") String endDate,
            @Query("format") String format
    );
}
