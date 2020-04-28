package com.example.fadi;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fadi.Interface.ItemClickListener;
import com.example.fadi.Model.Tests;
import com.example.fadi.ViewHolder.TestsViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;


public class TestsFragment extends Fragment {

    View myFragment;

    RecyclerView listTests;
    RecyclerView.LayoutManager layoutManager;
    int latoyi;


    FirebaseRecyclerAdapter adapter;
    FirebaseDatabase database;
    DatabaseReference tests;

    public static TestsFragment newInstance(){
        TestsFragment testsFragment = new TestsFragment();
        return testsFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        database=FirebaseDatabase.getInstance();
        tests=database.getReference("Tests");





    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myFragment=inflater.inflate(R.layout.fragment_tests,container,false);
        listTests =(RecyclerView)myFragment.findViewById(R.id.listTest);
        listTests.setHasFixedSize(true);
        layoutManager= new LinearLayoutManager(container.getContext());
        listTests.setLayoutManager(layoutManager);

        onStart();

        return myFragment;

    }
    @Override
    public void onStart() {
        super.onStart();
        Query query = tests;
    FirebaseRecyclerOptions<Tests> options = new FirebaseRecyclerOptions.Builder<Tests>()
            .setQuery(query, Tests.class)
            .build();

    adapter=new FirebaseRecyclerAdapter<Tests,TestsViewHolder>(options) {
        @NonNull
        @Override
        public TestsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.tests_layout, parent, false);

            return new TestsViewHolder(view);
        }

        @Override
        protected void onBindViewHolder(@NonNull TestsViewHolder holder, int position, @NonNull final Tests model) {
            holder.test_name.setText(model.getName());
            Picasso.get()
                    .load(model.getImage())
                    .into(holder.test_image);

            holder.setItemClickListener(new ItemClickListener() {
                @Override
                public void onClick(View view, int position, boolean isLongClick) {
                    Toast.makeText(getActivity(),String.format("%s | %s",adapter.getRef(position).getKey(),model.getName()) , Toast.LENGTH_SHORT).show();

                }
            });

        }
    };
       adapter.startListening();
       listTests.setAdapter(adapter);
}
}
