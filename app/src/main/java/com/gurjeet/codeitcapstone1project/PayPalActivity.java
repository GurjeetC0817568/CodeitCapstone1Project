package com.gurjeet.codeitcapstone1project;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;
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
    String paypalclientid="AZqeLqaD_RSBujv4VTo51SnZcRVcRCe4WbsQDddwy_a-ifdH5ZJVwqOrjG5g2TxqWWEz_e6LkoHz5F_o";//""AUyZ7wL252EkWECf6GEQ5cVvpYgyRmLU-tMugM_FDvM0zO3Yw7cmlpgTxTutYF_VCPybiihjSGLKzZSu";
    Intent mservices;
    int mpaypalrequestcode=999;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private StorageReference storageReference;

    AutoCompleteTextView txtName,txtPrice,txtDetails,txtEmail;

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
        txtPrice=(AutoCompleteTextView) findViewById(R.id.txtPrice);
        txtDetails=(AutoCompleteTextView) findViewById(R.id.txtDetails);

        Intent i = getIntent();
        String price = i.getExtras().getString("price");
        String pname =i.getExtras().getString("pname");
        String pdetails =i.getExtras().getString("pdetails");

        txtName.setText(pname);
        txtPrice.setText("Price: $"+price);
        txtDetails.setText(pdetails);
        // txtEmail.setText("");
    }

    public void pay(View view)
    {
        Intent i = getIntent();
        String price =i.getExtras().getString("price");

        PayPalPayment payment=new PayPalPayment(new BigDecimal(price),"USD","Test Payment with Paypal",PayPalPayment.PAYMENT_INTENT_SALE);

        Intent intent=new Intent(this,PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,payPalConfiguration);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT,payment);
        startActivityForResult(intent,mpaypalrequestcode);
       // paymentIntentLauncher.launch(intent);  //Working code - if enable this then uncomment below related functions
    }


   /* // This code is also working if uncomment line number 89 but lastly used line no. 88 only
   ActivityResultLauncher<Intent> paymentIntentLauncher = registerForActivityResult(
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
                Toast.makeText(this,"Payment Approved...",Toast.LENGTH_SHORT).show();

                if(confirmation!=null)
                {
                    String state=confirmation.getProofOfPayment().getState();

                    if(state.equals("approved"))
                    {
                        Toast.makeText(this,"Payment Done",Toast.LENGTH_LONG).show();

                        Intent i = getIntent();
                        String productId =i.getExtras().getString("pid");//remove this id from listing now because its sold
                        UpdateSoldProduct(productId);
                        //after payment done, goback to main activity
                        Intent in = new Intent(PayPalActivity.this, MainActivity.class);
                        in.putExtra("productIdSold",productId);//send product id back to make them sold and not show in list
                        PayPalActivity.this.startActivity(in);


                    }else
                    {
                        Toast.makeText(this,"Error in the payment",Toast.LENGTH_SHORT).show();
                    }
                }else
                    Toast.makeText(this,"Confirmation is null",Toast.LENGTH_SHORT).show();

            }else{Toast.makeText(this,"Payment Not OK",Toast.LENGTH_SHORT).show();}
        }
    }


    // if buyer paid for product then making it "sold" and not listing anymore
    public void UpdateSoldProduct(String productId){
               db.collection("data")
                  .whereEqualTo("pid", productId)
                  .get()
                  .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                      @Override
                      public void onComplete(@NonNull Task<QuerySnapshot> task) {
                          if (task.isSuccessful()) {
                              for (QueryDocumentSnapshot document : task.getResult()) {
                                 // Log.d("GK1- DocumentID", document.getId() + " => " + document.getData());
                                  DocumentReference contact = db.collection("data").document(document.getId());
                                  contact.update("paymentdone", "done")
                                          .addOnSuccessListener(new OnSuccessListener< Void >() {
                                              @Override
                                              public void onSuccess(Void aVoid) {
                                                  // Toast.makeText(this, "Updated Successfully",Toast.LENGTH_SHORT).show();
                                              }
                                          });
                              }
                          } else {
                             // Log.d("GK2", "Error getting documents: ", task.getException());
                          }
                      }
                  });
    }


}
