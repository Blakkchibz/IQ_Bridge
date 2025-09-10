package com.example.iqbridge.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.iqbridge.R
import com.example.iqbridge.databinding.ActivityMainBinding
import com.example.iqbridge.models.Category
import com.example.iqbridge.models.CategoryModel

class GridAdapter(private  val items:List<CategoryModel>,
    private val categoryStat:Map<String, Category>
    ):
RecyclerView.Adapter<GridAdapter.ViewHolder>() {

    private var touchResponse: TouchResponse? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GridAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.category_list_item,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: GridAdapter.ViewHolder, position: Int) {
        val item = items[position]
        holder.image.setImageResource(item.image)
        holder.categoryName.text = item.name
        val count = categoryStat[item.id]?.total_num_of_questions
        holder.questionCount.text = count.toString()+"questions"

        holder.itemView.setOnClickListener {
            if (touchResponse!=null){
                touchResponse!!.onClick(item.id.toInt())
            }
        }
    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val image: ImageView = itemView.findViewById(R.id.iv_category_icon)
        val categoryName: TextView = itemView.findViewById(R.id.tv_Category_name)
        val questionCount: TextView = itemView.findViewById(R.id.tv_no_of_questions)
    }

    fun setTouchResponse(touchResponse: TouchResponse){
        this.touchResponse = touchResponse
    }

    interface TouchResponse{
        fun onClick(id: Int)
    }


}