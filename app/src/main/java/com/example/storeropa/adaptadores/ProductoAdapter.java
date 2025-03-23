package com.example.storeropa.adaptadores;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.example.storeropa.R;
import com.example.storeropa.entidades.Producto;

import java.util.List;

public class ProductoAdapter extends ArrayAdapter<Producto> {

    private Context context;
    private List<Producto> listaProductos;

    public ProductoAdapter(@NonNull Context context, List<Producto> listaProductos) {
        super(context, R.layout.list_item, listaProductos);
        this.context = context;
        this.listaProductos = listaProductos;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        }

        Producto producto = listaProductos.get(position);

        TextView txtTipo = convertView.findViewById(R.id.txtItemTipo);
        TextView txtGenero = convertView.findViewById(R.id.txtItemGenero);
        Button button = convertView.findViewById(R.id.btnInLineItem);

        txtTipo.setText(producto.getTipo());

        String genero = producto.getGenero();
        if (genero.equals("F")) {
            genero = "Dama";
        } else if (genero.equals("M")) {
            genero = "Varón";
        }

        txtGenero.setText(genero);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mensaje = "";
                mensaje += "Tipo de Ropa: " + producto.getTipo() + "\n";
                mensaje += "Genero: " + producto.getTipo() + "\n";
                mensaje += "Talla: " + producto.getTalla() + "\n";
                mensaje += "Precio: " + producto.getPrecio() + "\n";
                showModal(mensaje);
            }
        });

        return convertView;
    }

    private void showModal(String message){
        AlertDialog.Builder dialogo = new AlertDialog.Builder(this.context);
        dialogo.setTitle("Store Ropa");
        dialogo.setMessage(message);
        dialogo.setCancelable(false);

        dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialogo.create().show();
    }
}
