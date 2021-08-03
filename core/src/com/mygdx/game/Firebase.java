package com.mygdx.game;

import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutures;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import java.util.Map;

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
     * @param doc documento en la base de datos
     * @param data datos guardados en la base de datos
     * @return 
     */
    public boolean insertData(String col, String doc, Map<String,String> data){
        try{
            DocumentReference docRef = bd.collection(col).document(doc);
            ApiFuture<WriteResult> result = docRef.set(data);
            System.out.println("Guardado con exito");
        return true;
        }
        catch (Exception e){
            System.out.println("No guardado");
        }
        return false;
    }
    
    
    
}
