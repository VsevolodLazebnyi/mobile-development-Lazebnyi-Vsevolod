package com.example.massenger;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

public class ProfileFragment extends Fragment {
    
    private static final String TAG = "ProfileFragment";
    private ProfileViewModel profileViewModel;
    
    private TextView nameTextView;
    private TextView emailTextView;
    private EditText nameEditText;
    private EditText emailEditText;
    private Button saveButton;
    
    public ProfileFragment() {
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ProfileFragment создается");
        
        // Инициализируем ViewModel
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
    }
    
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ProfileFragment создает view");
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        nameTextView = view.findViewById(R.id.nameTextView);
        emailTextView = view.findViewById(R.id.emailTextView);
        nameEditText = view.findViewById(R.id.nameEditText);
        emailEditText = view.findViewById(R.id.emailEditText);
        saveButton = view.findViewById(R.id.saveButton);
        setupObservers();
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProfileData();
            }
        });
        
        return view;
    }
    
    private void setupObservers() {
        profileViewModel.getUserName().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String name) {
                Log.d(TAG, "Имя обновлено: " + name);
                nameTextView.setText(name);
                nameEditText.setText(name);
            }
        });
        profileViewModel.getUserEmail().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String email) {
                Log.d(TAG, "Email обновлен: " + email);
                emailTextView.setText(email);
                emailEditText.setText(email);
            }
        });
    }
    
    private void saveProfileData() {
        String newName = nameEditText.getText().toString();
        String newEmail = emailEditText.getText().toString();
        
        profileViewModel.setUserName(newName);
        profileViewModel.setUserEmail(newEmail);
        
        Log.i(TAG, "Данные профиля сохранены");
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated: ProfileFragment view создан");
    }
    
    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ProfileFragment становится видимым");
    }
    
    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ProfileFragment возобновляет работу");
    }
    
    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ProfileFragment приостанавливается");
    }
    
    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ProfileFragment останавливается");
    }
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "onDestroyView: ProfileFragment view уничтожается");
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ProfileFragment уничтожается");
    }
}