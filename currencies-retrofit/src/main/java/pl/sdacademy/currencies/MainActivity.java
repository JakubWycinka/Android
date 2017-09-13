package pl.sdacademy.currencies;

import android.app.DatePickerDialog;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

import pl.sdacademy.currencies.domain.CurrencyRate;
import pl.sdacademy.currencies.domain.CurrencyTable;
import pl.sdacademy.currencies.service.CurrencyProvider;
import pl.sdacademy.currenciesretrofit.R;

public class MainActivity extends AppCompatActivity implements
        SwipeRefreshLayout.OnRefreshListener,
        CurrencyProvider.CurrencyProviderListener,
        DatePickerDialog.OnDateSetListener,
        CurrencyViewHolder.ViewHolderListener {

    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };


    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView tableNumber;
    private Button tableDate;

    private CurrencyAdapter currencyAdapter;

    private SimpleDateFormat dateFormat;
    private String formattedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tableNumber = (TextView) findViewById(R.id.table_number);
        tableDate = (Button) findViewById(R.id.table_date);
        tableDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment datePicker = new DatePickerFragment();
                datePicker.setListener(MainActivity.this);
                datePicker.show(getSupportFragmentManager(), "datePicker");
            }
        });

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_dark, android.R.color.holo_green_dark, android.R.color.holo_orange_dark, android.R.color.holo_red_dark);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        currencyAdapter = new CurrencyAdapter(this);
        recyclerView.setAdapter(currencyAdapter);

        dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        setFormattedToday();
    }

    @Override
    public void onRefresh() {
        new CurrencyProvider(this).execute("A", formattedDate);
    }

    @Override
    public void onStartDataLoading() {
        currencyAdapter.setData(Collections.<CurrencyRate>emptyList());
    }

    @Override
    public void onDataLoaded(CurrencyTable currencyTable) {
        swipeRefreshLayout.setRefreshing(false);

        if (currencyTable == null) {
            Toast.makeText(this, R.string.data_empty, Toast.LENGTH_LONG).show();
            return;
        }

        tableNumber.setText(currencyTable.getNumber());
        currencyAdapter.setData(currencyTable.getCurrencies());
    }

    @Override
    public void onDataLoadFailed() {
        currencyAdapter.setData(Collections.<CurrencyRate>emptyList());
        Toast.makeText(this, R.string.data_error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);
        Date date = calendar.getTime();
        formattedDate = dateFormat.format(date);
        tableDate.setText(formattedDate);
        onRefresh();
    }

    @Override
    public void onCurrencyClicked(String symbol) {
        Toast.makeText(this, symbol, Toast.LENGTH_SHORT).show();
    }

    private void setFormattedToday() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        onDateSet(null, year, month, dayOfMonth);
    }
}
