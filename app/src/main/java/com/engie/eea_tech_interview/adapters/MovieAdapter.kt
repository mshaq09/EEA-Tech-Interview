package com.engie.eea_tech_interview.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.engie.eea_tech_interview.MainActivity
import com.engie.eea_tech_interview.R
import com.engie.eea_tech_interview.helpers.Utils
import com.engie.eea_tech_interview.model.Movie

class MovieAdapter(private val dataSet: List<Movie>) :
    RecyclerView.Adapter<MovieAdapter.ViewHolder>() {

    var onItemClick: ((Movie) -> Unit)? = null

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView
        val date: TextView
        val poster: ImageView

        init {
            // Define click listener for the ViewHolder's View.
            title = view.findViewById(R.id.title)
            poster = view.findViewById(R.id.poster)
            date = view.findViewById(R.id.date)

        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.text_row_item, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.title.text = dataSet[position].title ?: dataSet[position].originalTitle
        viewHolder.date.text =  "Release Date: ${dataSet[position].releaseDate}"
        Glide.with(viewHolder.itemView.context)
            .load(Utils.getPosterUrl(dataSet[position].posterPath))
            .placeholder(R.drawable.eea_mobile_logo)
            .into( viewHolder.poster)
        viewHolder.itemView.setOnClickListener {
            onItemClick?.invoke(dataSet[position])
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size
}
