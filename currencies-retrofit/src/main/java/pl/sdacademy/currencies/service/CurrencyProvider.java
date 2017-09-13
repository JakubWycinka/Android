package pl.sdacademy.currencies.service;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.util.List;

import pl.sdacademy.currencies.domain.CurrencyTable;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CurrencyProvider extends AsyncTask<String, Void, CurrencyTable> {

    private final static String NBP_API_URL = "http://api.nbp.pl/api/";
    private final static String JSON_FORMAT_ARGUMENT = "json";

    private CurrencyProviderListener listener;
    private NbpApiService nbpApiService;

    private Exception exception;

    public CurrencyProvider(CurrencyProviderListener listener) {
        this.listener = listener;
        Retrofit retrofit = new Retrofit.Builder().baseUrl(NBP_API_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        nbpApiService = retrofit.create(NbpApiService.class);
    }

    @Override
    protected void onPreExecute() {
        listener.onStartDataLoading();
    }

    @Override
    protected CurrencyTable doInBackground(String... params) {
        try {
            return getData(params[0], params[1]);
        } catch (RuntimeException | IOException e) {
            Log.e(CurrencyProvider.class.getName(), e.getMessage());
            exception = e;
        }

        return null;
    }

    @Override
    protected void onPostExecute(CurrencyTable result) {
        if (exception != null) {
            listener.onDataLoadFailed();
            return;
        }

        listener.onDataLoaded(result);
    }

    private CurrencyTable getData(String table, String formattedDate) throws IOException {
        Call<List<CurrencyTable>> tablesCall = nbpApiService.getTableForDate(table, formattedDate, JSON_FORMAT_ARGUMENT);
        Response<List<CurrencyTable>> tablesResponse = tablesCall.execute();
        List<CurrencyTable> tables = tablesResponse.body();

        if (tablesResponse.isSuccessful() && tables != null && !tables.isEmpty()) {
            return tables.get(0);
        }

        int httpStatusCode = tablesResponse.code();

        // 400 - Bad Request, 404 - Not Found
        if (httpStatusCode == 400 || httpStatusCode == 404) {
            return null;
        }

        throw new RuntimeException("Response error received");
    }

    public interface CurrencyProviderListener {
        void onStartDataLoading();
        void onDataLoaded(CurrencyTable currencyTable);
        void onDataLoadFailed();
    }
}
