package com.gurjeet.codeitcapstone1project;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;

public class PayPalActivity extends AppCompatActivity {
//https://www.appinvoice.com/en/s/documentation/how-to-get-paypal-client-id-and-secret-key-22
    PayPalConfiguration payPalConfiguration;
    String paypalclientid="AUyZ7wL252EkWECf6GEQ5cVvpYgyRmLU-tMugM_FDvM0zO3Yw7cmlpgTxTutYF_VCPybiihjSGLKzZSu";
    Intent mservices;
    int mpaypalrequestcode=999;

    AutoCompleteTextView txtName,txtAddress,txtPhone,txtEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_pal);

        payPalConfiguration=new PayPalConfiguration().environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
                .clientId(paypalclientid);

        mservices=new Intent(this, PayPalService.class);
        mservices.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,payPalConfiguration);
        startService(mservices);



        txtName=(AutoCompleteTextView) findViewById(R.id.txtName);
        txtAddress=(AutoCompleteTextView) findViewById(R.id.txtAddress);
        txtPhone=(AutoCompleteTextView) findViewById(R.id.txtPhone);
        txtEmail=(AutoCompleteTextView) findViewById(R.id.txtEmail);
    }

    void pay(View view)
    {
       // Intent i = getIntent();
       // String price =i.getExtras().getString("price");

        PayPalPayment payment=new PayPalPayment(new BigDecimal(10),"CAD","Test Payment with Paypal",PayPalPayment.PAYMENT_INTENT_SALE);

        Intent intent=new Intent(this,PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,payPalConfiguration);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT,payment);
        startActivityForResult(intent,mpaypalrequestcode);//fixme:runtime error paypal connection
       // paymentIntentLauncher.launch(intent);  //if enable this then uncomment below related functions


        //Intent intent = new Intent(this, PayPalService.class);
       // intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        //startService(intent);

        txtName.setText("");
        txtAddress.setText("");
        txtPhone.setText("");
        txtEmail.setText("");

    }


   /* ActivityResultLauncher<Intent> paymentIntentLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Log.e("PayPalActivity", "onActivityResult: success");
                    assert result.getData() != null;
                    processPayment(result.getData());
                } else {
                    Log.e("PayPalActivity", "onActivityResult: failed");
                }
            }
    );
    private void processPayment(Intent data) {
        PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
        if (confirmation != null){
            try {
                String paymentDetails = confirmation.toJSONObject().toString(4);
                JSONObject payObj = new JSONObject(paymentDetails);
                String payID = payObj.getJSONObject("response").getString("id");
                String state = payObj.getJSONObject("response").getString("state");
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("PayPalPaymentError", "an extremely unlikely failure occurred: ", e);
            }
        }
    }
*/

    protected void onActivityResult(int requestCode,int resultCode, Intent data)
    {
        super.onActivityResult(requestCode,resultCode,data);

        if(requestCode==mpaypalrequestcode)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                PaymentConfirmation confirmation=data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                Toast.makeText(this,"Payment Approved",Toast.LENGTH_SHORT).show();

                if(confirmation!=null)
                {
                    String state=confirmation.getProofOfPayment().getState();

                    if(state.equals("approved"))
                    {
                        Toast.makeText(this,"Payment Approved",Toast.LENGTH_SHORT).show();
                    }else
                    {
                        Toast.makeText(this,"Error in the payment",Toast.LENGTH_SHORT).show();
                    }
                }else
                    Toast.makeText(this,"Confirmation is null",Toast.LENGTH_SHORT).show();

            }
        }
    }
}
