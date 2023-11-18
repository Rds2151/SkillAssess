package com.example.project;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DataQuery {
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private FirebaseFirestore firestore;
    private FirebaseAuth mAuth;
    private StorageReference storageReference;
    public ArrayList<CourseModel> courseModels = new ArrayList<>();

    public DataQuery() {
        this.firestore = FirebaseFirestore.getInstance();
        this.mAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
    }

    protected CompletableFuture<String> updateProfile(Uri imageURI, String Name, String birthDate, String Gender) {
        String uid = mAuth.getCurrentUser().getUid();
        CompletableFuture<String> updateResult = new CompletableFuture<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> data = new HashMap<>();

        // Create a reference to the profile image in Firebase Storage
        StorageReference profileImage = storageReference.child("Profile_Images/" + uid);

        // Upload the image to Firebase Storage
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                profileImage.putFile(imageURI)
                        .addOnSuccessListener(taskSnapshot -> {
                            taskSnapshot.getMetadata().getReference().getDownloadUrl()
                                    .addOnSuccessListener(uri -> {
                                        data.put("Profile_Image", uri);
                                        data.put("Full_name", Name);
                                        data.put("Birthdate", birthDate);
                                        data.put("Gender", Gender);
                                        data.put("Total_Score", MainActivity.profileDetail.Total_Score);

                                        // Update the profile data in Firestore
                                        db.collection("profile").document(uid).set(data)
                                                .addOnSuccessListener(aVoid -> {
                                                    MainActivity.profileDetail.updateDetail(data);
                                                    updateResult.complete("Profile updated successfully");
                                                })
                                                .addOnFailureListener(e -> {
                                                    String errorMessage = "Failed to update profile: " + e.getMessage();
                                                    updateResult.complete(errorMessage);
                                                });
                                    });
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                updateResult.complete("Failed to upload profile image.");
                            }
                        });
            }
        });
        return updateResult;
    }

    protected void fetchDetails(com.example.project.DataQuery.DataCallback callback) {
        String uid = mAuth.getCurrentUser().getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        StorageReference profileImageReference = storageReference.child("Profile_Images/" + uid);

        executorService.execute(() -> {
            db.collection("profile").document(uid).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        Map<String, Object> data = new HashMap<>();
                        if (documentSnapshot.exists()) {
                            data.put("Full_name", documentSnapshot.getString("Full_name"));
                            data.put("Birthdate", documentSnapshot.getString("Birthdate"));
                            data.put("Gender", documentSnapshot.getString("Gender"));
                            data.put("Total_Score", documentSnapshot.get("Total_Score"));
                            profileImageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                                data.put("Profile_Image", uri);
                                callback.onDataReceived(data);
                            });
                        } else {
                            data.put("Error", "Data Not found");
                            callback.onDataReceived(data);
                        }
                    })
                    .addOnFailureListener(e -> {
                        Map<String, Object> data = new HashMap<>();
                        data.put("Error", "Data Not found");
                        callback.onDataReceived(data);
                    });
        });
    }

    public interface DataCallback {
        void onDataReceived(Map<String, Object> data);
    }

    protected void loadCategories(LoadCategoriesCallback loadCategoriesCallback) {
        executorService.execute(() -> {
            this.courseModels.clear();
            firestore.collection("Courses").get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                            if (doc.getBoolean("Display"))
                            {
                                this.courseModels.add(new CourseModel(doc.getId(), doc.getString("Course_Name"),doc.getString("Course_Image")));
                            }
                        }
                        loadCategoriesCallback.onCategoriesLoaded(courseModels);
                    });
        });
    }

    public interface LoadCategoriesCallback {
        public void onCategoriesLoaded(ArrayList<CourseModel> courseModels);
    }

    protected void loadSubjects(String Course, LoadCategoriesCallback loadCategoriesCallback) {
        DocumentReference subjDocument = firestore.collection("Courses").document(Course);
        this.courseModels.clear();
        subjDocument.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists())
                    {
                        int subjectNo = documentSnapshot.getLong("Total_Subject").intValue();
                        List<Task<DocumentSnapshot>> tasks = new ArrayList<>();

                        for (int itr = 1;itr <= subjectNo;itr++) {
                            DocumentReference subjectReference = documentSnapshot.getDocumentReference("Subj" + itr);
                            if (subjectReference != null) {
                                tasks.add(subjectReference.get());
                            }
                        }

                        Task<List<DocumentSnapshot>> combinedTask = Tasks.whenAllSuccess(tasks);

                        combinedTask.addOnSuccessListener(documentSnapshots -> {
                            for (DocumentSnapshot doc : documentSnapshots) {
                                if (doc.exists()) {
                                    if (doc.getBoolean("Display")) {
                                        this.courseModels.add(new CourseModel(doc.getId(), doc.getString("Subject_Name"), doc.getString("Subject_Image")));
                                    }
                                }
                            }
                            loadCategoriesCallback.onCategoriesLoaded(courseModels);
                        });
                    }
                });
    }

}
