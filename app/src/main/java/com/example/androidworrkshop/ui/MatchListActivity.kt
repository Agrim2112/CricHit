package com.example.androidworrkshop.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidworrkshop.R
import com.example.androidworrkshop.databinding.ActivityMatchListBinding
import com.example.androidworrkshop.di.Resource
import com.example.androidworrkshop.model.Match
import com.example.androidworrkshop.model.MatchDetail
import com.example.androidworrkshop.model.MatchDetailsMap
import com.example.androidworrkshop.model.MatchInfo
import com.example.androidworrkshop.model.MatchInfoX
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MatchListActivity : AppCompatActivity() {

    lateinit var binding : ActivityMatchListBinding
    lateinit var viewModel : MainViewModel
    private var MatchInfo : MatchInfo?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_match_list)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]


        viewModel.getMatches()

        setObservers()
    }

    private fun setObservers(){
        viewModel.performFetchMatchesStatus.observe(this, Observer {
            when(it.status){
                Resource.Status.LOADING -> {
                    Log.e("setObservers", "Loading")
//                    binding.progressBar.visibility = View.VISIBLE
                }
                Resource.Status.EMPTY -> {
                    Log.e("setObservers", "Empty")
//                    binding.progressBar.visibility = View.GONE
//                    binding.emptyDialog.visibility = View.VISIBLE
                }
                Resource.Status.SUCCESS -> {
                    Log.e("setObservers", "Success")
//                    binding.progressBar.visibility = View.GONE
//                    binding.emptyDialog.visibility = View.GONE
//                    ImageList = it.data
                    Log.e("Result",it.data.toString())
                    MatchInfo=it.data
                    SetUpRecyclerView()
                }
                Resource.Status.ERROR -> {
                    Log.e("setObservers", it.error.toString())
//                    binding.progressBar.visibility = View.GONE
//                    binding.emptyDialog.visibility = View.VISIBLE
                    Toast.makeText(this, it.error.toString(), Toast.LENGTH_SHORT).show()
                }

                else -> {}
            }
        })
    }

    fun SetUpRecyclerView(){
        var matchList:MutableList<Match> = arrayListOf()
//        Log.e("kkkk",MatchInfo.toString())
        val list=MatchInfo?.matchDetails

        for(i in 0..MatchInfo?.matchDetails?.size!!-1){
//            Log.e("num",i.toString())
            try {
                matchList.add(MatchInfo?.matchDetails!![i].matchDetailsMap.match[0])
            }catch (e:Exception){
                Log.e("Error",e.toString())
            }

        }


        var allMatches:MutableList<Match> = arrayListOf()
        var count1:Int=0
        var count2:Int=0
        var count3:Int=0

        for(i in matchList){
            try {
                allMatches.add(i)

                if(i.matchInfo.state=="Complete") count1++
                else if(i.matchInfo.state=="Upcoming")count2++
                else count3++


            }catch (e:Exception){
                Log.e("error_ll",e.toString())
            }

//            allMatches.addAll(matchListUpcoming)
//            allMatches.addAll(matchListLive)
//            allMatches.addAll(matchListPast)

        }
//        Log.e("Match Upcoming",matchListUpcoming.toString())
        val adapterUp=MatchAdapter(this,allMatches, count1,count3)


        try {
            val rvUp =findViewById<RecyclerView>(R.id.rv_upMatches)
            rvUp.layoutManager=LinearLayoutManager(this)
            rvUp.adapter=adapterUp
        }catch (e:Exception){
            Log.e("error",e.toString())
        }



//        val adapterLive=MatchAdapter(this,matchListLive)//
//        try {
//            val rvLive =findViewById<RecyclerView>(R.id.rv_liveMatches)
//            rvLive.layoutManager=LinearLayoutManager(this)
//            rvLive.adapter=adapterLive
//        }catch (e:Exception){
//            Log.e("error",e.toString())
//        }
//
//
//        val adapterPast=MatchAdapter(this,matchListPast)
//
//        try {
//            val rvLive =findViewById<RecyclerView>(R.id.rv_pastMatches)
//            rvLive.layoutManager=LinearLayoutManager(this)
//            rvLive.adapter=adapterPast
//        }catch (e:Exception){
//            Log.e("error",e.toString())
//        }


        Log.e("Done","Done")
    }

}