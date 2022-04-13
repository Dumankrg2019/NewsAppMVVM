package com.androiddevs.mvvmnewsapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androiddevs.mvvmnewsapp.NewsActivity
import com.androiddevs.mvvmnewsapp.R
import com.androiddevs.mvvmnewsapp.adapters.NewsAdapter
import com.androiddevs.mvvmnewsapp.ui.NewsViewModel
import com.androiddevs.mvvmnewsapp.util.Resource

class BreakingNewsFragment : Fragment(R.layout.fragment_breaking_news){

    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter
    val TAG = "BreakingNewsFragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewsActivity).viewModel


        //rcView init
        newsAdapter = NewsAdapter()
        val rcView = getView()?.findViewById<RecyclerView>(R.id.rvBreakingNews)
        rcView?.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }


        newsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(
                R.id.action_breakingNewsFragment_to_articleFragment,
                bundle
            )
        }

        viewModel.breakingNews.observe(viewLifecycleOwner, Observer { response ->
            when(response) {
                is Resource.Success -> {
//                    val paginationProgressbar = getView()?.findViewById<RecyclerView>(R.id.paginationProgressBar)
//                    paginationProgressbar?.visibility = View.INVISIBLE
                    response.data?.let {newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles)
                    }
                }
                is Resource.Error -> {
//                    val paginationProgressbar = getView()?.findViewById<RecyclerView>(R.id.paginationProgressBar)
//                    paginationProgressbar?.visibility = View.INVISIBLE
                    response.message?.let { message ->
                        Log.e(TAG, "An error occured: $message")
                    }
                }
                is Resource.Loading -> {
//                    val paginationProgressbar = getView()?.findViewById<RecyclerView>(R.id.paginationProgressBar)
//                    paginationProgressbar?.visibility = View.VISIBLE
                }
            }
        })
    }


}