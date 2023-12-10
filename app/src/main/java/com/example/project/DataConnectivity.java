package com.example.project;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DataConnectivity {

    protected String email_addr;
    private String passwd, cpasswd;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private FirebaseAuth mAuth;

    public DataConnectivity(String email_addr) {
        this.email_addr = email_addr;
        this.mAuth = FirebaseAuth.getInstance();
    }

    public DataConnectivity(String email_addr, String passwd) {
        this.email_addr = email_addr;
        this.passwd = passwd;
        this.mAuth = FirebaseAuth.getInstance();
    }
    public DataConnectivity(String email_addr, String passwd, String cpasswd) {
        this(email_addr,passwd);
        this.cpasswd = cpasswd;
    }

    protected CompletableFuture<String> loginUser(Context context) {
        if (email_addr.isEmpty() || passwd.isEmpty()) {
            return CompletableFuture.completedFuture("Please fill in both email and password fields.");
        }

        CompletableFuture<String> result = new CompletableFuture<>();

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                mAuth.signInWithEmailAndPassword(email_addr, passwd).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Check if the user is authenticated and email is verified
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if (user != null) {
                            if (user.isEmailVerified()) {
                                String email = user.getEmail();
                                SharedPreferences sharedPreferences = context.getSharedPreferences("UserData", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("email", email);
                                editor.apply();
                                result.complete("Login successful");
                            } else {
                                result.complete("Please verify your email address before logging in.");
                            }
                        }
                        else {
                            result.complete("User is not authenticated. Please log in again.");
                        }
                    } else {
                        String errorMessage = task.getException() != null ? task.getException().getMessage() : "Login failed.";
                        result.complete(errorMessage);
                    }
                });
            }
        });
        return result;
    }

    protected CompletableFuture<String> signUpUser() {
        if (email_addr.isEmpty() || passwd.isEmpty() || cpasswd.isEmpty()) {
            return CompletableFuture.completedFuture("Please fill in both email and password fields.");
        } else if (!passwd.equals(cpasswd)) {
            return CompletableFuture.completedFuture("Passwords do not match. Please check your password.");
        }

        CompletableFuture<String> signUpResult = new CompletableFuture<>();

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                mAuth.createUserWithEmailAndPassword(email_addr, passwd)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                // Send email verification
                                FirebaseUser user = mAuth.getCurrentUser();
                                if (user != null) {
                                    user.sendEmailVerification()
                                            .addOnSuccessListener(aVoid -> {
                                                // Email verification sent successfully
                                                signUpResult.complete("Registration successful. Verification email sent.");
                                            })
                                            .addOnFailureListener(e -> {
                                                // Failed to send verification email
                                                signUpResult.complete("Registration successful, but failed to send verification email.");
                                            });
                                }
                            } else {
                                // Registration failed
                                signUpResult.complete(task.getException().getMessage());
                            }
                        });
            }
        });

        return signUpResult;
    }

    protected CompletableFuture<String> resetPassword() {
        if (email_addr.isEmpty()) {
            return CompletableFuture.completedFuture("Please fill email address fields.");
        }

        CompletableFuture<String> forgetResult = new CompletableFuture<>();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                mAuth.sendPasswordResetEmail(email_addr).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        forgetResult.complete("Password reset email sent successfully. Please check your email inbox.");
                    } else {
                        forgetResult.complete(task.getException().getMessage());
                    }
                });
            }
        });

        return forgetResult;
    }

    protected CompletableFuture<String> changePassword() {
        if (passwd.isEmpty() || cpasswd.isEmpty()) {
            return CompletableFuture.completedFuture("Please fill in both old password and new password fields");
        }

        CompletableFuture<String> result = new CompletableFuture<>();
        executorService.execute(() -> mAuth.confirmPasswordReset(passwd, cpasswd).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                result.complete("Password changed successfully!");
            } else {
                Exception exception = task.getException();
                if (exception instanceof FirebaseAuthInvalidUserException) {
                    result.complete("Invalid user or user not found");
                } else if (exception instanceof FirebaseAuthInvalidCredentialsException) {
                    result.complete("Invalid credentials or code");
                } else {
                    result.complete(exception.getMessage());
                }
            }
        }).addOnFailureListener(e -> {
            // Handle additional failure scenarios if needed
            result.complete("Failed to change password: " + e.getMessage());
        }));


        return result;
    }



}