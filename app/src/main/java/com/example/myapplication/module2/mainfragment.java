package com.example.myapplication.module2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.example.myapplication.HomeFragment;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.adapter.QuizListAdapter;
import com.example.myapplication.model.QuizListModel;
import com.example.myapplication.viewmodel.QuizListViewModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link mainfragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class mainfragment extends Fragment implements QuizListAdapter.OnItemClickedListner {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ImageButton huongdanBtn;
    private ImageButton caudoBtn;
    private Toolbar backBtn;

    private ProgressBar detailProgressBar;
    private QuizListViewModel quizListViewModel;
    private RecyclerView recyclerView;
    private QuizListAdapter adapter;

    private List<QuizListModel> quizListModels;

    public mainfragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment mainfragment.
     */
    // TODO: Rename and change types and number of parameters
    public static mainfragment newInstance(String param1, String param2) {
        mainfragment fragment = new mainfragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        quizListViewModel = new ViewModelProvider(this , ViewModelProvider.AndroidViewModelFactory
                .getInstance(getActivity().getApplication())).get(QuizListViewModel.class);
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mainfragment, container, false);
        huongdanBtn = view.findViewById(R.id.huongdan);
        huongdanBtn.setOnClickListener((v) -> {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.hide(mainfragment.this);
            fragmentTransaction.add(R.id.frame_layout, new huongdanghichu());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();


        });

//        caudoBtn = view.findViewById(R.id.btn_module1);
//        caudoBtn.setOnClickListener((v) -> {
//            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//            fragmentTransaction.hide(mainfragment.this);
//            fragmentTransaction.add(R.id.frame_layout, new caudo());
//            fragmentTransaction.addToBackStack(null);
//            fragmentTransaction.commit();
//        });
        backBtn = view.findViewById(R.id.toolbar);
        backBtn.setOnClickListener((v) -> {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.hide(mainfragment.this);
            fragmentTransaction.add(R.id.frame_layout, new HomeFragment());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });


        detailProgressBar = view.findViewById(R.id.detailProgressBar);
        // View init list
        recyclerView = view.findViewById(R.id.listQuizRecyclerview);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new QuizListAdapter(this);

        recyclerView.setAdapter(adapter);

        quizListViewModel.getQuizListLiveData().observe(getViewLifecycleOwner(), new Observer<List<QuizListModel>>() {
            @Override
            public void onChanged(List<QuizListModel> quizList) {
                System.out.println(quizList.toArray());
                quizListModels = quizList;
                adapter.setQuizListModels(quizList);
                adapter.notifyDataSetChanged();
                detailProgressBar.setVisibility(View.GONE);
            }
        });

        return view;

    }

    @Override
    public void onItemClick(int position) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.hide(mainfragment.this);
        Intent i = new Intent(getContext(), DetailQuizActivity.class);
        i.putExtra("quiz", quizListModels.get(position));
        startActivity(i);
    }
}