package com.example.card_io;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;

public class MainActivity extends AppCompatActivity {
    /**
     * 카드번호, 만료기간만 스캔됨
     */
    
    final int MY_SCAN_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void cardScanBtn(View view) {
        Intent scanIntent = new Intent(this, CardIOActivity.class);

        // customize these values to suit your needs.
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, true); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, false); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE, false); // default: false

        // MY_SCAN_REQUEST_CODE is arbitrary and is only used within this activity.
        startActivityForResult(scanIntent, MY_SCAN_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MY_SCAN_REQUEST_CODE) {
            String resultDisplayStr;
            if (data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
                CreditCard scanResult = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);

                // Never log a raw card number. Avoid displaying it, but if necessary use getFormattedCardNumber()
                String resultDisplayStr1 = "Card Number: " + scanResult.getRedactedCardNumber() + "\n";
                Log.e("card번호",resultDisplayStr1);

                // Do something with the raw number, e.g.:
                // myService.setCardNumber( scanResult.cardNumber );

                if (scanResult.isExpiryValid()) {
                    String resultDisplayStr2 = "Expiration Date: " + scanResult.expiryMonth + "/" + scanResult.expiryYear + "\n";
                    Log.e("card만료기간",resultDisplayStr2);
                }

                if (scanResult.cvv != null) {
                    // Never log or display a CVV
                    String resultDisplayStr3 = "CVV has " + scanResult.cvv.length() + " digits.\n";
                    Log.e("cardCVV",resultDisplayStr3);
                }

                if (scanResult.postalCode != null) {
                    String resultDisplayStr4 = "Postal Code: " + scanResult.postalCode + "\n";
                    Log.e("cardPostal",resultDisplayStr4);
                }
            }
            else {
                resultDisplayStr = "Scan was canceled.";
            }
            // do something with resultDisplayStr, maybe display it in a textView
            // resultTextView.setText(resultDisplayStr);
        }
        // else handle other activity results
    }
}