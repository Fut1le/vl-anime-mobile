package com.vlanime.view.ongoings

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.vlanime.R
import com.vlanime.core.dataLoader.loadOngoings
import com.vlanime.core.helper.isGoogleTV
import com.vlanime.core.model.PreviewTitleModel
import com.vlanime.view.adapter.TitlesAdapter
import com.vlanime.view.adapter.listener.OnLoadMoreListener
import com.vlanime.view.adapter.listener.RecyclerViewLoadMoreScroll
import com.vlanime.view.details.DetailsActivity
import com.vlanime.view.search.SearchActivity
import kotlinx.android.synthetic.main.activity_ongoings.*


class OngoingsActivity : AppCompatActivity(), TitlesAdapter.ItemClickListener {

    private lateinit var layoutManager: GridLayoutManager
    private lateinit var scrollListener: RecyclerViewLoadMoreScroll
    private lateinit var adapter: TitlesAdapter
    private var spanCount = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ongoings)

        search_badge.setOnClickListener {
            startActivity(Intent(this, SearchActivity::class.java))
        }

        spanCount = if (isGoogleTV()) 6 else 2

        layoutManager = GridLayoutManager(this, spanCount)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int = when (position) {
                0 -> spanCount
                else -> 1
            }
        }

        content.layoutManager = layoutManager
        adapter = TitlesAdapter(this@OngoingsActivity, mutableListOf())
        adapter.setClickListener(this)
        content.adapter = adapter

        scrollListener =
            RecyclerViewLoadMoreScroll(content.layoutManager as GridLayoutManager)
        scrollListener.setOnLoadMoreListener(object :
            OnLoadMoreListener {
            override fun onLoadMore(page: Int) {
                loadData(page)
            }
        })

        content.addOnScrollListener(scrollListener)

        showLoading()

        loadData(1)
    }

    private fun loadData(page: Int) {
        loadOngoings(page) {
            hideLoading()

            adapter.addAll(it)
            scrollListener.setLoaded()
        }
    }

    private fun showLoading() {
        progress_bar_layout.visibility = View.VISIBLE
        search_badge.visibility = View.GONE
    }

    private fun hideLoading() {
        progress_bar_layout.visibility = View.GONE
        search_badge.visibility = View.VISIBLE
    }

    override fun onItemClick(view: View?, previewTitleModel: PreviewTitleModel) {
        startActivity(DetailsActivity.getIntent(this@OngoingsActivity, previewTitleModel))
    }
}