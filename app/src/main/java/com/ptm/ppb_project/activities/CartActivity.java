package com.ptm.ppb_project.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.ptm.ppb_project.R;
import com.ptm.ppb_project.adapter.CartAdapter;
import com.ptm.ppb_project.model.CartModel;

public class CartActivity extends AppCompatActivity implements CartAdapter.OnItemClickCallback, View.OnClickListener {

    RecyclerView rvCart;
    FirebaseAuth mAuth;
    FirebaseFirestore firestoreRoot;
    CartAdapter adapter;
    ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        // Hooks
        rvCart = findViewById(R.id.rv_cart);
        ivBack = findViewById(R.id.btn_back_cart);

        // Set Firebase
        mAuth = FirebaseAuth.getInstance();
        firestoreRoot = FirebaseFirestore.getInstance();

        // On Click
        ivBack.setOnClickListener(this);

        setCartAdapter();
    }

    private void setCartAdapter() {
        assert mAuth.getCurrentUser() != null;

        Query query = firestoreRoot.collection("carts/CART_" + mAuth.getCurrentUser().getUid() + "/items");
        FirestoreRecyclerOptions<CartModel> options = new FirestoreRecyclerOptions.Builder<CartModel>()
                .setLifecycleOwner(this)
                .setQuery(query, CartModel.class)
                .build();

        rvCart.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CartAdapter(options, this);
        rvCart.setAdapter(adapter);
    }

    @Override
    public void onItemDeleteFromCart(CartModel dataCart) {
        assert mAuth.getCurrentUser() != null;
        firestoreRoot.document("carts/CART_" + mAuth.getCurrentUser().getUid() + "/items/" + dataCart.getId()).delete()
                .addOnSuccessListener(this, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getBaseContext(), "Delete Success!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getBaseContext(), "Delete Failed!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onClick(View v) {
        int btnId = v.getId();

        if (btnId == R.id.btn_back_cart) {
            startActivity(new Intent(this, DashboardActivity.class));
            finish();
        }
    }

}