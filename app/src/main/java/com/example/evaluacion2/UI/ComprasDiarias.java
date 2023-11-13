package com.example.evaluacion2.UI;

import static com.example.evaluacion2.TODOApplication.firebaseFirestore;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.evaluacion2.R;
import com.example.evaluacion2.Adapters.ADListaCompras;
import com.example.evaluacion2.Clases.CompraDetalle;
import com.example.evaluacion2.Clases.Compras;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ComprasDiarias extends AppCompatActivity implements ADListaCompras.OnItemClickListener {

    RecyclerView rcvDetalles;
    TextView tvPresupuesto, tvTotal;
    EditText edtNombre, edtCantidad, edtPrecio;
    Button btnGuardar, btnAgregar;
    private LinearLayoutManager layoutManager;
    ADListaCompras detallesAdapter;
    List<CompraDetalle> detallesList = new ArrayList<>();
    private Compras compra;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compras_diarias);

        tvPresupuesto = findViewById(R.id.tvPresupuesto);
        tvTotal = findViewById(R.id.tvTotal);
        edtNombre = findViewById(R.id.edtNombre);
        edtCantidad = findViewById(R.id.edtCantidd);
        edtPrecio = findViewById(R.id.edtPrecio);
        btnAgregar = findViewById(R.id.btnAgregar);
        btnGuardar = findViewById(R.id.btnGuadar);
        rcvDetalles = findViewById(R.id.rcvCarrito);

        CargarDetalles();
        // Recupera el Bundle del Intent
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            // Recupera el objeto Compras del Bundle
            compra = bundle.getParcelable("compra");

            // Realiza operaciones con el objeto Compras
            if (compra != null) {
                tvPresupuesto.setText(compra.getPresupuesto() + "");
                tvTotal.setText(compra.getTotal() + "");
            }
        }

        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AgregarProductos(compra);
            }
        });

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(detallesList.size()>0){
                    CompletarCompra(compra);
                }else{
                    Toast.makeText(ComprasDiarias.this, "Para completar debe al menos agregar un producto.", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void AgregarProductos(Compras compra) {

        if(edtNombre.getText().toString().equals("")){
            Toast.makeText(this, "Escriba un nombre.", Toast.LENGTH_SHORT).show();
            return;
        }
        if(edtCantidad.getText().toString().equals("")){
            Toast.makeText(this, "Escriba la cantidad.", Toast.LENGTH_SHORT).show();
            return;
        }
        if(edtPrecio.getText().toString().equals("")){
            Toast.makeText(this, "Escriba el precio.", Toast.LENGTH_SHORT).show();
            return;
        }

        double total = LlenarLista(compra);
        //Establecer el total
        if(total==0){
            Toast.makeText(this, "La compra sobrepasa a el presupuesto", Toast.LENGTH_SHORT).show();
        } else{
            tvTotal.setText(total + "");
        }


    }

    private void CompletarCompra(Compras compra){
        ActualizarCompra(compra);
        finish();
    }

    private void ActualizarCompra(Compras compra) {
        // Supongamos que tienes el ID del documento que deseas editar y los nuevos datos en una Compras objeto llamado "nuevaCompra".

        // Crea un mapa con los nuevos datos que deseas actualizar en el documento.
        Map<String, Object> datosActualizados = new HashMap<>();
        datosActualizados.put("titulo", compra.getTitulo());
        datosActualizados.put("presupuesto", compra.getPresupuesto()-compra.getTotal());
        datosActualizados.put("activa", false);
        datosActualizados.put("total", compra.getTotal());

        // Accede al documento por su ID y actualiza los datos.
        firebaseFirestore.collection("compras")
                .document(String.valueOf(compra.getIdCompra()))  // Utiliza el método "document" para acceder al documento por su ID.
                .update(datosActualizados)  // Utiliza el método "update" para actualizar los datos.
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Maneja el éxito de la actualización
                        AgregarDetalles();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Maneja el error al actualizar el documento
                        Toast.makeText(ComprasDiarias.this, "Ocurrió un problema al actualizar el documento: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        System.out.println("ERROR: " + e.getMessage());
                    }
                });

    }

    private void AgregarDetalles() {
        for (CompraDetalle lista: detallesList) {
            Map<String, Object> compraDetalle = new HashMap<>();
            compraDetalle.put("idCompra", lista.getIdCompra());
            compraDetalle.put("nombre", lista.getNombre());
            compraDetalle.put("precio", lista.getPrecio());
            compraDetalle.put("cantidad", lista.getCantidad());
            firebaseFirestore.collection("compradetalle")
                    .add(compraDetalle)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference)
                        {

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //Log.w(TAG, "Error adding document", e);
                            Toast.makeText(ComprasDiarias.this, "Ocurrio problema" + e, Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void CargarDetalles(){
        // Configurando adaptador
        detallesAdapter = new ADListaCompras(detallesList, ComprasDiarias.this);
        layoutManager = new LinearLayoutManager(this);
        rcvDetalles.setAdapter(detallesAdapter);
        rcvDetalles.setLayoutManager(layoutManager);
        rcvDetalles.setHasFixedSize(true);
    }

    private double LlenarLista(Compras compra){
        double total = Double.parseDouble(tvTotal.getText().toString());
        CompraDetalle detalle = new CompraDetalle();
        detalle.setIdCompra(this.compra.getIdCompra());
        detalle.setNombre(edtNombre.getText().toString());
        detalle.setCantidad(Integer.parseInt(edtCantidad.getText().toString()));
        detalle.setPrecio(Double.parseDouble(edtPrecio.getText().toString()));
        total += (detalle.getCantidad()*detalle.getPrecio());

        if(compra.getPresupuesto()>=total){
            detallesList.add(detalle);
            detallesAdapter.notifyDataSetChanged();
            return total;
        }else{
            return 0;
        }

    }

    @Override
    public void onItemClick(CompraDetalle detalle) {

    }
}