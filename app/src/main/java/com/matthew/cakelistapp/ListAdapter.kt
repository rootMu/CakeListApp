package com.matthew.cakelistapp

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.matthew.cakelistapp.model.cake.Cake
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_item.view.*

/**
 * @author Matthew Howells.
 */

class ListAdapter (private val cakeData: ArrayList<Cake>, private val favourite: (Cake) -> Unit, private val onClick: (Cake) -> Unit) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.list_item, parent, false)
        return CakeViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        // Populate ViewHolder with data that corresponds to the position in the list
        // which we are told to load
        (holder as CakeViewHolder).bind(cakeData[position], favourite, onClick)
    }

    override fun getItemCount() = cakeData.size

    class CakeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(cake: Cake, favourite: (Cake) -> Unit, onClick: (Cake) -> Unit) {
            itemView.tvTitle.text = cake.title

            //update to use all images with a custom view pager
            Picasso.get()
                    .load(cake.image)
                    .centerCrop()
                    .resize(150,150)
                    .into(itemView.ivMainImage)


            setFavourite(cake, favourite)
            setOnClick(cake, onClick)
        }

        private fun setFavourite(cake: Cake, favourite: (Cake) -> Unit) {

            cake.favourite.let {
                itemView.favourite?.isActivated = it
            }

            //set onclicklistener to toggle favourite
            itemView.favourite.setOnClickListener {
                favourite(cake)
            }
        }

        private fun setOnClick(cake: Cake, onClick: (Cake) -> Unit) {

            //set onClickListener to open description
            itemView.setOnClickListener {
                onClick(cake)
            }
        }
    }
}