package com.magung.customer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    EditText etID, etNama, etTelp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);

                // tarik layout
                LayoutInflater inflater = getLayoutInflater();
                view = inflater.inflate(R.layout.form_input_customer_layout, null);


                dialog.setView(view);
                dialog.setCancelable(true);

                // definisi objek
                etID = (EditText) view.findViewById(R.id.et_id);
                etNama = (EditText) view.findViewById(R.id.et_nama);
                etTelp = (EditText) view.findViewById(R.id.et_telp);

                dialog.setPositiveButton("SIMPAN", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final String id, nama, telp;

                        id = etID.getText().toString();
                        nama = etNama.getText().toString();
                        telp = etTelp.getText().toString();

                        // simpan customer
                        simpanCustomer(id, nama, telp);
                    }
                });

                dialog.setNegativeButton("BATAL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                dialog.show();

            }
        });


    }

    private void simpanCustomer(String id, String nama, String telp) {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = "https://project-base-team3.000webhostapp.com/api/customer.php?nama=" + nama + "&telp="+telp+"&id=" + id;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String id, nama, telp;

                if (response.optString("result").equals("true")) {
                    Toast.makeText(getApplicationContext(), "Yeay, data bertambah!", Toast.LENGTH_SHORT).show();

                    // panggil fungsi load pada fragment
                    loadFragment(new FirstFragment());
                } else {
                    Toast.makeText(getApplicationContext(), "O ow, sepertinya harus dicoba lagi!", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Events: ", error.toString());

                Toast.makeText(getApplicationContext(), "Hmm, masalah internet mungkin kuota anda habis atau data yang dimasukkan salah, sepertinya harus dicoba lagi!", Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(jsonObjectRequest);
    }

    private void loadFragment(Fragment fragment) {
        if(fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, fragment).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}