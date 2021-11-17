package com.example.weatherapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.ViewHolder> {
  private Context context;
  private ArrayList<WeatherModel> arrayList ;

    public WeatherAdapter(Context context, ArrayList<WeatherModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public WeatherAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.weather_rv_item,parent , false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherAdapter.ViewHolder holder, int position) {
        WeatherModel model = arrayList.get(position);
        holder.temperature.setText(model.getTemperature() + "°C");
        Picasso.get().load("http:".concat(model.getIcon())).into(holder.condition);
        holder.wind_speed.setText(model.getWindSpeed()+ "Km/h");
        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        SimpleDateFormat output = new SimpleDateFormat("hh:mm aa");
        try{
            Date t = input.parse(model.getTime());
            holder.time.setText(output.format(t));
        }catch (ParseException e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView wind_speed , time , temperature;
        private ImageView condition;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            wind_speed = itemView.findViewById(R.id.wind_speed);
            time = itemView.findViewById(R.id.time);
            temperature = itemView.findViewById(R.id.temperature);
            condition = itemView.findViewById(R.id.condition);
        }
    }


}
