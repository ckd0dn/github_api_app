package com.example.githubapiapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.githubapiapp.adapter.RepoAdapter
import com.example.githubapiapp.databinding.ActivityRepoBinding
import com.example.githubapiapp.model.Repo
import com.example.githubapiapp.network.GithubService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RepoActivity: AppCompatActivity() {

    private lateinit var binding: ActivityRepoBinding
    private lateinit var repoAdapter: RepoAdapter

    private var page = 0
    private var hasMore = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRepoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //인텐트로 넘어온 값 받기
        val username = intent.getStringExtra("username") ?: return


        binding.usernameTextView.text = username

        repoAdapter = RepoAdapter {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it.htmlUrl))
            startActivity(intent)
        }
        val linearLayoutManager = LinearLayoutManager(this@RepoActivity)

        //리사이클러뷰 적용 1. 레이아웃 매니저 2. 어댑터
        binding.repoRecyclerView.apply {
            layoutManager = linearLayoutManager
            adapter = repoAdapter

        }

        //페이징 처리
        binding.repoRecyclerView.addOnScrollListener(object: RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val totalCount = linearLayoutManager.itemCount
                val lastVIsiblePosition = linearLayoutManager.findLastVisibleItemPosition()

                //스크롤 포지션이 아이템 개수 이상이 되면 && 아이템이 30개이상
                if(lastVIsiblePosition >= (totalCount - 1) && hasMore){

                    page += 1
                    listRepo(username, page)
                }
            }
        })

        listRepo(username, 0)

    }

    private fun listRepo(username: String, page: Int) {
        val githubService = ApiClient.retrofit.create(GithubService::class.java)
        githubService.listRepos(username, page).enqueue(object: Callback<List<Repo>> {
            override fun onResponse(call: Call<List<Repo>>, response: Response<List<Repo>>) {
                Log.e("repo", response.body().toString())

                hasMore = response.body()?.count() == 30

                //orEmpty 빈리스트로 치환
                //기존의 리스트와 가져올 리스트를 합침
                repoAdapter.submitList(repoAdapter.currentList + response.body().orEmpty())
            }

            override fun onFailure(call: Call<List<Repo>>, t: Throwable) {

            }

        })
    }
}