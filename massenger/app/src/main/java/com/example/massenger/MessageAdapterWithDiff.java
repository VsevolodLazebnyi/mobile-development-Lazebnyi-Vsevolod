package com.example.massenger;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.massenger.model.Message;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class MessageAdapterWithDiff extends ListAdapter<Message, MessageAdapterWithDiff.MessageViewHolder> {

    private OnMessageClickListener listener;

    public interface OnMessageClickListener {
        void onLikeClick(String messageId);
    }

    public MessageAdapterWithDiff(OnMessageClickListener listener) {
        super(new MessageDiffCallback());
        this.listener = listener;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_message_enhanced, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message message = getItem(position);
        holder.bind(message, listener);
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

        void bind(Message message, OnMessageClickListener listener) {
            // Аватарка
            if (message.avatarUrl != null && !message.avatarUrl.isEmpty()) {
                Glide.with(itemView.getContext())
                        .load(message.avatarUrl)
                        .placeholder(R.drawable.default_avatar)
                        .error(R.drawable.default_avatar)
                        .circleCrop()
                        .into(avatarImageView);
            } else {
                avatarImageView.setImageResource(R.drawable.default_avatar);
            }

            userNameTextView.setText(message.userName != null ?
                    message.userName : "Пользователь " + message.userId);
            messageTextView.setText(message.body);
            timeTextView.setText(formatTime(message));

            // Лайки
            likeCountTextView.setText(String.valueOf(message.likeCount));
            likeButton.setImageResource(
                    message.isLiked ? R.drawable.ic_like_filled : R.drawable.ic_like_empty
            );

            likeButton.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onLikeClick(message.id);
                }
            });
        }

        private String formatTime(Message message) {
            if (message.messageTime != null && !message.messageTime.trim().isEmpty()
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
    }

    static class MessageDiffCallback extends DiffUtil.ItemCallback<Message> {
        @Override
        public boolean areItemsTheSame(@NonNull Message oldItem, @NonNull Message newItem) {
            return oldItem.id.equals(newItem.id);
        }

        @Override
        public boolean areContentsTheSame(@NonNull Message oldItem, @NonNull Message newItem) {
            return oldItem.isLiked == newItem.isLiked &&
                    oldItem.likeCount.equals(newItem.likeCount) &&
                    oldItem.body.equals(newItem.body);
        }
    }
}