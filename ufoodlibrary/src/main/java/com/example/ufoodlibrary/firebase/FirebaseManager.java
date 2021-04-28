package com.example.ufoodlibrary.firebase;

import android.graphics.Bitmap;

import com.example.ufoodlibrary.objects.G3OrderObj;
import com.example.ufoodlibrary.objects.G3RestaurantObj;
import com.example.ufoodlibrary.objects.G3ReviewObj;
import com.example.ufoodlibrary.objects.G3UserObj;
import com.example.ufoodlibrary.utilities.G3Application;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by Alfonso on 21-May-16.
 */
public class FirebaseManager implements FirebaseInterface {

    private String accountId;
    private static FirebaseManager instance= null;

    private static String FIREBASE_STORAGE_REF = "gs://project-1780749557739696720.appspot.com";
    private static String STORAGE_IMAGES = "images";

    private static String DATABASE_USERS = "users";
    private static String DATABASE_RESTAURANTS = "restaurants";
    private static String DATABASE_ORDERS = "orders";
    private static String DATABASE_REVIEWS = "reviews";
    private static String DATABASE_USER_FAVOURITE = "usersFavourite";
    private static String DATABASE_RESTAURANT_FAVOURITE = "restaurantFavourite";
    private static String DATABASE_NOTIFY_DAILY = "notifyDaily";



    private FirebaseManager(){
        instance= this;
    }

    public static FirebaseManager newInstance(){
        if(instance == null){
            return new FirebaseManager();
        }
        else{
            return instance;
        }
    }

    /**
     * <p>WARNING !!! NOT USE THIS METHOD !!!</p>
     * @param accID
     */
    @Override
    public void setAccountId(String accID) {
        accountId = accID;
    }

    @Override
    public String getCurrentId() {
        return accountId;
    }

    @Override
    public boolean isSignedin() {
        if(accountId == null){
            return false;
        }
        else{
            return true;
        }
    }


    /**
     * <p>This method save a user profile into the database, is the client isn't authenticated throws @NotAuthenticatedException</p>
     * @param user @G3UserObj
     * @param listener @OnCompleteListener<Void>
     * @throws NotAuthenticatedException
     * @throws NetworkDownException
     */
    @Override
    public void saveUserProfile(G3UserObj user, OnCompleteListener<Void> listener) throws NotAuthenticatedException,NetworkDownException{

        if(!G3Application.isNetworkAvailable()){
            throw new NetworkDownException();
        }

        if(accountId == null) {
            throw new NotAuthenticatedException();
        }
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        if(mDatabase != null) {
            user.setId(accountId);
            mDatabase.getReference().child("users").child(accountId).setValue(user).addOnCompleteListener(listener);

        }

    }

    /**
     * <p>This method save the Restaurant profile in the database, if the client isn't authenticated throws a @NotAuthenticatedException</p>
     * @param restaurantObj @G3RestaurantObj
     * @param listener @Activity
     * @throws NotAuthenticatedException
     * @throws NetworkDownException if network is absent
     */
    @Override
    public void saveRestaurantProfile(G3RestaurantObj restaurantObj, OnCompleteListener<Void> listener) throws NotAuthenticatedException, NetworkDownException {

        if(!G3Application.isNetworkAvailable()){
            throw new NetworkDownException();
        }

        if(accountId == null) {
            throw new NotAuthenticatedException();
        }

        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        if(mDatabase != null) {
            restaurantObj.setId(accountId);
            mDatabase.getReference().child(DATABASE_RESTAURANTS).child(accountId)
                    .setValue(restaurantObj).addOnCompleteListener(listener);

        }
//        GeoFire geoFire = new GeoFire(new Firebase("https://project-1780749557739696720.firebaseio.com/"));
//        double lat = restaurantObj.getAddressObj().getLatitude();
//        double log = restaurantObj.getAddressObj().getLongitude();
//        GeoLocation geoloc = new GeoLocation(lat,log);
//        geoFire.setLocation(accountId,geoloc);

    }


