package com.uottawa.olympus.olympusservices;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

import java.util.Iterator;
import java.util.List;

/**
 * screen for user list that the admin can access
 * and manage all users in the program. Userlist is generated through
 * the database using DBHelper class.
 *
 */

public class UsersList extends AppCompatActivity {

    /**
     * on Creation of this class the app loads up the xml page for
     * activity_user_list and generates a gridview using the database
     * for all the users in the app.
     *
     * @param savedInstanceState bundle to transfer data
     */

    //field for RecyclerView
    private RecyclerView mRecyclerView;
    //field for adapter of Recycler view
    private RecyclerView.Adapter mAdapter;
    //field for layout manager of Recyler view.
    private RecyclerView.LayoutManager mLayoutManager;

    private DBHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_list);

        dbHelper = new DBHelper(this);
        List<String[]> users = dbHelper.getAllUsers();
        String[] usernames = new String[(users.size())];
        Iterator iter = users.iterator();
        for (int i=0; i<users.size();i++){
            String[] current = (String[])iter.next();
            usernames[(i)] = current[0];
        }


        mRecyclerView = (RecyclerView) findViewById(R.id.Users);


        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new UsersList.MyAdapter(usernames, this);
        mRecyclerView.setAdapter(mAdapter);
    }


    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.UsersHolder> {

        private String[] users;
        private Context context;

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder

        // Provide a suitable constructor (depends on the kind of dataset)
        public MyAdapter(String[] users, Context context) {
            this.users = users;
        }

        // Create new views (invoked by the layout manager)
        @NonNull
        @Override
        public UsersHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.user_list_item, parent, false);
            return new UsersHolder(v);
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(UsersHolder holder, int position) {
            UserType user = dbHelper.findUserByUsername(users[position]);
            holder.name.setText(user.getUsername());
            holder.rate.setText(user.getRole());



        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return users.length;
        }

        class UsersHolder extends RecyclerView.ViewHolder{

            TextView name;
            TextView rate;

            public UsersHolder(View row){
                super(row);
                name = row.findViewById(R.id.Name);
                rate = row.findViewById(R.id.Role);
            }

            }


        }


}
