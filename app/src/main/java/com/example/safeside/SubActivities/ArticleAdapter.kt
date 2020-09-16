package com.example.safeside.SubActivities

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.safeside.DataModel.Article
import com.example.safeside.R
import kotlinx.android.synthetic.main.article_layout.view.*

class ArticleAdapter(private val list : List<Article> ) : RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.article_layout, parent, false)
        return ArticleViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val currentItem = list[position]

        holder.name.text = currentItem.userName
        holder.body.text = currentItem.articleBody
        holder.upVotes.text = currentItem.articleUpVotes
        holder.comments.text = currentItem.articleReviews
        holder.date.text = currentItem.articleDate
        holder.time.text = currentItem.articleTime
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val name : TextView = itemView.userName
        val body : TextView = itemView.articleBody
        val upVotes : TextView = itemView.upVotesNo
        val comments : TextView = itemView.reviewsNo
        val date : TextView = itemView.articleDate
        val time : TextView = itemView.articleTime
    }
}