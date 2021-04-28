package com.example.ufoodlibrary.firebase;

import android.app.Activity;
import android.graphics.Bitmap;

import com.example.ufoodlibrary.objects.G3OrderObj;
import com.example.ufoodlibrary.objects.G3RestaurantObj;
import com.example.ufoodlibrary.objects.G3ReviewObj;
import com.example.ufoodlibrary.objects.G3UserObj;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.UploadTask;


import java.io.File;

/**
 * Created by Alfonso on 21-May-16.
 */
public interface FirebaseInterface {

    void setAccountId(String accID);

    String getCurrentId();

    boolean isSignedin();

    void saveUserProfile(G3UserObj user, OnCompleteListener<Void> listener) throws NotAuthenticatedException,NetworkDownException;

    void saveRestaurantProfile(G3RestaurantObj restaurantObj, OnCompleteListener<Void> listener) throws NotAuthenticatedException,NetworkDownException;

    void signIn( String email, String password, OnCompleteListener<AuthResult> listener) throws NetworkDownException;

    void signUp( String email, String password, OnCompleteListener<AuthResult> listener) throws NetworkDownException;

    void deleteAccount(OnCompleteListener<Void> listener) throws NetworkDownException,NotAuthenticatedException;

    void signOut(FirebaseAuth.AuthStateListener listener) throws NotAuthenticatedException,NetworkDownException;

    void getUserProfile(ValueEventListener listener) throws NotAuthenticatedException, NetworkDownException;

    void getUserProfileWithKey(String key,ValueEventListener listener) throws NetworkDownException;

    void getRestaurantProfile(ValueEventListener listener) throws NotAuthenticatedException, NetworkDownException;

    void getRestaurantProfileWithKey(String key,ValueEventListener listener) throws NotAuthenticatedException, NetworkDownException;

    void isRestaurant(ValueEventListener listener) throws  NotAuthenticatedException,NetworkDownException;

    void getRestaurants(ValueEventListener listenr) throws NetworkDownException;

    void saveImageToFirebaseStorage(String filename, Bitmap bitmap, OnFailureListener faillistener, OnSuccessListener<UploadTask.TaskSnapshot> successListener) throws NetworkDownException;

    void getImageFormFirebaseStorage(String url,File file, OnSuccessListener<FileDownloadTask.TaskSnapshot> listener) throws NetworkDownException;

    void getRestaurantOrders(ValueEventListener listener) throws NotAuthenticatedException, NetworkDownException;

    void getNewOrders(long timestamp, ValueEventListener listener) throws NotAuthenticatedException, NetworkDownException;

    void getRestaurantReviews(ValueEventListener listener) throws NotAuthenticatedException, NetworkDownException;

    void getRestaurantReviewsbyKeyRestaurant(String restaurantId, ValueEventListener listener) throws NetworkDownException;

    void getUserOrders(ValueEventListener listener) throws  NotAuthenticatedException, NetworkDownException;

    void getFavouriteRestaurant(ValueEventListener listener) throws NotAuthenticatedException, NetworkDownException;

    void isFavourite (String restaurantId, ValueEventListener listener) throws NotAuthenticatedException,NetworkDownException;

    void removeFavourite (String restaurantId) throws NotAuthenticatedException, NetworkDownException;

    void getFavouriteUsers(ValueEventListener listener) throws NotAuthenticatedException, NetworkDownException;

    void getOrderWithKey(String orderid,ValueEventListener listenr) throws  NetworkDownException;

    void addOrder(G3OrderObj order, OnCompleteListener<Void> listener) throws NotAuthenticatedException, NetworkDownException;

    void addReview(G3ReviewObj review, G3OrderObj order, OnCompleteListener<Void> listener) throws NotAuthenticatedException, NetworkDownException;

    void addFavouriteRestaurant(String restaurantId) throws NotAuthenticatedException, NetworkDownException;

    void setOrderViewed(String orderid) throws NetworkDownException;

    void setOrderEvaded(String orderid) throws NetworkDownException;

    void setOrderDeleted(String orderid) throws NetworkDownException;

    void setUserOrderVisualized(String orderid) throws NetworkDownException;

    void updateRestaurantProfile(G3RestaurantObj restaurantObj, OnCompleteListener<Void> listener) throws NetworkDownException;

    void addReplyToReview(String reviewId, String replyText, OnCompleteListener<Void> listener) throws NetworkDownException;

    void recoveryPasswordByEmail(String email, OnCompleteListener<Void> listener) throws NetworkDownException;

    void addNotifyDaily(String name) throws NetworkDownException;

    void getNotifyDaily(ValueEventListener listener) throws NotAuthenticatedException, NetworkDownException;

    void setNotifyDaily(String restaurantName, boolean value) throws NetworkDownException, NotAuthenticatedException;
}
