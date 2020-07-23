package pucp.telecom.moviles.incidencias.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import pucp.telecom.moviles.incidencias.R;
import pucp.telecom.moviles.incidencias.entities.Incidence;

public class ListIncidencesAdapter extends RecyclerView.Adapter<ListIncidencesAdapter.IncidenceViewHolder>{

    private ArrayList<Incidence> incidencesList;
    Context context;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    // clase ViewHolder
    public static class IncidenceViewHolder extends RecyclerView.ViewHolder{
        public TextView incNameTextView, incDateTextView;
        public ImageView registradoImageView, atendidoImageView;

        public IncidenceViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            incNameTextView = itemView.findViewById(R.id.incNameTextView);
            incDateTextView = itemView.findViewById(R.id.incDateTextView);
            registradoImageView = itemView.findViewById(R.id.registradoImageView);
            atendidoImageView = itemView.findViewById(R.id.atendidoImageView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    public ListIncidencesAdapter(ArrayList<Incidence> list, Context c){
        this.incidencesList = list;
        this.context = c;
    }

    @NonNull
    @Override
    public IncidenceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(context).inflate(R.layout.item_rv_incidence,parent,false);
        IncidenceViewHolder incidenceViewHolder = new IncidenceViewHolder(item, mListener);
        return incidenceViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull IncidenceViewHolder holder, int position) {
        Incidence i = incidencesList.get(position);
        String incName = i.getIncidenceName();
        String incDate = i.getDate();
        String incStatus = i.getStatus();

        holder.incNameTextView.setText(incName);
        holder.incDateTextView.setText(incDate);

        // Configuración de íconos de registrado o atendido de incidencias
        if(incStatus.equals("registrado")){
            holder.registradoImageView.setVisibility(View.VISIBLE);
            holder.atendidoImageView.setVisibility(View.INVISIBLE);
        }else{
            holder.registradoImageView.setVisibility(View.INVISIBLE);
            holder.atendidoImageView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return incidencesList.size();
    }
}