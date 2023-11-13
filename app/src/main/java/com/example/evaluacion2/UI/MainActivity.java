package com.example.evaluacion2.UI;

import static com.example.evaluacion2.TODOApplication.firebaseFirestore;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.evaluacion2.R;
import com.example.evaluacion2.Adapters.ADListaActiva;
import com.example.evaluacion2.Adapters.ADListaCompleta;
import com.example.evaluacion2.Clases.Compras;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import org.checkerframework.checker.nullness.qual.NonNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements  ADListaActiva.OnItemClickListener{
    Button btnCompra;
    BottomSheetDialog dialogo;
    RecyclerView rcvComprasActivas, rcvComprasCompletas;
    ADListaActiva comprasActivasAdapter;
    ADListaCompleta comprasCompletasAdapter;
    List<Compras> comprasActivasList = new ArrayList<>();
    List<Compras> comprasCompletasList = new ArrayList<>();
    private LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnCompra = findViewById(R.id.btnCompra);

        dialogo = new BottomSheetDialog(this);
        ConfigurarAdaptadorComprasActivas();
        ConfigurarAdaptadorComprasCompletas();

        btnCompra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogo.show();
            }
        });

        View v = getLayoutInflater().inflate(R.layout.item_bottom, null, false);
        Button btnGuardar = v.findViewById(R.id.btnGuardar);
        EditText edtPresupuesto = v.findViewById(R.id.edtPresupuesto);
        EditText edtTitulo = v.findViewById(R.id.edtTitulo);
        dialogo.setContentView(v);
        ActualizarCompras();
        Compras nuevaCompra = new Compras();
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogo.dismiss();
                if(!edtPresupuesto.getText().equals("") && !edtTitulo.getText().equals("")){

                    nuevaCompra.setTitulo(edtTitulo.getText().toString());
                    nuevaCompra.setPresupuesto(Double.parseDouble(edtPresupuesto.getText().toString()));
                    nuevaCompra.setActiva(true);
                    nuevaCompra.setTotal(0);
                    Map<String, Object> compra = new HashMap<>();
                    compra.put("idCompra", nuevaCompra.getIdCompra());
                    compra.put("titulo", nuevaCompra.getTitulo());
                    compra.put("presupuesto", nuevaCompra.getPresupuesto());
                    compra.put("activa", nuevaCompra.isActiva());
                    compra.put("total", nuevaCompra.getTotal());
                    // Add a new document with a generated ID
                    firebaseFirestore.collection("compras")
                    .add(compra)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference)
                        {
                            ActualizarCompras();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //Log.w(TAG, "Error adding document", e);
                            Toast.makeText(MainActivity.this, "Ocurrio problema" + e, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        dialogo.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

    }

    private void ActualizarCompras() {
        firebaseFirestore.collection("compras")
        .get()
        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                CargarCompras(queryDocumentSnapshots);
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Maneja el error de recuperación de datos
            }
        });
    }

    private void borrarTodosLosDocumentos() {
        CollectionReference comprasCollection = firebaseFirestore.collection("compras");

        comprasCollection.get()
        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    document.getReference().delete();
                }
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Manejar errores en caso de que falle la eliminación de documentos
            }
        });
    }


    private void CargarCompras(QuerySnapshot queryDocumentSnapshots){
        if(queryDocumentSnapshots.size() > 0){
            comprasActivasList.clear();
            comprasCompletasList.clear();
            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                String idCompra = document.getId();
                String titulo = document.getString("titulo");
                double presupuesto = document.getDouble("presupuesto");
                double total = document.getDouble("total");
                boolean activa = document.getBoolean("activa");

                Compras compra = new Compras();
                compra.setIdCompra(idCompra);
                compra.setTitulo(titulo);
                compra.setPresupuesto(presupuesto);
                compra.setActiva(activa);
                compra.setTotal(total);
                if(compra.isActiva()){
                    comprasActivasList.add(compra);
                } else{
                    comprasCompletasList.add(compra);
                }

            }
            comprasActivasAdapter.notifyDataSetChanged();
            comprasCompletasAdapter.notifyDataSetChanged();
        }

    }

    private void ConfigurarAdaptadorComprasActivas(){
        // Configurando adaptador
        comprasActivasAdapter = new ADListaActiva(comprasActivasList, MainActivity.this);
        layoutManager = new LinearLayoutManager(this);
        rcvComprasActivas = findViewById(R.id.rcvActivas);
        rcvComprasActivas.setAdapter(comprasActivasAdapter);
        rcvComprasActivas.setLayoutManager(layoutManager);
        rcvComprasActivas.setHasFixedSize(true);
    }

    private void ConfigurarAdaptadorComprasCompletas(){
        // Configurando adaptador
        comprasCompletasAdapter = new ADListaCompleta(comprasCompletasList);
        layoutManager = new LinearLayoutManager(this);
        rcvComprasCompletas = findViewById(R.id.rcvCompletas);
        rcvComprasCompletas.setAdapter(comprasCompletasAdapter);
        rcvComprasCompletas.setLayoutManager(layoutManager);
        rcvComprasCompletas.setHasFixedSize(true);
    }

    @Override
    public void onItemClick(Compras compra) {
        // Crea un Intent
        Intent intent = new Intent(MainActivity.this, ComprasDiarias.class);

        // Crea un Bundle y coloca el objeto Compras en él
        Bundle bundle = new Bundle();
        bundle.putParcelable("compra", compra);

        // Agrega el Bundle como extra en el Intent
        intent.putExtras(bundle);

        // Inicia la actividad de destino
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ActualizarCompras();
    }
}