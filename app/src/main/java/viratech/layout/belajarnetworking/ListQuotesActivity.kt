package viratech.layout.belajarnetworking

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import viratech.layout.belajarnetworking.databinding.ActivityListQuotesBinding

class ListQuotesActivity : AppCompatActivity() {

    companion object{
        private val TAG = ListQuotesActivity::class.java.simpleName
    }

    private lateinit var binding : ActivityListQuotesBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityListQuotesBinding.inflate(layoutInflater)
        setContentView(binding.root)


        supportActionBar?.title = "List Of Quotes"

        val layoutManager= LinearLayoutManager(this)
        binding.rcListQuotes.setLayoutManager(layoutManager)

        val itemDecoration = DividerItemDecoration(this,layoutManager.orientation)
        binding.rcListQuotes.addItemDecoration(itemDecoration)

    }

    private fun getListQuotes(){
        binding.progressBarListquotes.visibility = View.INVISIBLE

        val client = AsyncHttpClient()
        val url = "https://quote-api.dicoding.dev/list"

        client.get(url,object : AsyncHttpResponseHandler(){
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?) {

                // jika sucess
                binding.progressBarListquotes.visibility = View.INVISIBLE


                val listQuotes = ArrayList<String> ()
                val result = String()
                Log.d(TAG,result)

                try {

                    val jsonArray = JSONArray(result)

                    for ( i in 0 until jsonArray.length()){
                        val jsonObject = jsonArray.getJSONObject(i)
                        val quote = jsonObject.getString("en")
                        val author = jsonObject.getString("author")
                        listQuotes.add("\n$quote\n - $author\n")
                    }

                    val adapter = QuoteAdapter(listQuotes)

                    binding.rcListQuotes.adapter = adapter

                } catch (e : Exception){

                    Toast.makeText(this@ListQuotesActivity,e.message,Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }



            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                // jika gagal

                binding.progressBarListquotes.visibility = View.INVISIBLE
                val errorMesage =when (statusCode){

                    401 -> "$statusCode : Bad Request"

                    403 -> "$statusCode : Forbiden "

                    404 -> "$statusCode  : Not Found "


                    else -> "$statusCode : ${error?.message}"
                }

                Toast.makeText(this@ListQuotesActivity,errorMesage,Toast.LENGTH_SHORT).show()

            }

        })
    }



}