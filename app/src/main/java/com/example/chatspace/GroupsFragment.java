package com.example.chatspace;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class GroupsFragment extends Fragment {
    private View groupFragmentView;
    private ListView list_view;
    private SearchView searchView;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> list_of_group = new ArrayList<>();
    private DatabaseReference GroupRef;

    public GroupsFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        groupFragmentView = inflater.inflate(R.layout.fragment_groups, container, false);
        GroupRef = FirebaseDatabase.getInstance().getReference().child("Groups"); //Reference to the firebase Database


        IntializeFields();

        RetrieveAndDisplayGroups(); // Method for retrieve and display group name
        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String currentGroupName = adapterView.getItemAtPosition(position).toString();
                Intent groupChatIntent = new Intent(getContext(), GroupChatActivity.class);
                groupChatIntent.putExtra("groupName", currentGroupName);
                startActivity(groupChatIntent);

            }
        });
        return groupFragmentView;
    }

    private void RetrieveAndDisplayGroups() {
        GroupRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Set<String> set = new HashSet<>(); // Containing list of all group name
                Iterator iterator = snapshot.getChildren().iterator();
                while (iterator.hasNext()) {
                    set.add(((DataSnapshot) iterator.next()).getKey());
                }
                list_of_group.clear();
                list_of_group.addAll(set);
                arrayAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void IntializeFields() {
        list_view = (ListView) groupFragmentView.findViewById(R.id.list_view);
        arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, list_of_group);
        list_view.setAdapter(arrayAdapter);

        searchView = (SearchView) groupFragmentView.findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                arrayAdapter.getFilter().filter(s);
                return false;
            }
        });

    }
}