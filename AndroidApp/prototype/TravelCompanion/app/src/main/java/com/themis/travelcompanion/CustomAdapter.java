package com.themis.travelcompanion;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.Holder> {

    private Context context;
    private ArrayList<UserModel> arrayList;

    DatabaseHelper databaseHelper;

    public CustomAdapter(Context context, ArrayList<UserModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        databaseHelper = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.order, parent, false);

        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, final int position) {

        UserModel model = arrayList.get(position);
        final Integer sight_id = model.getSight_id();
        final String name = model.getName();
        final String comments = model.getComments();
        final String country = model.getCountry();
        final String address = model.getAddress();
        final String area = model.getArea();
        final String image = model.getImage();

        holder.sightpiciv.setImageURI(Uri.parse(image));
        holder.name.setText(name);
        holder.country.setText(country);
        holder.area.setText(area);

        holder.delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteDialog(
                        ""+sight_id
                );
            }
        });


        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                editDialog(
                        ""+position,
                        ""+sight_id,
                        ""+name,
                        ""+comments,
                        ""+image

                );
                return false;
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showinfoDialog(
                        ""+position,
                        ""+sight_id,
                        ""+name,
                        ""+comments,
                        ""+country,
                        ""+address,
                        ""+area,
                        ""+image
                );
            }
        });

    }



    private void deleteDialog(final String id) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete");
        builder.setMessage("Are you sure you want to delete?");
        builder.setCancelable(false);
        builder.setIcon(R.drawable.ic_action_delete);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {

                databaseHelper.deleteSight(id);
                ((SightseeingsList)context).onResume();
                Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();

            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {

            }
        });

        builder.create().show();
    }

    private void editDialog(String position, final String sight_id, final String name, final String comments,final String image) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Update");
        builder.setMessage("Are you sure you want to update?");
        builder.setCancelable(false);
        builder.setIcon(R.drawable.ic_action_edit);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                Intent intent = new Intent(context, EditActivity.class);
                intent.putExtra("sight_id", sight_id);
                intent.putExtra("name", name);
                intent.putExtra("comments", comments);
                intent.putExtra("image", image);
                intent.putExtra("editMode", true);
                context.startActivity(intent);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.cancel();
            }
        });
        builder.create().show();
    }


    private void showinfoDialog(String position, final String sight_id, final String name, final String comments,
                                final String country, final String address, final String area, final String image) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Details");
        builder.setMessage("Want to see more details?");
        builder.setCancelable(false);
        builder.setIcon(R.drawable.ic_action_play);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                Intent intent = new Intent(context, ShowDetails.class);
                intent.putExtra("sight_id", sight_id);
                intent.putExtra("name", name);
                intent.putExtra("comments", comments);
                intent.putExtra("country", country);
                intent.putExtra("address", address);
                intent.putExtra("area", area);
                intent.putExtra("image", image);
                context.startActivity(intent);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.cancel();
            }
        });
        builder.create().show();
    }



    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class Holder extends RecyclerView.ViewHolder {

        ImageView sightpiciv;
        TextView name, country, area;
        ImageButton delButton;

        public Holder(@NonNull View itemView) {
            super(itemView);

            sightpiciv = itemView.findViewById(R.id.sightpiciv);
            name = itemView.findViewById(R.id.sightnametv);
            country = itemView.findViewById(R.id.sightcountrytv);
            area = itemView.findViewById(R.id.sightareatv);
            delButton = itemView.findViewById(R.id.delbtn);


        }
    }

}
