package com.mygdx.game;

import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutures;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mygdx.game.DataStructures.MyDoubleLinkedList;
import com.mygdx.game.DataStructures.MyHashTable;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import jdk.internal.net.http.common.Pair;

/**
 * Clase para hacer una integracion muy simple con Firebase
 * @author 3200g
 */
public class Firebase {
    Firestore bd; 
    
    /**
     * Este metodo inicializa clase
     */
    public Firebase(){
    }
    
    /**
     * Este metodo inicializa la coneccion con la base de datos
     * @throws FileNotFoundException  si no encuentra el archivo json para conectarse devuelve esta excepcion
     */
    public void connect() throws FileNotFoundException {
        // Use the application default credentials

        FileInputStream serviceAccount
                = new FileInputStream("datastructuregame-firebase-adminsdk-1sfhu-a69b0c7dd8.json");
        try{
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

        FirebaseApp.initializeApp(options);
        bd = FirestoreClient.getFirestore();
            System.out.println("Se conecto con exito");
        }
        catch(Exception error){
            System.out.println("NO SE CONECTA");
        }
    }
    
    /**
     * Este metodo guarda datos en la base de datos en una coleccion y documento a elegir
     * @param col coleccion de la base de datos
     * @param data datos guardados en la base de datos
     * @return 
     */
    public DocumentReference insertData(String col,MyJsonReader data){
        try{
            
            ApiFuture<DocumentReference> docRef =  bd.collection(col).add(data);
            System.out.println("Guardado con exito");
            
        return docRef.get();
        }
        catch (Exception e){
            System.out.println("No guardado: "+ e);
        }
        return null;
    }
     public void updateData(String col, MyJsonReader data, DocumentReference docRef){
         try{
         ApiFuture<WriteResult> wresult = docRef.set(data);
             System.out.println("Update exitoso");
         }
         catch (Exception e){
             System.out.println("Update fallido");
         }
     }   
     public MyDoubleLinkedList<Pair<String,String>> searchData(String col) throws InterruptedException, ExecutionException{
         MyDoubleLinkedList<Pair<String,String>> toReturn = new MyDoubleLinkedList<>();
         CollectionReference puntajes = bd.collection(col);
         ApiFuture<QuerySnapshot> qp = puntajes.get();
         
         for (DocumentSnapshot dc : qp.get().getDocuments()){
             String id = dc.getId();
             String json = (String) dc.get("json");
             Pair<String,String> tuple = new Pair<>(id,json);
             toReturn.add(tuple);
         }     
         return toReturn;
     }
}
