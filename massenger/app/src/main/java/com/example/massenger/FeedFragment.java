package com.example.massenger;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log; // ДОБАВЬТЕ ЭТОТ ИМПОРТ
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import com.example.massenger.model.Message;
import com.example.massenger.workers.SyncWorker;
import com.google.android.material.snackbar.Snackbar;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class FeedFragment extends Fragment {
    private FeedViewModel viewModel;
    private EnhancedMessageAdapter adapter;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private static final int NOTIFICATION_PERMISSION_CODE = 100;
    private static final int AUTO_GENERATE_INTERVAL = 20000;
    private Handler autoGenerateHandler;
    private Runnable autoGenerateRunnable;
    private boolean isFirstLoad = true;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed, container, false);

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        recyclerView = view.findViewById(R.id.messagesRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new EnhancedMessageAdapter(new EnhancedMessageAdapter.OnMessageClickListener() {
            @Override
            public void onLikeClick(int position, Message message) {
                if (viewModel != null) {
                    viewModel.toggleLike(message);
                }
            }
        });

        recyclerView.setAdapter(adapter);

        swipeRefreshLayout.setOnRefreshListener(() -> {
            if (viewModel != null) {
                viewModel.generateNewMessages(2);
                new Handler().postDelayed(() -> {
                    if (recyclerView != null) {
                        recyclerView.scrollToPosition(0);
                    }
                    if (swipeRefreshLayout != null) {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                    Toast.makeText(getContext(), "Добавлено 2 новых сообщения", Toast.LENGTH_SHORT).show();
                }, 500);
            }
        });

        com.google.android.material.floatingactionbutton.FloatingActionButton fab = view.findViewById(R.id.fabRefresh);
        fab.setOnClickListener(v -> {
            if (viewModel != null) {
                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.setRefreshing(true);
                }
                viewModel.generateNewMessages(2);
                new Handler().postDelayed(() -> {
                    if (recyclerView != null) {
                        recyclerView.scrollToPosition(0);
                    }
                    if (swipeRefreshLayout != null) {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                    Toast.makeText(getContext(), "Добавлено 2 новых сообщения", Toast.LENGTH_SHORT).show();
                }, 500);
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(FeedViewModel.class);
        viewModel.initRepository(requireContext());

        viewModel.getMessages().observe(getViewLifecycleOwner(), new Observer<List<Message>>() {
            @Override
            public void onChanged(List<Message> messages) {
                if (adapter != null && messages != null) {
                    if (isFirstLoad) {
                        adapter.setMessages(messages);
                        isFirstLoad = false;
                    } else {
                        int oldCount = adapter.getItemCount();
                        adapter.setMessages(messages);
                        int newCount = messages.size() - oldCount;
                        if (newCount > 0) {
                            showNewMessagesNotification(newCount);
                        }
                    }
                }
            }
        });

        viewModel.getIsLoading().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {
                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.setRefreshing(isLoading != null && isLoading);
                }
            }
        });

        viewModel.getNewMessageCount().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer count) {
                if (count != null && count > 0) {
                    Log.d("FeedFragment", "Новых сообщений: " + count);
                }
            }
        });

        requestPermissions();
        setupAutoGeneration();
        setupPeriodicSync();
    }

    private void requestPermissions() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(requireContext(),
                    Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(requireActivity(),
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        NOTIFICATION_PERMISSION_CODE);
            }
        }
    }

    private void setupAutoGeneration() {
        autoGenerateHandler = new Handler();
        autoGenerateRunnable = new Runnable() {
            @Override
            public void run() {
                if (viewModel != null && isAdded() && getView() != null) {
                    viewModel.generateNewMessages(5);

                    if (isUserAtTop()) {
                        new Handler().postDelayed(() -> {
                            if (recyclerView != null) {
                                recyclerView.scrollToPosition(0);
                            }
                        }, 300);
                    }

                    Toast.makeText(getContext(), "Автоматически добавлено 5 новых сообщений", Toast.LENGTH_LONG).show();

                    Log.d("FeedFragment", "Автоматическая генерация 5 сообщений");
                }

                if (autoGenerateHandler != null) {
                    autoGenerateHandler.postDelayed(this, AUTO_GENERATE_INTERVAL);
                }
            }
        };
        autoGenerateHandler.postDelayed(autoGenerateRunnable, AUTO_GENERATE_INTERVAL);
    }

    private boolean isUserAtTop() {
        if (recyclerView != null && recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
            return firstVisibleItemPosition <= 3;
        }
        return false;
    }

    private void showNewMessagesNotification(int count) {
        if (getView() != null) {
            Snackbar snackbar = Snackbar.make(getView(),
                    "Получено " + count + " новых сообщений",
                    Snackbar.LENGTH_SHORT);
            snackbar.show();
        }
    }

    private void setupPeriodicSync() {
        try {
            Constraints constraints = new Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build();

            PeriodicWorkRequest syncRequest = new PeriodicWorkRequest.Builder(
                    SyncWorker.class,
                    15,
                    TimeUnit.MINUTES
            )
                    .setConstraints(constraints)
                    .build();

            WorkManager.getInstance(requireContext()).enqueue(syncRequest);
        } catch (Exception e) {
            Log.e("FeedFragment", "Ошибка настройки периодической синхронизации", e);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (viewModel != null) {
            viewModel.resetNewMessageCount();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (autoGenerateHandler != null && autoGenerateRunnable != null) {
            autoGenerateHandler.removeCallbacks(autoGenerateRunnable);
            autoGenerateHandler = null;
        }
    }
}