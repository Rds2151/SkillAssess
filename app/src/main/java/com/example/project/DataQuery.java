package com.example.project;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DataQuery {
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final FirebaseFirestore firestore;
    private final FirebaseDatabase database;
    private final FirebaseAuth mAuth;
    private final StorageReference storageReference;
    public ArrayList<CourseModel> courseModels = new ArrayList<>();

    public DataQuery() {
        this.firestore = FirebaseFirestore.getInstance();
        this.mAuth = FirebaseAuth.getInstance();
        this.database = FirebaseDatabase.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
    }

    protected CompletableFuture<String> updateProfile(Uri imageURI, String Name, String birthDate, String Gender) {
        String uid = mAuth.getCurrentUser().getUid();
        CompletableFuture<String> updateResult = new CompletableFuture<>();
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
                                        firestore.collection("profile").document(uid).set(data)
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
        StorageReference profileImageReference = storageReference.child("Profile_Images/" + uid);

        executorService.execute(() -> {
            firestore.collection("profile").document(uid).get()
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
        executorService.execute(() -> {
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
        });
    }

    protected void getSubjectData(String subjectId,int size,LoadQuestionCallback loadQuestionCallback) {
        ArrayList<QuestionModel> dataList = new ArrayList<>();
        executorService.execute(() -> {
            CollectionReference questionsCollectionRef = firestore.collection("Subjects").document(subjectId).collection("Questions");

            questionsCollectionRef.get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                            List<String> options = (List<String>) doc.get("Options");
                            dataList.add(new QuestionModel(doc.getString("Text"),doc.getString("CorrectAnswer"),options));
                        }

                        Collections.shuffle(dataList);

                        int questionsToFetch = Math.min(size,dataList.size());
                        ArrayList<QuestionModel> randomQuestions = new ArrayList<>(dataList.subList(0,questionsToFetch));
                        if ( randomQuestions.isEmpty()) {
                            loadQuestionCallback.onQuestionLoadedFailed("No Data Found");
                            return;
                        }
                        loadQuestionCallback.onQuestionLoaded(randomQuestions);
                    })
                    .addOnFailureListener(e -> loadQuestionCallback.onQuestionLoadedFailed(e.getMessage()));
        });
    }

    public interface LoadQuestionCallback {
        public void onQuestionLoaded(ArrayList<QuestionModel> questionModels);
        public void onQuestionLoadedFailed(String error);
    }

    protected void submitData(List<Map<String, Object>> dataList,int Total_Correct,String subjectName,SubmitDataCallback submitDataCallback) {
        String uid = mAuth.getCurrentUser().getUid();
        DatabaseReference databaseReference = database.getReference("QuizHistory").child(uid);
        long currentTimeMillis = System.currentTimeMillis();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a", Locale.getDefault());
        String formattedDate = sdf.format(new Date(currentTimeMillis));

        Map<String, Object> quizData = new HashMap<>();
        quizData.put("Total_Correct", Total_Correct);
        quizData.put("Subject_Name", subjectName);
        quizData.put("Submission_Date", formattedDate);

        executorService.execute(() -> {
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    long numberOfQuiz = snapshot.getChildrenCount();
                    DatabaseReference quizReference = databaseReference.child("Quiz"+(++numberOfQuiz));

                    for (int i = 0; i < dataList.size(); i++) {
                        quizData.put("Question" + (i + 1), dataList.get(i));
                    }

                    quizReference.setValue(quizData).addOnCompleteListener(task -> {
                        if(task.isSuccessful()) {
                            updateScore(Total_Correct, new SubmitDataCallback() {
                                @Override
                                public void onSubmitData() {
                                    submitDataCallback.onSubmitData();
                                }

                                @Override
                                public void onSubmitDataFailed(String errror) {
                                    submitDataCallback.onSubmitDataFailed(errror);
                                }
                            });
                        } else {
                            submitDataCallback.onSubmitDataFailed(task.getException().getMessage());
                        }
                    });
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    submitDataCallback.onSubmitDataFailed(error.getMessage());
                }
            });
        });
    }

    public interface SubmitDataCallback {
        public void onSubmitData();
        public void onSubmitDataFailed(String errror);
    }

    private void updateScore(int Score,SubmitDataCallback submitDataCallback) {
        String uid = mAuth.getCurrentUser().getUid();
        executorService.execute(() -> {
            firestore.collection("profile").document(uid).update("Total_Score", FieldValue.increment(Score)).addOnCompleteListener(task -> {
                if(task.isSuccessful()) {
                    submitDataCallback.onSubmitData();
                } else {
                    submitDataCallback.onSubmitDataFailed(task.getException().getMessage());
                }
            });
        });
    }

    protected void fetchData(LoadQuizCallback loadQuizCallback ) {
        String uid = mAuth.getCurrentUser().getUid();
        DatabaseReference databaseReference = database.getReference("QuizHistory").child(uid);
        List<Map<String,Object>> quizModels = new ArrayList<>();

        Query query = databaseReference.limitToFirst(29);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Iterate through each Quiz entry
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        ArrayList<QuestionModel> dataList = new ArrayList<>();
                        Map<String,Object> eachQuiz = new HashMap<>();
                        int size = 0;

                        // Retrieve data from each Quiz
                        eachQuiz.put("Total_Correct",dataSnapshot.child("Total_Correct").getValue(Integer.class));
                        eachQuiz.put("Subject_Name",dataSnapshot.child("Subject_Name").getValue(String.class));
                        eachQuiz.put("Submission_Date",dataSnapshot.child("Submission_Date").getValue(String.class));

                        // Iterate through each Question entry
                        for (DataSnapshot questionSnapshot : dataSnapshot.getChildren()) {
                            if(questionSnapshot.getKey() != null && questionSnapshot.getKey().startsWith("Question")) {
                                QuestionModel questionModel = new QuestionModel();
                                size++;

                                questionModel.setQuestion(questionSnapshot.child("Text").getValue(String.class));
                                questionModel.setCorrectAnswer(questionSnapshot.child("CorrectAnswer").getValue(String.class));
                                questionModel.setSelectedAnswer(questionSnapshot.child("SelectedAnswer").getValue(String.class));

                                List<String> options = new ArrayList<>();
                                for (DataSnapshot optionSnapshot : questionSnapshot.child("Options").getChildren()) {
                                    String option = optionSnapshot.getValue(String.class);
                                    options.add(option);
                                }
                                questionModel.setOptions(options);
                                dataList.add(questionModel);
                            }
                        }
                        eachQuiz.put("Question_Size",size);
                        eachQuiz.put("Questions",dataList);
                        quizModels.add(eachQuiz);
                    }
                    Collections.reverse(quizModels);
                    if (!quizModels.isEmpty()) {
                        loadQuizCallback.onQuizLoaded(quizModels);
                    } else {
                        loadQuizCallback.onQuizLoadedFailed("Zero data");
                    }
                } else {
                    loadQuizCallback.onQuizLoadedFailed("Error: Data not found");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                loadQuizCallback.onQuizLoadedFailed("Error fetching data: "+error.getMessage());
            }
        });
    }

    public interface LoadQuizCallback {
        public void onQuizLoaded(List<Map<String,Object>> result);
        public void onQuizLoadedFailed(String error);
    }

    private String formatDate(Long timestamp) {
        if (timestamp != null) {
            // Convert the timestamp to a Date object
            Date date = new Date(timestamp);

            // Format the Date object to a desired string format
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            return sdf.format(date);
        } else {
            return "N/A"; // Handle null case or return a default value
        }
    }
}
