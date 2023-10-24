package com.example.androidworrkshop.ui

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.example.androidworrkshop.R
import com.example.androidworrkshop.databinding.ItemMatchBinding
import com.example.androidworrkshop.model.Match
import com.example.androidworrkshop.model.MatchDetail
import com.example.androidworrkshop.model.MatchDetailsMap

var k:Int=0
var j:Int=0
var l:Int=0

class MatchAdapter(private val context: Context, private val MatchDetails : MutableList<Match>,private val pastMatchesSize: Int,private val liveMatchesSize:Int) : RecyclerView.Adapter<MatchAdapter.ViewHolder>() {



    class ViewHolder(binding: ItemMatchBinding) : RecyclerView.ViewHolder(binding.root){
        val Team1 = binding.cvTv1
        val time = binding.cvTime
        val location = binding.location
        val matchT= binding.matchT
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemMatchBinding.inflate(
                LayoutInflater.from(parent.context),parent,false
            )
        )
    }

    override fun getItemCount(): Int {
        return MatchDetails.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       val model = MatchDetails[position]

        holder.Team1.text = model.matchInfo.team1?.teamSName+" vs "+model.matchInfo.team2?.teamSName
        holder.time.text = model.matchInfo.status
        holder.location.text = model.matchInfo.venueInfo?.city

        if(model.matchInfo.state=="Complete" && position==0)
        {
            k=position
            holder.matchT.visibility=View.VISIBLE
            holder.matchT.text="Past Matches"
        }
        else if(position==pastMatchesSize)
        {
            j=position
            holder.matchT.visibility=View.VISIBLE
            holder.matchT.text="Live Matches"
        }
        else if( position==pastMatchesSize+liveMatchesSize)
        {
            l=1
            holder.matchT.visibility=View.VISIBLE
            holder.matchT.text="Upcoming Matches"
        }
        else
        {
            holder.matchT.visibility=View.GONE
        }

        holder.itemView.setOnClickListener{
           if(model.matchInfo.state!="Upcoming"){
               val intent=Intent(context,MatchInfoActivity::class.java)
               intent.putExtra("matchId",model.matchInfo.matchId.toString())
               context.startActivity(intent)
           }else{
               Toast.makeText(context, "No Scorecard available", Toast.LENGTH_SHORT).show()
           }
       }
    }

}