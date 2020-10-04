package com.magung.customer;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FirstFragment extends Fragment {

    View fragment_view;
    ArrayList<Customer> customers;
    ProgressBar pb;
    SwipeRefreshLayout srl;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_first, container, false);

        fragment_view = rootView;
        pb = (ProgressBar) rootView.findViewById(R.id.progress_horizontal);
        // lookup the swipe container
        srl = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);


        // Setup refresh listener which trigger new data loading
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                load();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        srl.setRefreshing(false);
                    }
                }, 1000);
            }
        });

        // configure the refreshing color
        srl.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);

        load();

        return rootView;
    }

    public void load() {
        pb.setVisibility(ProgressBar.VISIBLE);

        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.getCache().clear();
        String url = "https://project-base-team3.000webhostapp.com/api/customer.php?search";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String id, nama, telp;
                        customers = new ArrayList<>();

                        try {
                            JSONArray jsonArray = response.getJSONArray("result");
                            customers.clear();
                            if (jsonArray.length() != 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject data = jsonArray.getJSONObject(i);
                                    id = data.getString("idcustomer").toString().trim();
                                    nama = data.getString("namacustomer").toString().trim();
                                    telp = data.getString("telpcustomer").toString().trim();

                                    customers.add(new Customer(id, nama, telp));
                                }
                                showRecyclerGrid();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        pb.setVisibility(ProgressBar.GONE);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Events: ", error.toString());

                pb.setVisibility(ProgressBar.GONE);
                Toast.makeText(getContext(), "Please check your connection", Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(jsonObjectRequest);
    }

    private void showRecyclerGrid(){
        RecyclerView recyclerView = (RecyclerView) fragment_view.findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        CustomerAdapter mAdapter = new CustomerAdapter(getContext(), customers);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

}