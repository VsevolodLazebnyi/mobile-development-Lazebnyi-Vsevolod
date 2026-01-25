package com.example.massenger;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.massenger.model.Message;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class EnhancedMessageAdapter extends RecyclerView.Adapter<EnhancedMessageAdapter.MessageViewHolder> {
    private List<Message> messages = new ArrayList<>();
    private OnMessageClickListener listener;

    public interface OnMessageClickListener {
        void onLikeClick(int position, Message message);
    }

    public EnhancedMessageAdapter(OnMessageClickListener listener) {
        this.listener = listener;
    }

    public void setMessages(List<Message> messages) {
        if (messages != null) {
            List<Message> sortedMessages = new ArrayList<>(messages);
            Collections.sort(sortedMessages, (m1, m2) -> {
                Long time1 = m1.timestamp != null ? m1.timestamp : 0L;
                Long time2 = m2.timestamp != null ? m2.timestamp : 0L;
                return time2.compareTo(time1);
            });

            this.messages.clear();
            this.messages.addAll(sortedMessages);
            notifyDataSetChanged();
        }
    }

    public void addNewMessages(List<Message> newMessages) {
        if (newMessages != null && !newMessages.isEmpty()) {
            Collections.sort(newMessages, (m1, m2) -> {
                Long time1 = m1.timestamp != null ? m1.timestamp : 0L;
                Long time2 = m2.timestamp != null ? m2.timestamp : 0L;
                return time2.compareTo(time1);
            });

            messages.addAll(0, newMessages);
            notifyItemRangeInserted(0, newMessages.size());
        }
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_message_enhanced, parent, false);
        return new MessageViewHolder(view);
    }

    private String formatTime(Message message) {
        if (message.messageTime != null
                && !message.messageTime.trim().isEmpty()
                && !"null".equalsIgnoreCase(message.messageTime.trim())) {
            return message.messageTime;
        }
        if (message.timestamp != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
            sdf.setTimeZone(TimeZone.getDefault());
            return sdf.format(new Date(message.timestamp));
        }
        return "";
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message message = messages.get(position);

        int avatarResource = R.drawable.default_avatar;

        if (message.avatarUrl != null && !message.avatarUrl.isEmpty()
            && !message.avatarUrl.equals("")) {
            Glide.with(holder.itemView.getContext())
                    .load(message.avatarUrl)
                    .placeholder(R.drawable.default_avatar)
                    .error(R.drawable.default_avatar)
                    .into(holder.avatarImageView);
        } else {
            holder.avatarImageView.setImageResource(R.drawable.default_avatar);
        }

        holder.userNameTextView.setText(message.userName != null ?
            message.userName : "Пользователь " + message.userId);
        holder.messageTextView.setText(message.body);
        holder.timeTextView.setText(formatTime(message));

        holder.likeCountTextView.setText(String.valueOf(message.likeCount));
        holder.likeButton.setImageResource(
            message.isLiked ? R.drawable.ic_like_filled : R.drawable.ic_like_empty
        );

        holder.likeButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onLikeClick(position, message);
            }
        });
    }

    @Override
    public int getItemCount() {
        return messages != null ? messages.size() : 0;
    }

    static class MessageViewHolder extends RecyclerView.ViewHolder {
        ImageView avatarImageView;
        TextView userNameTextView;
        TextView messageTextView;
        TextView timeTextView;
        ImageView likeButton;
        TextView likeCountTextView;

        MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            avatarImageView = itemView.findViewById(R.id.avatarImageView);
            userNameTextView = itemView.findViewById(R.id.userNameTextView);
            messageTextView = itemView.findViewById(R.id.messageTextView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            likeButton = itemView.findViewById(R.id.likeButton);
            likeCountTextView = itemView.findViewById(R.id.likeCountTextView);
        }
    }
}