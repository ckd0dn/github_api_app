package com.example.githubapiapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubapiapp.adapter.UserAdapter
import com.example.githubapiapp.databinding.ActivityMainBinding
import com.example.githubapiapp.model.Repo
import com.example.githubapiapp.model.UserDto
import com.example.githubapiapp.network.GithubService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var userAdapter: UserAdapter

    private val handler = Handler(Looper.getMainLooper())
    private var searchFor: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userAdapter = UserAdapter {
            intent = Intent(this@MainActivity, RepoActivity::class.java)
            intent.putExtra("username", it.username)
            startActivity(intent)
        }

        binding.userRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = userAdapter
        }

        //디바운싱
        val runnable = Runnable {
            searchUser()
        }

        binding.searchEditText.addTextChangedListener {

            searchFor = it.toString()
            handler.removeCallbacks(runnable)
            handler.postDelayed(
                runnable,
                300
            )
        }



    }

    private fun searchUser() {
        val githubService = ApiClient.retrofit.create(GithubService::class.java)

        githubService.searchUsers(searchFor).enqueue(object: Callback<UserDto> {
            override fun onResponse(call: Call<UserDto>, response: Response<UserDto>) {
                Log.e("user", response.body().toString())

                userAdapter.submitList(response.body()?.items)

            }

            override fun onFailure(call: Call<UserDto>, t: Throwable) {
                Toast.makeText(this@MainActivity, "에러가 발생했습니다", Toast.LENGTH_SHORT)
                t.printStackTrace()

            }

        })
    }
}