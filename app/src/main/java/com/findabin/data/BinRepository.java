package com.findabin.data;

import android.telecom.Call;
import android.util.Log;

import com.findabin.model.*;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;


public class BinRepository {
    public interface Callback<T>{
        void onSuccess(T result);
        void onError(Exception e);
    }
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String BINS = "bins";
    public void getAllBins(Callback<List<Bin>> callback){
        db.collection(BINS)
                .get()
                .addOnSuccessListener(snapshot ->{
                        List<Bin> bins = snapshot.toObjects(Bin.class);
                        callback.onSuccess(bins);})
                .addOnFailureListener(e ->{
                        Log.e("BinRepository", "getAllBins failed", e);
                        callback.onError(e);
                });

    }

    public void getBin(String binId, Callback<Bin> callback){
        db.collection(BINS)
                .document(binId)
                .get()
                .addOnSuccessListener(documentSnapshot ->{
                    if(documentSnapshot.exists()){
                        Bin bin = documentSnapshot.toObject(Bin.class);
                        callback.onSuccess(bin);
                    } else{
                        callback.onError(new Exception("Bin not found"));

                    }
                })
                .addOnFailureListener(e ->{
                    Log.e("BinRepository", "getBin failed", e);
                    callback.onError(e);
                });
    }

    public void addBin(Bin bin, Callback<Void> callback){
        db.collection(BINS).add(bin)
                .addOnSuccessListener(docRef -> callback.onSuccess(null))
                .addOnFailureListener(e ->{
                    Log.e("BinRepository", "add bin failed",e);
                    callback.onError(e);
                });

    }
}
