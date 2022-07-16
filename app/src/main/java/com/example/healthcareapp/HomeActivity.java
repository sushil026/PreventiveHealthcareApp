package com.example.healthcareapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//import okhttp3.Call;
//import okhttp3.Callback;
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.Response;

public class HomeActivity extends AppCompatActivity {
    JsonArrayRequest jsonArrayRequest;
    FirebaseAuth auth;
    EditText sym1, sym2, sym3, sym4, sym5;
    AppCompatButton diagnose;
    ImageView plus, logout;
    TextView result;
    ProgressDialog progressDialog;
    String object;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        sym1 = findViewById(R.id.smp1);
        sym2 = findViewById(R.id.smp2);
        sym3 = findViewById(R.id.smp3);
        sym4 = findViewById(R.id.smp4);
        sym5 = findViewById(R.id.smp5);
        result = findViewById(R.id.res);
        diagnose = findViewById(R.id.diagnose);
        plus = findViewById(R.id.plus);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait");
        progressDialog.setCancelable(false);
        diagnose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                String s1 = sym1.getEditableText().toString();
                String s2 = sym2.getEditableText().toString();
                String s3 = sym3.getEditableText().toString();
                String s4 = sym4.getEditableText().toString();
                String s5 = sym3.getEditableText().toString();
                if (TextUtils.isEmpty(s1) || TextUtils.isEmpty(s2) || TextUtils.isEmpty(s3)){
                    progressDialog.dismiss();
                    Toast.makeText(HomeActivity.this, "Enter at least 3 Symptoms", Toast.LENGTH_SHORT).show();
                }else{
                    fetchData(s1,s2,s3,s4,s5);
                }
            }
        });
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                plus.setVisibility(View.GONE);
                sym5.setVisibility(View.VISIBLE);
                sym4.setVisibility(View.VISIBLE);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                builder.setMessage("Are u sure u want to Logout?")
                .setCancelable(true)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                auth.signOut();
                                finish ();
                                startActivity ( new Intent ( HomeActivity.this, LoginActivity.class ) );
                            }
                        });
            }
        });
    }
    private void fetchData(String s5, String s1, String s2, String s3, String s4){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,
                "https://disease-detection.p.rapidapi.com/get_disease/" +
                        s1 + "," + s2 + "," + s3 + "," + s4 + "," + s5 + ",?rapidapi-key=d8e4e23863msh4e193477072a12ap1579ecjsn5de8dde7475c",
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject obj = response.getJSONObject(i);
                                object = obj.getString("Disease");
                                result.setText(new StringBuilder().append(" ").append(object).toString());
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {}
        });
        mySingleton.getInstance(HomeActivity.this).addToRequestQueue(jsonArrayRequest);
        diagnose.setVisibility(View.GONE);
        result.setVisibility(View.VISIBLE);
    }
//    private void fetchData(String s5, String s1, String s2, String s3, String s4){
//        String url = "https://disease-detection.p.rapidapi.com/get_disease/"+ s1+","+s2+","+s3 +","+s4+","+s5;
//        OkHttpClient client = new OkHttpClient();
//        Request request = new Request.Builder()
//                .url(url)
//                                .get()
//                                .addHeader("X-RapidAPI-Key", "d8e4e23863msh4e193477072a12ap1579ecjsn5de8dde7475c")
//                                .addHeader("X-RapidAPI-Host", "disease-detection.p.rapidapi.com")
//                                .build();
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                Toast.makeText(HomeActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
//            }
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                if (response.isSuccessful()){
//                    String resp = response.body().string();
//                    HomeActivity.this.runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            try {
//                                JSONObject disease = new JSONObject();
//                                JSONArray jsonArray = disease.getJSONArray();
//                                for (int i = 0; i < jsonArray.length(); i++) {
//
//                                }
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    });
//                }
//
//            }
//        });
//    }
}