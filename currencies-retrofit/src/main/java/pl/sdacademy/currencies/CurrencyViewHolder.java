package pl.sdacademy.currencies;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import pl.sdacademy.currenciesretrofit.R;

public class CurrencyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private TextView symbolText;
    private TextView nameText;
    private TextView rateText;

    private ViewHolderListener listener;

    public CurrencyViewHolder(View itemView, ViewHolderListener listener) {
        super(itemView);
        symbolText = (TextView) itemView.findViewById(R.id.symbol);
        nameText = (TextView) itemView.findViewById(R.id.currency);
        rateText = (TextView) itemView.findViewById(R.id.mid);
        itemView.setOnClickListener(this);
        this.listener = listener;
    }

    public void setSymbolText(String symbol) {
        symbolText.setText(symbol);
    }

    public void setNameText(String name) {
        nameText.setText(name);
    }

    public void setRateText(String rate) {
        rateText.setText(rate);
    }

    @Override
    public void onClick(View v) {
        String symbol = symbolText.getText().toString();
        listener.onCurrencyClicked(symbol);
    }

    public interface ViewHolderListener {
        void onCurrencyClicked(String symbol);
    }
}