    /**
     * <p>This method signIn a user or restaurant</p>
     * @param email @String
     * @param password @String
     * @param listener @OnCompleteListener
     * @throws NetworkDownException if connection absent
     */
    @Override
    public void signIn( String email, String password, OnCompleteListener<AuthResult> listener) throws NetworkDownException {
        FirebaseAuth mAuth;

        if(!G3Application.isNetworkAvailable()){
            throw new NetworkDownException();
        }

        mAuth = FirebaseAuth.getInstance();
        if(mAuth != null) {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(listener);
        }

    }

    /**
     * <p>This method signup new user in the database (either for users and for restaurant )</p>
     * @param email @String
     * @param password @String
     * @param listener @OnCompleteListener<AuthResult>
     * @throws NetworkDownException
     */
    @Override
    public void signUp( String email, String password, OnCompleteListener<AuthResult> listener) throws NetworkDownException{
        FirebaseAuth mAuth;
        if(!G3Application.isNetworkAvailable()){
            throw new NetworkDownException();
        }

        mAuth = FirebaseAuth.getInstance();
        if(mAuth != null) {

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(listener);
        }

    }

    @Override
    public void deleteAccount(OnCompleteListener<Void> listener) throws NetworkDownException, NotAuthenticatedException {

        if(!G3Application.isNetworkAvailable()){
            throw new NetworkDownException();
        }

        if(!isSignedin()){
            throw new NotAuthenticatedException();
        }

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            user.delete().addOnCompleteListener(listener);
        }

    }


    /**
     * <p>This method signOut hte client, if the client isn't signed in throws @NotAuthenticatedException </p>
     * @param listener @FirebaseAuth.AuthStateListener
     * @throws NotAuthenticatedException
     * @throws NetworkDownException
     */
    @Override
    public void signOut(FirebaseAuth.AuthStateListener listener) throws NotAuthenticatedException,NetworkDownException {

        if(!G3Application.isNetworkAvailable()){
            throw new NetworkDownException();
        }

        if(accountId == null){
            throw new NotAuthenticatedException();
        }

        FirebaseAuth mAuth;

        mAuth = FirebaseAuth.getInstance();
       if(mAuth != null){
           mAuth.signOut();

           mAuth.addAuthStateListener(listener);
       }
    }

    /**
     * <p>WARNING!!! This method is async, block UI thread with ProgressDialog until the listener return</p>
     * This method get the profile, set the DataSnapshot.getValue in the ValueEventListener to retrive the specifc account type (G3UserObj)
     * @param listener @ValueEventListener
     * @throws NotAuthenticatedException
     * @throws NetworkDownException if network is absent
     */
    @Override
    public void getUserProfile(ValueEventListener listener) throws NotAuthenticatedException, NetworkDownException {

        if(!G3Application.isNetworkAvailable()){
            throw new NetworkDownException();
        }

        if(accountId == null) {
            throw new NotAuthenticatedException();
        }

        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        mDatabase.getReference().child(DATABASE_USERS).child(accountId).addListenerForSingleValueEvent(listener);

    }

    /**
     * <p>This method get a specific user</p>
     * @param key
     * @param listener
     * @throws NotAuthenticatedException
     * @throws NetworkDownException
     */
    @Override
    public void getUserProfileWithKey(String key, ValueEventListener listener) throws  NetworkDownException {

        if(!G3Application.isNetworkAvailable()){
            throw new NetworkDownException();
        }


        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        mDatabase.getReference().child(DATABASE_USERS).child(key).addListenerForSingleValueEvent(listener);
    }

    /**
     * <p>WARNING!!! This method is async, block UI thread with ProgressDialog until the listener return</p>
     * This method get the profile, set the DataSnapshot.getValue in the ValueEventListener to retrive the specifc account type (G3RestaurantObj)
     * @param listener @ValueEventListener
     * @throws NotAuthenticatedException
     * @throws NetworkDownException if network is absent
     */
    @Override
    public void getRestaurantProfile(ValueEventListener listener) throws NotAuthenticatedException, NetworkDownException {

        if(!G3Application.isNetworkAvailable()){
            throw new NetworkDownException();
        }

        if(accountId == null) {
            throw new NotAuthenticatedException();
        }

        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        mDatabase.getReference().child(DATABASE_RESTAURANTS).child(accountId).addListenerForSingleValueEvent(listener);

    }

    /**
     *
     * @param key
     * @param listener
     * @throws NotAuthenticatedException
     * @throws NetworkDownException
     */
    @Override
    public void getRestaurantProfileWithKey(String key, ValueEventListener listener) throws NotAuthenticatedException, NetworkDownException {

        if(!G3Application.isNetworkAvailable()){
            throw new NetworkDownException();
        }

        if(accountId == null) {
            throw new NotAuthenticatedException();
        }

        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        mDatabase.getReference().child(DATABASE_RESTAURANTS).child(key).addListenerForSingleValueEvent(listener);
    }

    /**
     * This method verify if an accountId is a Restaurant
     * @throws NotAuthenticatedException
     * @throws NetworkDownException
     */
    @Override
    public void isRestaurant(ValueEventListener listener) throws NotAuthenticatedException, NetworkDownException {
        if(!isSignedin()){
            throw new NotAuthenticatedException();
        }
        if(!G3Application.isNetworkAvailable()){
            throw new NetworkDownException();
        }

        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();

        mDatabase.getReference().child(DATABASE_RESTAURANTS).child(accountId).addListenerForSingleValueEvent(listener);

    }

    /**
     * <p>WARNING!!! This method is async, block UI thread with ProgressDialog until the listener return</p>
     * This method returns all restaurants
     * @param listenr @ValueEventListener
     * @throws NetworkDownException if network is absent
     */
    @Override
    public void getRestaurants(ValueEventListener listenr) throws NetworkDownException {
        if(!G3Application.isNetworkAvailable()){
            throw new NetworkDownException();
        }

        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        mDatabase.getReference().child(DATABASE_RESTAURANTS).addListenerForSingleValueEvent(listenr);
    }

    @Override
    public void saveImageToFirebaseStorage(String filename, Bitmap bitmap, OnFailureListener faillistener, OnSuccessListener<UploadTask.TaskSnapshot> successListener) throws NetworkDownException {

        if(!G3Application.isNetworkAvailable()){
            throw new NetworkDownException();
        }

        FirebaseStorage storage = FirebaseStorage.getInstance();

        StorageReference storageRef = storage.getReferenceFromUrl(FIREBASE_STORAGE_REF);
        StorageReference mFileRef= storageRef.child(STORAGE_IMAGES).child(filename);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = mFileRef.putBytes(data);
        uploadTask.addOnFailureListener(faillistener).addOnSuccessListener(successListener);


    }

    /**
     * This method get an image from firebase storage
     * @param url
     * @param file
     * @param listener
     * @throws NetworkDownException
     */
    @Override
    public void getImageFormFirebaseStorage(String url,File file, OnSuccessListener<FileDownloadTask.TaskSnapshot> listener) throws NetworkDownException {

        StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(url);

        if(!G3Application.isNetworkAvailable()){
            throw new NetworkDownException();
        }

        storageRef.getFile(file).addOnSuccessListener(listener);

    }

    /**
     *
     * @param listener
     * @throws NotAuthenticatedException
     * @throws NetworkDownException
     */
    @Override
    public void getRestaurantOrders(ValueEventListener listener) throws NotAuthenticatedException, NetworkDownException {

        if(!isSignedin()){
            throw new NotAuthenticatedException();
        }
        if(!G3Application.isNetworkAvailable()){
            throw new NetworkDownException();
        }

        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();

        mDatabase.getReference().child(DATABASE_ORDERS).orderByChild("restaurantId").equalTo(accountId).addListenerForSingleValueEvent(listener);
    }

    /**
     *
     * @param listener
     * @throws NotAuthenticatedException
     * @throws NetworkDownException
     */
    @Override
    public void getNewOrders(long timestamp, ValueEventListener listener) throws NotAuthenticatedException, NetworkDownException {

        if(!isSignedin()){
            throw new NotAuthenticatedException();
        }
        if(!G3Application.isNetworkAvailable()){
            throw new NetworkDownException();
        }

        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();

        mDatabase.getReference().child(DATABASE_ORDERS).orderByChild("timestamp").startAt(timestamp).addListenerForSingleValueEvent(listener);

    }

    /**
     * <p>This method get all reviews from logged restaurant</p>
     * @param listener
     * @throws NotAuthenticatedException
     * @throws NetworkDownException
     */
    @Override
    public void getRestaurantReviews(ValueEventListener listener) throws NotAuthenticatedException, NetworkDownException {

        if(!isSignedin()){
            throw new NotAuthenticatedException();
        }
        if(!G3Application.isNetworkAvailable()){
            throw new NetworkDownException();
        }

        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();

        mDatabase.getReference().child(DATABASE_REVIEWS).orderByChild("restaurantId").equalTo(accountId).addListenerForSingleValueEvent(listener);
    }

    @Override
    public void getRestaurantReviewsbyKeyRestaurant(String restaurantId, ValueEventListener listener) throws NetworkDownException {


        if(!G3Application.isNetworkAvailable()){
            throw new NetworkDownException();
        }

        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();

        mDatabase.getReference().child(DATABASE_REVIEWS).orderByChild("restaurantId").equalTo(restaurantId).addListenerForSingleValueEvent(listener);
    }

    /**
     *
     * @param listener
     * @throws NotAuthenticatedException
     * @throws NetworkDownException
     */
    @Override
    public void getUserOrders(ValueEventListener listener) throws NotAuthenticatedException, NetworkDownException {

        if(!isSignedin()){
            throw new NotAuthenticatedException();
        }
        if(!G3Application.isNetworkAvailable()){
            throw new NetworkDownException();
        }

        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();

        mDatabase.getReference().child(DATABASE_ORDERS).orderByChild("userid").equalTo(accountId).addListenerForSingleValueEvent(listener);

    }



    /**
     *
     * @param listener
     * @throws NotAuthenticatedException
     * @throws NetworkDownException
     */
    @Override
    public void getFavouriteRestaurant(ValueEventListener listener) throws NotAuthenticatedException, NetworkDownException {
        if(!isSignedin()){
            throw new NotAuthenticatedException();
        }
        if(!G3Application.isNetworkAvailable()){
            throw new NetworkDownException();
        }

        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();

        mDatabase.getReference().child(DATABASE_USER_FAVOURITE).child(accountId).addListenerForSingleValueEvent(listener);
    }

    /**
     *
     * @param restaurantId
     * @param listener
     * @throws NotAuthenticatedException
     * @throws NetworkDownException
     */
    @Override
    public void isFavourite(String restaurantId , ValueEventListener listener) throws NotAuthenticatedException, NetworkDownException {
        if(!isSignedin()){
            throw new NotAuthenticatedException();
        }
        if(!G3Application.isNetworkAvailable()){
            throw new NetworkDownException();
        }

        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();

        mDatabase.getReference().child(DATABASE_USER_FAVOURITE).child(accountId).orderByValue().equalTo(restaurantId).addValueEventListener(listener);

    }

    /**
     *
     * @param restaurantId
     * @throws NotAuthenticatedException
     * @throws NetworkDownException
     */
    @Override
    public void removeFavourite(final String restaurantId) throws NotAuthenticatedException, NetworkDownException {
        if(!isSignedin()){
            throw new NotAuthenticatedException();
        }
        if(!G3Application.isNetworkAvailable()){
            throw new NetworkDownException();
        }

        final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();

        mDatabase.getReference().child(DATABASE_USER_FAVOURITE).child(accountId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    if (d.getValue() != null) {
                        if (d.getValue().equals(restaurantId)) {
                            mDatabase.getReference().child(DATABASE_USER_FAVOURITE).child(accountId).child(d.getKey()).removeValue();
                        }
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mDatabase.getReference().child(DATABASE_RESTAURANT_FAVOURITE).child(restaurantId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    if (d.getValue() != null) {
                        if (d.getValue().equals(accountId)) {
                            mDatabase.getReference().child(DATABASE_RESTAURANT_FAVOURITE).child(restaurantId).child(d.getKey()).removeValue();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    /**
     *
     * @param listener
     * @throws NotAuthenticatedException
     * @throws NetworkDownException
     */
    @Override
    public void getFavouriteUsers(ValueEventListener listener) throws NotAuthenticatedException, NetworkDownException {
        if(!isSignedin()){
            throw new NotAuthenticatedException();
        }
        if(!G3Application.isNetworkAvailable()){
            throw new NetworkDownException();
        }

        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();

        mDatabase.getReference().child(DATABASE_RESTAURANT_FAVOURITE).child(accountId).addListenerForSingleValueEvent(listener);
    }

    /**
     *
     * @param orderid
     * @param listener
     * @throws NetworkDownException
     */
    @Override
    public void getOrderWithKey(String orderid, ValueEventListener listener) throws NetworkDownException {
        if(!G3Application.isNetworkAvailable()){
            throw new NetworkDownException();
        }

        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();

        mDatabase.getReference().child(DATABASE_ORDERS).child(orderid).addListenerForSingleValueEvent(listener);
    }

    /**
     * <p>This method save an order to database</p>
     * @param order
     * @param listener
     * @throws NotAuthenticatedException
     * @throws NetworkDownException
     */
    @Override
    public void addOrder(G3OrderObj order, OnCompleteListener<Void> listener) throws NotAuthenticatedException, NetworkDownException {
        if(!isSignedin()){
            throw new NotAuthenticatedException();
        }
        if(!G3Application.isNetworkAvailable()){
            throw new NetworkDownException();
        }

        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        String orderid= accountId+"_"+System.currentTimeMillis();
        if(mDatabase != null) {
            order.setUserid(accountId);
            order.setOrderid(orderid);
            mDatabase.getReference().child(DATABASE_ORDERS).child(orderid)
                    .setValue(order).addOnCompleteListener(listener);
        }

    }

    /**
     *
     * @param review
     * @param order
     * @param listener
     * @throws NotAuthenticatedException
     * @throws NetworkDownException
     */
    @Override
    public void addReview(G3ReviewObj review, G3OrderObj order, OnCompleteListener<Void> listener) throws NotAuthenticatedException, NetworkDownException {
        if(!isSignedin()){
            throw new NotAuthenticatedException();
        }
        if(!G3Application.isNetworkAvailable()){
            throw new NetworkDownException();
        }

        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        String reviewid= order.getOrderid()+"_"+System.currentTimeMillis();
        order.setReviewId(reviewid);
        review.setId(reviewid);
        if(mDatabase != null) {
            mDatabase.getReference().child(DATABASE_ORDERS).child(order.getOrderid())
                    .setValue(order);
        }
        if(mDatabase != null){
            mDatabase.getReference().child(DATABASE_REVIEWS).child(reviewid)
                    .setValue(review).addOnCompleteListener(listener);
        }
    }

    @Override
    public void addFavouriteRestaurant(String restaurantId) throws NotAuthenticatedException, NetworkDownException {

        if(!isSignedin()){
            throw new NotAuthenticatedException();
        }
        if(!G3Application.isNetworkAvailable()){
            throw new NetworkDownException();
        }

        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        String time= ""+System.currentTimeMillis();
        if(mDatabase != null) {
            mDatabase.getReference().child(DATABASE_USER_FAVOURITE).child(accountId).child(time).setValue(restaurantId);
            mDatabase.getReference().child(DATABASE_RESTAURANT_FAVOURITE).child(restaurantId).child(time).setValue(accountId);
        }

    }

    @Override
    public void setOrderViewed(String orderid) throws NetworkDownException {

        if(!G3Application.isNetworkAvailable()){
            throw new NetworkDownException();
        }

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("visualized",true);

        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        if(mDatabase != null) {
            mDatabase.getReference().child(DATABASE_ORDERS).child(orderid).updateChildren(childUpdates);
        }
    }

    @Override
    public void setOrderEvaded(String orderid) throws NetworkDownException {

        if(!G3Application.isNetworkAvailable()){
            throw new NetworkDownException();
        }

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("orderState",1);
        childUpdates.put("visualized",true);
        childUpdates.put("userVisualized",false);

        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        if(mDatabase != null) {
            mDatabase.getReference().child(DATABASE_ORDERS).child(orderid).updateChildren(childUpdates);
        }

    }

    @Override
    public void setOrderDeleted(String orderid) throws NetworkDownException {
        if(!G3Application.isNetworkAvailable()){
            throw new NetworkDownException();
        }

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("orderState",2);
        childUpdates.put("visualized",true);
        childUpdates.put("userVisualized",false);

        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        if(mDatabase != null) {
            mDatabase.getReference().child(DATABASE_ORDERS).child(orderid).updateChildren(childUpdates);
        }


    }

    @Override
    public void setUserOrderVisualized(String orderid) throws NetworkDownException {

        if(!G3Application.isNetworkAvailable()){
            throw new NetworkDownException();
        }

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("userVisualized",true);

        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        if(mDatabase != null) {
            mDatabase.getReference().child(DATABASE_ORDERS).child(orderid).updateChildren(childUpdates);
        }

    }

    @Override
    public void updateRestaurantProfile(G3RestaurantObj restaurantObj, OnCompleteListener<Void> listener) throws NetworkDownException {

        if(!G3Application.isNetworkAvailable()){
            throw new NetworkDownException();
        }

        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        if(mDatabase != null) {
            mDatabase.getReference().child(DATABASE_RESTAURANTS).child(restaurantObj.getId())
                    .setValue(restaurantObj).addOnCompleteListener(listener);

        }
    }

    @Override
    public void addReplyToReview(String reviewId, String replyText, OnCompleteListener<Void> listener) throws NetworkDownException {

        if(!G3Application.isNetworkAvailable()){
            throw new NetworkDownException();
        }

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("reply", replyText);

        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        if(mDatabase != null) {
            mDatabase.getReference().child(DATABASE_REVIEWS).child(reviewId).updateChildren(childUpdates)
                    .addOnCompleteListener(listener);
        }

    }

    @Override
    public void recoveryPasswordByEmail(String email, OnCompleteListener<Void> listener) throws NetworkDownException {

        if(!G3Application.isNetworkAvailable()){
            throw new NetworkDownException();
        }

        FirebaseAuth mAuth;

        mAuth = FirebaseAuth.getInstance();
        if(mAuth != null){
            mAuth.sendPasswordResetEmail(email.trim()).addOnCompleteListener(listener);
        }
    }


    @Override
    public void addNotifyDaily(final String name) throws NetworkDownException {
        if(!G3Application.isNetworkAvailable()){
            throw new NetworkDownException();
        }

        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        final ArrayList<String> utenti = new ArrayList<>();
        if(mDatabase != null) {
            mDatabase.getReference().child(DATABASE_RESTAURANT_FAVOURITE).child(this.accountId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot d : dataSnapshot.getChildren()) {
                        String s = d.getValue(String.class);
                        utenti.add(s);
                        setDailyNotification(utenti, name);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }


    }

    @Override
    public void getNotifyDaily(ValueEventListener listener) throws NotAuthenticatedException, NetworkDownException {
        if(!isSignedin()){
            throw new NotAuthenticatedException();
        }
        if(!G3Application.isNetworkAvailable()){
            throw new NetworkDownException();
        }

        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();

        if(mDatabase != null) {
            mDatabase.getReference().child(DATABASE_NOTIFY_DAILY).child(accountId).addListenerForSingleValueEvent(listener);

        }


    }

    @Override
    public void setNotifyDaily(String restaurantName, boolean value) throws NetworkDownException, NotAuthenticatedException {
        if(!isSignedin()){
            throw new NotAuthenticatedException();
        }
        if(!G3Application.isNetworkAvailable()){
            throw new NetworkDownException();
        }

        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();

        if(mDatabase != null) {
            mDatabase.getReference().child(DATABASE_NOTIFY_DAILY).child(accountId).child(restaurantName).setValue(false);

        }


    }

    private void setDailyNotification(ArrayList<String> utenti, String nameRest) {

        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();

        if(mDatabase != null) {
            for(String utente: utenti){
                mDatabase.getReference().child(DATABASE_NOTIFY_DAILY).child(utente).child(nameRest).setValue(true);

            }
        }

    }


}
