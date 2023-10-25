package com.example.androidworrkshop.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.androidworrkshop.R
import com.example.androidworrkshop.databinding.ActivityLiveMatchInfoBinding
import com.example.androidworrkshop.databinding.ActivityMatchInfoBinding
import com.example.androidworrkshop.di.Resource
import com.example.androidworrkshop.model.leanback
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.roundToInt

@AndroidEntryPoint
class LiveMatchInfo : AppCompatActivity() {

    private var binding :ActivityLiveMatchInfoBinding?=null
    lateinit var viewModel : MainViewModel
    lateinit var  scoreCard:leanback
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityLiveMatchInfoBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val receivedIntent = intent
        var matchId = receivedIntent.getStringExtra("matchId")

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        if (matchId != null) {
            viewModel.getMatchScore(matchId)
        }else{
//            matchId="75451"
//            viewModel.getMatchScore(matchId)
        }

        setObservers()
    }



    private fun setObservers() {
        viewModel.performFetchMatchesScoreStatus.observe(this, Observer {
            when(it.status){
                Resource.Status.LOADING -> {
                    Log.d("setObservers", "Loading")
                    binding?.tvScorecard?.visibility=View.GONE
                    binding?.cvScorecard?.visibility=View.GONE
                    binding?.animationView?.visibility=View.VISIBLE

                }
                Resource.Status.EMPTY -> {
                    Log.d("setObservers", "Empty")
//                    binding.progressBar.visibility = View.GONE
//                    binding.emptyDialog.visibility = View.VISIBLE
                }
                Resource.Status.SUCCESS -> {
                    Log.d("setObservers", "Success")
//                    binding.progressBar.visibility = View.GONE
//                    binding.emptyDialog.visibility = View.GONE
//                    ImageList = it.data
                    Log.d("Result",it.data.toString())


                    binding?.tvScorecard?.visibility=View.VISIBLE
                    binding?.cvScorecard?.visibility=View.VISIBLE
                    binding?.animationView?.visibility=View.GONE

                    scoreCard= it.data!!
                    binding?.tvToss?.text=scoreCard.miniscore.matchScoreDetails.tossResults.tossWinnerName+" won and chose "+ scoreCard.miniscore.matchScoreDetails.tossResults.decision.lowercase()
                    binding?.tvTeam1?.text=scoreCard.miniscore.matchScoreDetails.matchTeamInfo[0].battingTeamShortName
                    binding?.tvTeam2?.text=scoreCard.miniscore.matchScoreDetails.matchTeamInfo[0].bowlingTeamShortName
                    binding?.tvTeam1Score?.text=scoreCard.miniscore.matchScoreDetails.inningsScoreList[1].score.toString()+"/"+scoreCard.miniscore.matchScoreDetails.inningsScoreList[1].wickets.toString()
                    binding?.tvTeam2Score?.text=scoreCard.miniscore.matchScoreDetails.inningsScoreList[0].score.toString()+"/"+scoreCard.miniscore.matchScoreDetails.inningsScoreList[0].wickets.toString()
                    binding?.tvBatter1?.text=scoreCard.miniscore.batsmanStriker.batName
                    binding?.tvBatter1Score?.text=scoreCard.miniscore.batsmanStriker.batRuns.toString()+" ("+scoreCard.miniscore.batsmanStriker.batBalls.toString()+")"
                    binding?.tvBatter2?.text=scoreCard.miniscore.batsmanNonStriker.batName
                    binding?.tvBatter2Score?.text=scoreCard.miniscore.batsmanNonStriker.batRuns.toString()+" ("+scoreCard.miniscore.batsmanNonStriker.batBalls.toString()+")"
                    binding?.tvBowler1?.text=scoreCard.miniscore.bowlerStriker.bowlName
                    binding?.tvBowler1Score?.text=scoreCard.miniscore.bowlerStriker.bowlWkts.toString()+"/"+scoreCard.miniscore.bowlerStriker.bowlRuns+" ("+scoreCard.miniscore.bowlerStriker.bowlOvs.toString()+")"
                    binding?.tvBowler2?.text=scoreCard.miniscore.bowlerNonStriker.bowlName
                    binding?.tvBowler2Score?.text=scoreCard.miniscore.bowlerNonStriker.bowlWkts.toString()+"/"+scoreCard.miniscore.bowlerNonStriker.bowlRuns+" ("+scoreCard.miniscore.bowlerNonStriker.bowlOvs.toString()+")"
                    binding?.tvTeam1Overs?.text = "(" + scoreCard.miniscore.matchScoreDetails.inningsScoreList[1].overs + ")"
                    binding?.tvTeam2Overs?.text = "(" + scoreCard.miniscore.matchScoreDetails.inningsScoreList[0].overs + ")"
                    binding?.tvCRR?.text="CRR: "+scoreCard.miniscore.currentRunRate.toString()
                    if(scoreCard.miniscore.recentOvsStats.isNullOrEmpty()||scoreCard.miniscore.recentOvsStats.isBlank())
                    {
                        binding?.llRecentOvers?.visibility=View.GONE
                    }
                    else
                    {
                        binding?.tvRecentOvers?.text=scoreCard.miniscore.recentOvsStats
                    }
                    if(scoreCard.miniscore.lastWicket.isNullOrEmpty()||scoreCard.miniscore.lastWicket.isBlank())
                    {
                        binding?.llLastWicket?.visibility=View.GONE
                    }
                    else
                    {
                        binding?.tvLastWicket?.text=scoreCard.miniscore.lastWicket
                    }
                    if(scoreCard.miniscore.partnerShip.runs.toString().isNullOrEmpty()||scoreCard.miniscore.partnerShip.runs.toString().isBlank())
                    {
                        binding?.llPartnership?.visibility=View.GONE
                    }
                    else
                    {
                        binding?.tvPartnership?.text=scoreCard.miniscore.partnerShip.runs.toString()+" ("+scoreCard.miniscore.partnerShip.balls.toString()+")"
                    }
                    if(scoreCard.miniscore.requiredRunRate.toString().isNullOrEmpty()||scoreCard.miniscore.requiredRunRate.toString().isBlank())
                    {
                        binding?.tvRRR?.visibility=View.GONE
                    }
                    else
                    {
                        binding?.tvRRR?.text="RRR: "+scoreCard.miniscore.requiredRunRate.toString()
                    }

                    binding?.tvResult?.text=scoreCard.miniscore.matchScoreDetails.customStatus


                    if(scoreCard.miniscore.matchScoreDetails.matchTeamInfo[0].bowlingTeamShortName=="IND") {
                        Glide.with(this)
                            .load("https://img1.hscicdn.com/image/upload/f_auto/lsci/db/PICTURES/CMS/313100/313128.logo.png")

                            .into(binding?.ivTeam2!!)
                    }
                    else if(scoreCard.miniscore.matchScoreDetails.matchTeamInfo[0].bowlingTeamShortName=="AUS") {
                        Glide.with(this)
                            .load("https://www.espncricinfo.com/db/PICTURES/CMS/340400/340493.png")
                            .into(binding?.ivTeam2!!)
                    }
                    else if(scoreCard.miniscore.matchScoreDetails.matchTeamInfo[0].bowlingTeamShortName=="RSA") {
                        Glide.with(this)
                            .load("https://img1.hscicdn.com/image/upload/f_auto/lsci/db/PICTURES/CMS/313100/313125.logo.png")
                            .into(binding?.ivTeam2!!)
                    }
                    else if(scoreCard.miniscore.matchScoreDetails.matchTeamInfo[0].bowlingTeamShortName=="NZ") {
                        Glide.with(this)
                            .load("https://img1.hscicdn.com/image/upload/f_auto,t_ds_w_1200/lsci/db/PICTURES/CMS/340500/340505.png")
                            .into(binding?.ivTeam2!!)
                    }
                    else if(scoreCard.miniscore.matchScoreDetails.matchTeamInfo[0].bowlingTeamShortName=="PAK") {
                        Glide.with(this)
                            .load("https://img1.hscicdn.com/image/upload/f_auto/lsci/db/PICTURES/CMS/313100/313129.logo.png")
                            .into(binding?.ivTeam2!!)
                    }
                    else if(scoreCard.miniscore.matchScoreDetails.matchTeamInfo[0].bowlingTeamShortName=="NED") {
                        Glide.with(this)
                            .load("https://www.espncricinfo.com/db/PICTURES/CMS/313100/313136.logo.png")
                            .into(binding?.ivTeam2!!)
                    }
                    else if(scoreCard.miniscore.matchScoreDetails.matchTeamInfo[0].bowlingTeamShortName=="SL") {
                        Glide.with(this)
                            .load("https://img1.hscicdn.com/image/upload/f_auto/lsci/db/PICTURES/CMS/340000/340047.png")
                            .into(binding?.ivTeam2!!)
                    }
                    else if(scoreCard.miniscore.matchScoreDetails.matchTeamInfo[0].bowlingTeamShortName=="AFG") {
                        Glide.with(this)
                            .load("https://img1.hscicdn.com/image/upload/f_auto,t_ds_square_w_160,q_50/lsci/db/PICTURES/CMS/321000/321005.png")
                            .into(binding?.ivTeam2!!)
                    }
                    else if(scoreCard.miniscore.matchScoreDetails.matchTeamInfo[0].bowlingTeamShortName=="ENG") {
                        Glide.with(this)
                            .load("https://img1.hscicdn.com/image/upload/f_auto/lsci/db/PICTURES/CMS/313100/313114.logo.png")
                            .into(binding?.ivTeam2!!)
                    }
                    else if(scoreCard.miniscore.matchScoreDetails.matchTeamInfo[0].bowlingTeamShortName=="BAN") {
                        Glide.with(this)
                            .load("https://img1.hscicdn.com/image/upload/f_auto,t_ds_w_1200/lsci/db/PICTURES/CMS/341400/341455.png")
                            .into(binding?.ivTeam2!!)
                    }

                    if(scoreCard.miniscore.matchScoreDetails.matchTeamInfo[0].battingTeamShortName=="IND") {
                        Glide.with(this)
                            .load("https://img1.hscicdn.com/image/upload/f_auto/lsci/db/PICTURES/CMS/313100/313128.logo.png")
                            .into(binding?.ivTeam1!!)
                    }
                    else if(scoreCard.miniscore.matchScoreDetails.matchTeamInfo[0].battingTeamShortName=="AUS") {
                        Glide.with(this)
                            .load("https://www.espncricinfo.com/db/PICTURES/CMS/340400/340493.png")
                            .into(binding?.ivTeam1!!)
                    }
                    else if(scoreCard.miniscore.matchScoreDetails.matchTeamInfo[0].battingTeamShortName=="RSA") {
                        Glide.with(this)
                            .load("https://img1.hscicdn.com/image/upload/f_auto/lsci/db/PICTURES/CMS/313100/313125.logo.png")
                            .into(binding?.ivTeam1!!)
                    }
                    else if(scoreCard.miniscore.matchScoreDetails.matchTeamInfo[0].battingTeamShortName=="NZ") {
                        Glide.with(this)
                            .load("https://img1.hscicdn.com/image/upload/f_auto,t_ds_w_1200/lsci/db/PICTURES/CMS/340500/340505.png")
                            .into(binding?.ivTeam1!!)
                    }
                    else if(scoreCard.miniscore.matchScoreDetails.matchTeamInfo[0].battingTeamShortName=="PAK") {
                        Glide.with(this)
                            .load("https://img1.hscicdn.com/image/upload/f_auto/lsci/db/PICTURES/CMS/313100/313129.logo.png")
                            .into(binding?.ivTeam1!!)
                    }
                    else if(scoreCard.miniscore.matchScoreDetails.matchTeamInfo[0].battingTeamShortName=="NED") {
                        Glide.with(this)
                            .load("https://www.espncricinfo.com/db/PICTURES/CMS/313100/313136.logo.png")
                            .into(binding?.ivTeam1!!)
                    }
                    else if(scoreCard.miniscore.matchScoreDetails.matchTeamInfo[0].battingTeamShortName=="SL") {
                        Glide.with(this)
                            .load("https://img1.hscicdn.com/image/upload/f_auto/lsci/db/PICTURES/CMS/340000/340047.png")
                            .into(binding?.ivTeam1!!)
                    }
                    else if(scoreCard.miniscore.matchScoreDetails.matchTeamInfo[0].battingTeamShortName=="AFG") {
                        Glide.with(this)
                            .load("https://img1.hscicdn.com/image/upload/f_auto,t_ds_square_w_160,q_50/lsci/db/PICTURES/CMS/321000/321005.png")
                            .into(binding?.ivTeam1!!)
                    }
                    else if(scoreCard.miniscore.matchScoreDetails.matchTeamInfo[0].battingTeamShortName=="ENG") {
                        Glide.with(this)
                            .load("https://img1.hscicdn.com/image/upload/f_auto/lsci/db/PICTURES/CMS/313100/313114.logo.png")
                            .into(binding?.ivTeam1!!)
                    }
                    else if(scoreCard.miniscore.matchScoreDetails.matchTeamInfo[0].battingTeamShortName=="BAN") {
                        Glide.with(this)
                            .load("https://img1.hscicdn.com/image/upload/f_auto,t_ds_w_1200/lsci/db/PICTURES/CMS/341400/341455.png")
                            .into(binding?.ivTeam1!!)
                    }

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
}

