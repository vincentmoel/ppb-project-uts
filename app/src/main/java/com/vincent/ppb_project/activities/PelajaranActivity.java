package com.vincent.ppb_project.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Adapter;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;
import com.vincent.ppb_project.R;
import com.vincent.ppb_project.adapter.PelajaranAdapter;
import com.vincent.ppb_project.model.CartModel;
import com.vincent.ppb_project.model.PelajaranModel;
import com.vincent.ppb_project.session.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;

public class PelajaranActivity extends AppCompatActivity implements PelajaranAdapter.OnItemClickCallback {

    RecyclerView rv;
    FirebaseFirestore firestoreRoot;
    FirebaseAuth mAuth;
    SessionManager loginSession;
    PelajaranAdapter adapter;
    String uid;
    GetAdapter getAdapterListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pelajaran);

        // Hooks
        rv = findViewById(R.id.rv_pelajaran);

        // Set Firebase
        firestoreRoot = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        assert  mAuth.getCurrentUser() != null;
        uid = mAuth.getCurrentUser().getUid();

        // Set Session
        loginSession = new SessionManager(this, SessionManager.LOGIN_SESSION);

        String fromWhere = getIntent().getStringExtra("from");
        if (fromWhere.equals("Matematika")) {
            setPelajaranAdapter(fromWhere);
        }
        else if (fromWhere.equals("Fisika")) {
            setPelajaranAdapter(fromWhere);
        }
        else if (fromWhere.equals("Biologi")) {
            setPelajaranAdapter(fromWhere);
        }
        else if (fromWhere.equals("Kimia")){
            setPelajaranAdapter(fromWhere);
        }
    }

    private void setPelajaranAdapter(String matpel) {

        String kelas = loginSession.getLoginSessionData().getKelas();

        Query query = firestoreRoot.collection("pelajaran")
                .whereEqualTo("matpel", matpel)
                .whereEqualTo("kelas", kelas);

        FirestoreRecyclerOptions<PelajaranModel> options = new FirestoreRecyclerOptions.Builder<PelajaranModel>()
                .setQuery(query, PelajaranModel.class)
                .build();

        rv.setLayoutManager(new LinearLayoutManager(getBaseContext()));

        // CEK CART
        firestoreRoot.collection("carts/CART_" + uid + "/items")
                .addSnapshotListener(this, new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            return;
                        }

                        if (value != null && !value.isEmpty()) {
                            ArrayList<CartModel> dataCart = new ArrayList<>();
                            for (DocumentSnapshot ds : value) {
                                dataCart.add(ds.toObject(CartModel.class));
                            }

                            adapter = new PelajaranAdapter(options, PelajaranActivity.this, dataCart);
                            rv.setAdapter(adapter);

                            getAdapterListener.getAdapter(adapter);
                        }

                        else {
                            Toast.makeText(getBaseContext(), "NULL", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }


    @Override
    protected void onStart() {
        super.onStart();
        setGetAdapter(new GetAdapter() {
            @Override
            public void getAdapter(PelajaranAdapter adapter) {
                adapter.startListening();

            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        setGetAdapter(new GetAdapter() {
            @Override
            public void getAdapter(PelajaranAdapter adapter) {
                adapter.stopListening();
            }
        });
    }

    private void removeKuota(String idPelajaran) {

        firestoreRoot.runTransaction(new Transaction.Function<Void>() {
            @Nullable
            @Override
            public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                DocumentReference docRef = firestoreRoot.document("pelajaran/" + idPelajaran);
                DocumentSnapshot dataSnapshot = transaction.get(docRef);
                // Logic
                long kuota = dataSnapshot.getLong("kuota");
                if (kuota > 0) {
                    long sisaKuota = kuota - 1;
                    transaction.update(docRef, "kuota", sisaKuota);
                }
                // End Logic
                return null;
            }
        })
        .addOnSuccessListener(this, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getBaseContext(), "Add Success!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addKuota(String idPelajaran) {

        firestoreRoot.runTransaction(new Transaction.Function<Void>() {
            @Nullable
            @Override
            public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                DocumentReference docRef = firestoreRoot.document("pelajaran/" + idPelajaran);
                DocumentSnapshot dataSnapshot = transaction.get(docRef);
                // Logic
                long kuota = dataSnapshot.getLong("kuota");
                long sisaKuota = kuota + 1;
                // End Logic
                transaction.update(docRef, "kuota", sisaKuota);
                return null;
            }
        })
                .addOnSuccessListener(this, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getBaseContext(), "Remove Success!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onItemAddToCart(PelajaranModel dataPelajaran) {
        CartModel dataCart = new CartModel(dataPelajaran.getHari(), dataPelajaran.getStart_at(), dataPelajaran.getFinish_at());
        firestoreRoot.document("carts/CART_" + uid + "/items/" + dataPelajaran.getId()).set(dataCart)
                .addOnSuccessListener(this, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        removeKuota(dataPelajaran.getId());
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getBaseContext(), "Add Failed!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onItemDeleteFromCart(PelajaranModel dataPelajaran) {
        firestoreRoot.document("carts/CART_" + uid + "/items/" + dataPelajaran.getId()).delete()
                .addOnSuccessListener(this, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        addKuota(dataPelajaran.getId());
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getBaseContext(), "Delete Failed!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public interface GetAdapter {
        void getAdapter(PelajaranAdapter adapter);
    }

    private void setGetAdapter(GetAdapter getAdapterListener) {
        this.getAdapterListener = getAdapterListener;
    }
}