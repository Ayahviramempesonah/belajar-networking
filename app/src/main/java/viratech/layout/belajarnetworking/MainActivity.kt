package viratech.layout.belajarnetworking

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject
import viratech.layout.belajarnetworking.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    companion object{
        private val TAG = MainActivity::class.java.simpleName
    }

  private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)


        getRandomQuote()
        // panggil button dari sini di method onCreate
        binding.button.setOnClickListener {
            startActivity(Intent(this@MainActivity,ListQuotesActivity::class.java))
        }



    }

    private fun getRandomQuote(){
        binding.progressBar2.visibility = View.VISIBLE
        val client = AsyncHttpClient()
        val url ="https://quote-api.dicoding.dev/random"
       // val url ="https://quote-api.dicoding.dev/list"
        client.get(url,object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray) {
                // jika proses pengambilan data success
                binding.progressBar2.visibility=View.VISIBLE

                val result = String(responseBody)
                Log.d(TAG,result)
                try {
                    val responseObject= JSONObject(result)

                    val quote = responseObject.getString("en")
                    val author = responseObject.getString("author")

                    binding.textViewQuote.text=quote
                    binding.textViewAuthor.text=author

                }catch (e : Exception){
                    Toast.makeText(this@MainActivity,e.message,Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {

                //jika koneksi gagal
                binding.progressBar2.visibility=View.VISIBLE
            val errorMessage = when (statusCode){

                401 -> "$statusCode : Bad Request"
                403 -> "$statusCode : Forbidden"
                404 -> "$statusCode : Not Found "
                else -> "$statusCode : ${error?.message}"
            }


            }
        }



        )

        }
    }



//}