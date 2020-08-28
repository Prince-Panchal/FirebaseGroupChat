package com.groupchat

import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.CommonKey
import com.Preference
import com.e.groupchat.R
import kotlinx.android.synthetic.main.chat_recycler.view.*

class ChatRecyclerView(
    val chat: Chat,
    val arrayList: ArrayList<Chat.ChatDataClass>
) : RecyclerView.Adapter<ChatRecyclerView.ViewHolder>() {
    class ViewHolder(var item: View) : RecyclerView.ViewHolder(item) {
        val tvOtherMessg = item.tvOtherMessg
        val tvMyMessg = item.tvMyMessg
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.chat_recycler, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            if (Preference.retrieveKey(chat, CommonKey.userId) == arrayList[position].userId) {
                holder.tvMyMessg.visibility = VISIBLE
                holder.tvMyMessg.text = arrayList[position].message
            } else {
                holder.tvOtherMessg.visibility = VISIBLE
                holder.tvOtherMessg.text = arrayList[position].message
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}