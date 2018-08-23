package s.kouta.janken

import android.os.Bundle
import android.preference.PreferenceManager
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity

import kotlinx.android.synthetic.main.activity_result.*
import org.jetbrains.anko.defaultSharedPreferences

class ResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        val gu=0
        val choki=1
        val pa=2

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val id=intent.getIntExtra("MY_HAND",0)
        val myHand:Int = when(id){
            R.id.gu->{
                my_hand.setImageResource(R.drawable.gu)
                gu
            }
            R.id.choki->{
                my_hand.setImageResource(R.drawable.choki)
                choki
            }
            R.id.pa->{
                my_hand.setImageResource(R.drawable.pa)
                pa
            }
            else -> gu
        }

        val comHand= getHand()
        when(comHand){
            gu->com_hand.setImageResource(R.drawable.com_gu)
            choki->com_hand.setImageResource(R.drawable.com_choki)
            pa->com_hand.setImageResource(R.drawable.com_pa)
        }

        val result=(comHand-myHand+3) % 3
        when(result){
            0->result_text.setText(R.string.result_draw)
            1->result_text.setText(R.string.result_win)
            2->result_text.setText(R.string.result_lose)
        }

        button_back.setOnClickListener{
            finish()
        }
        saveData(myHand,comHand,result)
    }

    private fun saveData(myHand:Int,comHand:Int,result:Int){
        val pref=PreferenceManager.getDefaultSharedPreferences(this)
        val gameCount=pref.getInt("GAME_COUNT",0)
        val winningSteakCount=pref.getInt("WINNING_STEAK_COUNT",0)
        val lastConHand = pref.getInt("LAST_COUNT",0)
        val lastGameresult = pref.getInt("LAST_GAME_RESULT",0)

        val editer=pref.edit()
        editer.putInt("GAME_COUNT",gameCount+1)
                .putInt("WINNING_STEAK_COUNT"
                        ,if(lastGameresult==2 && gameCount==2)
                            winningSteakCount+1
                        else
                            0)
                .putInt("LAST_MY_HAND",myHand)
                .putInt("LAST_COM_HAND",comHand)
                .putInt("BEFORE_LAST_COM_HAND",lastConHand)
                .putInt("GAME_RESULT",result)
                .apply()
    }

    private fun getHand():Int{
        var hand =(Math.random()*3).toInt()
        val pref=PreferenceManager.getDefaultSharedPreferences(this)
        val gameCount=pref.getInt("GAME_COUNT",0)
        val winningSteakCount=pref.getInt("WINNING_STEAK_COUNT",0)
        val lastMyHand = pref.getInt("LAST_MY_HAND",0)
        val lastComHand=pref.getInt("LAST_COM_HAND",0)
        val beforeLastComHand=pref.getInt("BEFORE_LAST_COM_HAND",0)
        val gameResult=pref.getInt("GAME_RESULT",-1)

        if(gameCount==1){
            if(gameResult==2){
                while (lastComHand==hand){
                    hand=(Math.random()*3).toInt()
                }
            }else if(gameResult==1){
                hand=(lastMyHand - 1 + 3) % 3
            }
        }else if (winningSteakCount>0){
            if(beforeLastComHand==lastComHand){
                while (lastComHand==hand){
                    hand=(Math.random()*3).toInt()
                }
            }
        }
        return hand
    }

}
